#include <jni.h>
#include <android/log.h>
#include <dlfcn.h>
#include <elf.h>
#include <link.h>
#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <time.h>
#include <zlib.h>

#define LOG_TAG "PhixitHook"
#define LOGI(...) phixit_log_write(ANDROID_LOG_INFO,  "I", __VA_ARGS__)
#define LOGD(...) phixit_log_write(ANDROID_LOG_DEBUG, "D", __VA_ARGS__)
#define LOGW(...) phixit_log_write(ANDROID_LOG_WARN,  "W", __VA_ARGS__)
#define LOGE(...) phixit_log_write(ANDROID_LOG_ERROR, "E", __VA_ARGS__)

static void phixit_log_write(int priority, const char *level, const char *format, ...);

#define LOG_MESSAGE_SIZE 2048

#define SQLITE_UTF8 1
#define SQLITE_DETERMINISTIC 0x800
#define SQLITE_TRANSIENT ((void (*)(void *))-1)
#define SQLITE_OK 0
#define SQLITE_ROW 100
#define SQLITE_DBCONFIG_ENABLE_TRIGGER 1003
#define SQLITE_DBCONFIG_DEFENSIVE 1010

#define MINIMAL_PHENOTYPE_VERSION 1033

#define PHIXIT_FLAG_FALSE 0
#define PHIXIT_FLAG_TRUE 1
#define PHIXIT_FLAG_INT 2
#define PHIXIT_FLAG_FLOAT 3
#define PHIXIT_FLAG_STRING 4
#define PHIXIT_FLAG_EXTENSION 5

static const char MERGE_FUNCTION_NAME[] = "gmsflags_phixit_merge_flags";
static const char CREATE_OVERRIDES_TABLE_SQL[] =
        "CREATE TABLE IF NOT EXISTS GmsFlagsOverrides ("
        "packageName TEXT NOT NULL,"
        "user TEXT,"
        "name TEXT NOT NULL,"
        "flagType INTEGER NOT NULL,"
        "intVal TEXT,"
        "boolVal TEXT,"
        "floatVal TEXT,"
        "stringVal TEXT,"
        "extensionVal BLOB,"
        "committed INTEGER NOT NULL DEFAULT 1,"
        "PRIMARY KEY(packageName, user, name)"
        ");";
static const char DISABLE_RECURSIVE_TRIGGERS_SQL[] =
        "PRAGMA recursive_triggers = OFF;";
static const char CREATE_INSERT_TRIGGER_SQL[] =
        "CREATE TEMP TRIGGER IF NOT EXISTS gmsflags_phixit_merge_insert "
        "AFTER INSERT ON main.param_partitions "
        "WHEN NEW.flags_content IS NOT NULL "
        "BEGIN "
        "UPDATE param_partitions "
        "SET flags_content = gmsflags_phixit_merge_flags(NEW.static_config_package_id, NEW.flags_content) "
        "WHERE rowid = NEW.rowid; "
        "END;";
static const char CREATE_UPDATE_TRIGGER_SQL[] =
        "CREATE TEMP TRIGGER IF NOT EXISTS gmsflags_phixit_merge_update "
        "AFTER UPDATE OF flags_content ON main.param_partitions "
        "WHEN NEW.flags_content IS NOT NULL "
        "BEGIN "
        "UPDATE param_partitions "
        "SET flags_content = gmsflags_phixit_merge_flags(NEW.static_config_package_id, NEW.flags_content) "
        "WHERE rowid = NEW.rowid; "
        "END;";

typedef struct sqlite3 sqlite3;
typedef struct sqlite3_stmt sqlite3_stmt;
typedef struct sqlite3_context sqlite3_context;
typedef struct sqlite3_value sqlite3_value;

typedef const char *(*sqlite3_db_filename_t)(sqlite3 *, const char *);
typedef int (*sqlite3_db_config_t)(sqlite3 *, int, ...);
typedef int (*sqlite3_create_function_v2_t)(sqlite3 *, const char *, int, int, void *,
        void (*)(sqlite3_context *, int, sqlite3_value **), void (*)(sqlite3_context *, int, sqlite3_value **),
        void (*)(sqlite3_context *), void (*)(void *));
typedef int (*sqlite3_exec_t)(sqlite3 *, const char *, int (*)(void *, int, char **, char **), void *, char **);
typedef sqlite3 *(*sqlite3_context_db_handle_t)(sqlite3_context *);
typedef int (*sqlite3_value_int_t)(sqlite3_value *);
typedef const void *(*sqlite3_value_blob_t)(sqlite3_value *);
typedef int (*sqlite3_value_bytes_t)(sqlite3_value *);
typedef void (*sqlite3_result_blob_t)(sqlite3_context *, const void *, int, void (*)(void *));
typedef void (*sqlite3_result_value_t)(sqlite3_context *, sqlite3_value *);
typedef void (*sqlite3_result_error_t)(sqlite3_context *, const char *, int);
typedef int (*sqlite3_prepare_v2_t)(sqlite3 *, const char *, int, sqlite3_stmt **, const char **);
typedef int (*sqlite3_bind_int_t)(sqlite3_stmt *, int, int);
typedef int (*sqlite3_step_t)(sqlite3_stmt *);
typedef int (*sqlite3_finalize_t)(sqlite3_stmt *);
typedef int (*sqlite3_column_int_t)(sqlite3_stmt *, int);
typedef const unsigned char *(*sqlite3_column_text_t)(sqlite3_stmt *, int);
typedef const void *(*sqlite3_column_blob_t)(sqlite3_stmt *, int);
typedef int (*sqlite3_column_bytes_t)(sqlite3_stmt *, int);
typedef const char *(*sqlite3_errmsg_t)(sqlite3 *);

typedef struct {
    sqlite3_db_filename_t db_filename;
    sqlite3_db_config_t db_config;
    sqlite3_create_function_v2_t create_function_v2;
    sqlite3_exec_t exec;
    sqlite3_context_db_handle_t context_db_handle;
    sqlite3_value_int_t value_int;
    sqlite3_value_blob_t value_blob;
    sqlite3_value_bytes_t value_bytes;
    sqlite3_result_blob_t result_blob;
    sqlite3_result_value_t result_value;
    sqlite3_result_error_t result_error;
    sqlite3_prepare_v2_t prepare_v2;
    sqlite3_bind_int_t bind_int;
    sqlite3_step_t step;
    sqlite3_finalize_t finalize;
    sqlite3_column_int_t column_int;
    sqlite3_column_text_t column_text;
    sqlite3_column_blob_t column_blob;
    sqlite3_column_bytes_t column_bytes;
    sqlite3_errmsg_t errmsg;
} sqlite_api;

static sqlite_api g_sqlite = {0};

typedef struct {
    ElfW(Addr) base;
    const ElfW(Sym) *symtab;
    const char *strtab;
    uint32_t sym_count;
} elf_symbols;

static elf_symbols g_sqlite_elf = {0};
static FILE *g_log_file = NULL;
static pthread_mutex_t g_log_mutex = PTHREAD_MUTEX_INITIALIZER;

static void phixit_log_write(int priority, const char *level, const char *format, ...) {
    char message[LOG_MESSAGE_SIZE];
    va_list args;

    va_start(args, format);
    vsnprintf(message, sizeof(message), format, args);
    va_end(args);

    __android_log_print(priority, LOG_TAG, "%s", message);

    pthread_mutex_lock(&g_log_mutex);
    if (g_log_file) {
        struct timespec ts;
        clock_gettime(CLOCK_REALTIME, &ts);
        struct tm utc_tm;
        gmtime_r(&ts.tv_sec, &utc_tm);
        char timebuf[24];
        strftime(timebuf, sizeof(timebuf), "%Y-%m-%d %H:%M:%S", &utc_tm);
        fprintf(g_log_file, "%s.%03ld %s [native] %s\n",
                timebuf, ts.tv_nsec / 1000000L, level, message);
        fflush(g_log_file);
    }
    pthread_mutex_unlock(&g_log_mutex);
}

typedef struct {
    unsigned char *data;
    size_t size;
    size_t cap;
} bytes;

typedef struct {
    char *name;
    int type;
    uint64_t value;
    unsigned char *blob;
    size_t blob_size;
} flag;

typedef struct {
    flag *items;
    size_t size;
    size_t cap;
} flag_list;

static void *elf_dynamic_ptr(ElfW(Addr) base, ElfW(Addr) ptr) {
    return (void *)(ptr >= base ? ptr : base + ptr);
}

static uint32_t gnu_hash_symbol_count(const uint32_t *gnu_hash) {
    uint32_t nbuckets = gnu_hash[0];
    uint32_t symoffset = gnu_hash[1];
    uint32_t bloom_size = gnu_hash[2];
    const uint32_t *buckets;
    const uint32_t *chains;
    uint32_t max_symbol = 0;

    buckets = gnu_hash + 4 + bloom_size * (sizeof(ElfW(Addr)) / sizeof(uint32_t));
    chains = buckets + nbuckets;

    for (uint32_t i = 0; i < nbuckets; i++) {
        if (buckets[i] > max_symbol) max_symbol = buckets[i];
    }
    if (max_symbol < symoffset) return symoffset;

    uint32_t limit = max_symbol + 65536;
    while ((chains[max_symbol - symoffset] & 1) == 0) {
        if (++max_symbol > limit) return max_symbol;
    }

    return max_symbol + 1;
}

static int find_sqlite_callback(struct dl_phdr_info *info, size_t size, void *data) {
    (void)size;
    elf_symbols *symbols = (elf_symbols *)data;
    const char *name = info->dlpi_name;
    const ElfW(Phdr) *dynamic_phdr = NULL;
    const ElfW(Dyn) *dynamic = NULL;
    const uint32_t *gnu_hash = NULL;

    if (!name) return 0;
    const char *base = strrchr(name, '/');
    base = base ? base + 1 : name;
    if (strcmp(base, "libsqlite.so") != 0) return 0;

    for (ElfW(Half) i = 0; i < info->dlpi_phnum; i++) {
        if (info->dlpi_phdr[i].p_type == PT_DYNAMIC) {
            dynamic_phdr = &info->dlpi_phdr[i];
            break;
        }
    }
    if (!dynamic_phdr) return 0;

    symbols->base = info->dlpi_addr;
    dynamic = (const ElfW(Dyn) *)(symbols->base + dynamic_phdr->p_vaddr);

    for (const ElfW(Dyn) *dyn = dynamic; dyn->d_tag != DT_NULL; dyn++) {
        switch (dyn->d_tag) {
            case DT_SYMTAB:
                symbols->symtab = (const ElfW(Sym) *)elf_dynamic_ptr(symbols->base, dyn->d_un.d_ptr);
                break;
            case DT_STRTAB:
                symbols->strtab = (const char *)elf_dynamic_ptr(symbols->base, dyn->d_un.d_ptr);
                break;
            case DT_HASH: {
                const uint32_t *hash = (const uint32_t *)elf_dynamic_ptr(symbols->base, dyn->d_un.d_ptr);
                symbols->sym_count = hash[1];
                break;
            }
            case DT_GNU_HASH:
                gnu_hash = (const uint32_t *)elf_dynamic_ptr(symbols->base, dyn->d_un.d_ptr);
                break;
        }
    }

    if (symbols->sym_count == 0 && gnu_hash) {
        symbols->sym_count = gnu_hash_symbol_count(gnu_hash);
    }

    if (!symbols->symtab || !symbols->strtab || symbols->sym_count == 0) {
        LOGE("Found libsqlite.so but failed to read dynamic symbols");
        memset(symbols, 0, sizeof(*symbols));
        return 0;
    }

    LOGI("Found loaded SQLite library: %s", name);
    return 1;
}

static void *load_sqlite_symbol(const char *name) {
    void *symbol = dlsym(RTLD_DEFAULT, name);
    if (!symbol && g_sqlite_elf.symtab && g_sqlite_elf.strtab) {
        for (uint32_t i = 0; i < g_sqlite_elf.sym_count; i++) {
            const ElfW(Sym) *sym = &g_sqlite_elf.symtab[i];
            if (sym->st_name == 0 || sym->st_value == 0 || sym->st_shndx == SHN_UNDEF) continue;
            if (strcmp(g_sqlite_elf.strtab + sym->st_name, name) == 0) {
                symbol = (void *)(g_sqlite_elf.base + sym->st_value);
                break;
            }
        }
    }
    if (!symbol) LOGE("dlsym %s failed: %s", name, dlerror());
    return symbol;
}

static int bytes_reserve(bytes *b, size_t need) {
    if (need <= b->cap) return 1;

    size_t cap = b->cap ? b->cap : 256;
    while (cap < need) cap *= 2;

    unsigned char *data = (unsigned char *)realloc(b->data, cap);
    if (!data) return 0;

    b->data = data;
    b->cap = cap;
    return 1;
}

static int bytes_write(bytes *b, const void *data, size_t len) {
    if (len == 0) return 1;
    if (!bytes_reserve(b, b->size + len)) return 0;

    memcpy(b->data + b->size, data, len);
    b->size += len;
    return 1;
}

static int bytes_write_byte(bytes *b, unsigned char value) {
    return bytes_write(b, &value, 1);
}

static void bytes_free(bytes *b) {
    free(b->data);
    b->data = NULL;
    b->size = 0;
    b->cap = 0;
}

static int inflate_raw(const unsigned char *input, size_t input_size, bytes *out) {
    z_stream zs;
    memset(&zs, 0, sizeof(zs));
    if (inflateInit2(&zs, -MAX_WBITS) != Z_OK) return 0;

    zs.next_in = (Bytef *)input;
    zs.avail_in = (uInt)input_size;
    unsigned char buffer[1024];
    int rc;
    do {
        zs.next_out = buffer;
        zs.avail_out = sizeof(buffer);
        rc = inflate(&zs, Z_NO_FLUSH);
        if (rc != Z_OK && rc != Z_STREAM_END) {
            inflateEnd(&zs);
            return 0;
        }
        if (!bytes_write(out, buffer, sizeof(buffer) - zs.avail_out)) {
            inflateEnd(&zs);
            return 0;
        }
    } while (rc != Z_STREAM_END);

    inflateEnd(&zs);
    return 1;
}

static int deflate_raw(const unsigned char *input, size_t input_size, bytes *out) {
    z_stream zs;
    memset(&zs, 0, sizeof(zs));
    if (deflateInit2(&zs, 1, Z_DEFLATED, -MAX_WBITS, 8, Z_DEFAULT_STRATEGY) != Z_OK) return 0;

    zs.next_in = (Bytef *)input;
    zs.avail_in = (uInt)input_size;
    unsigned char buffer[1024];
    int rc;
    do {
        zs.next_out = buffer;
        zs.avail_out = sizeof(buffer);
        rc = deflate(&zs, Z_FINISH);
        if (rc != Z_OK && rc != Z_STREAM_END) {
            deflateEnd(&zs);
            return 0;
        }
        if (!bytes_write(out, buffer, sizeof(buffer) - zs.avail_out)) {
            deflateEnd(&zs);
            return 0;
        }
    } while (rc != Z_STREAM_END);

    deflateEnd(&zs);
    return 1;
}

static int read_varint(const unsigned char *data, size_t size, size_t *pos, uint64_t *value) {
    uint64_t result = 0;
    for (int shift = 0; shift < 64; shift += 7) {
        if (*pos >= size) return 0;
        unsigned char byte = data[(*pos)++];
        result |= ((uint64_t)(byte & 0x7f)) << shift;
        if ((byte & 0x80) == 0) {
            *value = result;
            return 1;
        }
    }
    return 0;
}

static int write_varint(bytes *out, uint64_t value) {
    while (value > 0x7f) {
        if (!bytes_write_byte(out, (unsigned char)((value & 0x7f) | 0x80))) return 0;
        value >>= 7;
    }
    return bytes_write_byte(out, (unsigned char)value);
}

static char *copy_string(const unsigned char *data, size_t len) {
    char *out = (char *)malloc(len + 1);
    if (!out) return NULL;
    memcpy(out, data, len);
    out[len] = 0;
    return out;
}

static int can_read(size_t size, size_t pos, uint64_t len) {
    return pos <= size && len <= size - pos;
}

static int flags_reserve(flag_list *list, size_t need) {
    if (need <= list->cap) return 1;

    size_t cap = list->cap ? list->cap : 32;
    while (cap < need) cap *= 2;

    flag *items = (flag *)realloc(list->items, cap * sizeof(flag));
    if (!items) return 0;

    list->items = items;
    list->cap = cap;
    return 1;
}

static int flags_add(flag_list *list, flag item) {
    if (!flags_reserve(list, list->size + 1)) return 0;
    list->items[list->size++] = item;
    return 1;
}

static void flag_free(flag *item) {
    free(item->name);
    free(item->blob);
    memset(item, 0, sizeof(flag));
}

static void flags_free(flag_list *list) {
    for (size_t i = 0; i < list->size; i++) flag_free(&list->items[i]);
    free(list->items);
    list->items = NULL;
    list->size = 0;
    list->cap = 0;
}

static int decode_flags(const unsigned char *compressed, size_t compressed_size, flag_list *flags) {
    bytes plain = {0};
    int ok = 0;

    if (!inflate_raw(compressed, compressed_size, &plain)) goto out;

    size_t pos = 0;
    uint64_t count = 0;
    uint64_t next = 0;
    if (!read_varint(plain.data, plain.size, &pos, &count)) goto out;

    for (uint64_t i = 0; i < count; i++) {
        uint64_t theory = 0;
        if (!read_varint(plain.data, plain.size, &pos, &theory)) goto out;

        uint64_t shift = theory >> 3;
        int type = (int)(theory & 7);
        char name_buf[32];
        char *name = NULL;
        if (shift == 0) {
            uint64_t len = 0;
            if (!read_varint(plain.data, plain.size, &pos, &len) ||
                    !can_read(plain.size, pos, len)) {
                goto out;
            }
            name = copy_string(plain.data + pos, (size_t)len);
            pos += (size_t)len;
        } else {
            next += shift;
            snprintf(name_buf, sizeof(name_buf), "%llu", (unsigned long long)next);
            name = copy_string((const unsigned char *)name_buf, strlen(name_buf));
        }
        if (!name) goto out;

        flag f;
        memset(&f, 0, sizeof(f));
        f.name = name;
        f.type = type;

        if (type == PHIXIT_FLAG_FALSE || type == PHIXIT_FLAG_TRUE) {
            f.value = (uint64_t)type;
        } else if (type == PHIXIT_FLAG_INT) {
            if (!read_varint(plain.data, plain.size, &pos, &f.value)) {
                flag_free(&f);
                goto out;
            }
        } else if (type == PHIXIT_FLAG_FLOAT) {
            if (!can_read(plain.size, pos, 8)) {
                flag_free(&f);
                goto out;
            }
            memcpy(&f.value, plain.data + pos, 8);
            pos += 8;
        } else if (type == PHIXIT_FLAG_STRING || type == PHIXIT_FLAG_EXTENSION) {
            uint64_t len = 0;
            if (!read_varint(plain.data, plain.size, &pos, &len) ||
                    !can_read(plain.size, pos, len)) {
                flag_free(&f);
                goto out;
            }
            size_t allocation_size = (size_t)len + (type == PHIXIT_FLAG_STRING || len == 0 ? 1 : 0);
            f.blob = (unsigned char *)malloc(allocation_size);
            if (!f.blob) {
                flag_free(&f);
                goto out;
            }
            memcpy(f.blob, plain.data + pos, (size_t)len);
            if (type == PHIXIT_FLAG_STRING) f.blob[len] = 0;
            f.blob_size = (size_t)len;
            pos += (size_t)len;
        } else {
            flag_free(&f);
            goto out;
        }

        if (!flags_add(flags, f)) {
            flag_free(&f);
            goto out;
        }
    }

    ok = 1;

out:
    bytes_free(&plain);
    if (!ok) flags_free(flags);
    return ok;
}

static int name_to_u64(const char *name, uint64_t *value) {
    char *end = NULL;
    uint64_t parsed = strtoull(name, &end, 10);
    if (!name[0] || !end || *end != 0) return 0;
    *value = parsed;
    return 1;
}

static int flag_compare(const void *a, const void *b) {
    const flag *fa = (const flag *)a;
    const flag *fb = (const flag *)b;
    uint64_t na, nb;
    int ia = name_to_u64(fa->name, &na);
    int ib = name_to_u64(fb->name, &nb);
    if (ia && ib) return (na > nb) - (na < nb);
    if (ia != ib) return ia ? -1 : 1;
    return strcmp(fa->name, fb->name);
}

static int encode_flags(flag_list *flags, bytes *compressed) {
    qsort(flags->items, flags->size, sizeof(flag), flag_compare);

    bytes plain = {0};
    uint64_t next = 0;
    if (!write_varint(&plain, flags->size)) goto fail;

    for (size_t i = 0; i < flags->size; i++) {
        flag *f = &flags->items[i];
        uint64_t name_value = 0;
        if (name_to_u64(f->name, &name_value) && name_value > next) {
            if (!write_varint(&plain, ((name_value - next) << 3) | (uint64_t)f->type)) goto fail;
            next = name_value;
        } else {
            size_t name_len = strlen(f->name);
            if (!write_varint(&plain, (uint64_t)f->type) ||
                    !write_varint(&plain, name_len) ||
                    !bytes_write(&plain, f->name, name_len)) goto fail;
        }

        if (f->type == PHIXIT_FLAG_INT) {
            if (!write_varint(&plain, f->value)) goto fail;
        } else if (f->type == PHIXIT_FLAG_FLOAT) {
            if (!bytes_write(&plain, &f->value, 8)) goto fail;
        } else if (f->type == PHIXIT_FLAG_STRING || f->type == PHIXIT_FLAG_EXTENSION) {
            if (!write_varint(&plain, f->blob_size) || !bytes_write(&plain, f->blob, f->blob_size)) goto fail;
        }
    }

    int ok = deflate_raw(plain.data, plain.size, compressed);
    bytes_free(&plain);
    return ok;

fail:
    bytes_free(&plain);
    return 0;
}

static flag clone_flag(const flag *source) {
    flag copy;
    memset(&copy, 0, sizeof(copy));
    copy.name = copy_string((const unsigned char *)source->name, strlen(source->name));
    copy.type = source->type;
    copy.value = source->value;
    copy.blob_size = source->blob_size;
    if (source->blob) {
        size_t allocation_size = source->blob_size +
                (source->type == PHIXIT_FLAG_STRING || source->blob_size == 0 ? 1 : 0);
        copy.blob = (unsigned char *)malloc(allocation_size);
        if (copy.blob) {
            memcpy(copy.blob, source->blob, source->blob_size);
            if (source->type == PHIXIT_FLAG_STRING) copy.blob[source->blob_size] = 0;
        }
    }
    return copy;
}

static int find_flag(flag_list *list, const char *name) {
    for (size_t i = 0; i < list->size; i++) {
        if (strcmp(list->items[i].name, name) == 0) return (int)i;
    }
    return -1;
}

#define COL_OVR_NAME      0
#define COL_OVR_FLAG_TYPE 1
#define COL_OVR_INT_VAL   2
#define COL_OVR_BOOL_VAL  3
#define COL_OVR_FLOAT_VAL 4
#define COL_OVR_STR_VAL   5
#define COL_OVR_EXT_VAL   6

static const char READ_OVERRIDES_SQL[] =
        "SELECT o.name, o.flagType, o.intVal, o.boolVal, o.floatVal, o.stringVal, o.extensionVal "
        "FROM GmsFlagsOverrides o "
        "JOIN static_config_packages p ON p.name = o.packageName "
        "WHERE p.static_config_package_id = ?;";

static int read_overrides(sqlite3 *db, int package_id, flag_list *overrides) {
    sqlite3_stmt *stmt = NULL;
    if (g_sqlite.prepare_v2(db, READ_OVERRIDES_SQL, -1, &stmt, NULL) != 0) return 0;
    g_sqlite.bind_int(stmt, 1, package_id);

    while (g_sqlite.step(stmt) == SQLITE_ROW) {
        const unsigned char *name_text = g_sqlite.column_text(stmt, COL_OVR_NAME);
        if (!name_text) continue;

        flag f;
        memset(&f, 0, sizeof(f));
        f.name = copy_string(name_text, strlen((const char *)name_text));

        int flag_type = g_sqlite.column_int(stmt, COL_OVR_FLAG_TYPE);
        if (flag_type == 0) {
            const unsigned char *bool_text = g_sqlite.column_text(stmt, COL_OVR_BOOL_VAL);
            int bool_value = bool_text &&
                    (strcmp((const char *)bool_text, "1") == 0 ||
                     strcmp((const char *)bool_text, "true") == 0);
            f.type = bool_value ? PHIXIT_FLAG_TRUE : PHIXIT_FLAG_FALSE;
            f.value = (uint64_t)f.type;
        } else if (flag_type == 1 || flag_type == 2) {
            const unsigned char *value_text = g_sqlite.column_text(
                    stmt, flag_type == 1 ? COL_OVR_INT_VAL : COL_OVR_FLOAT_VAL);
            if (!value_text) { flag_free(&f); continue; }
            if (flag_type == 1) {
                f.type = PHIXIT_FLAG_INT;
                f.value = strtoull((const char *)value_text, NULL, 10);
            } else {
                double parsed = strtod((const char *)value_text, NULL);
                f.type = PHIXIT_FLAG_FLOAT;
                memcpy(&f.value, &parsed, sizeof(parsed));
            }
        } else if (flag_type == 3) {
            const unsigned char *value_text = g_sqlite.column_text(stmt, COL_OVR_STR_VAL);
            if (!value_text) { flag_free(&f); continue; }
            f.type = PHIXIT_FLAG_STRING;
            f.blob_size = strlen((const char *)value_text);
            f.blob = (unsigned char *)copy_string(value_text, f.blob_size);
        } else if (flag_type == 4) {
            const void *blob = g_sqlite.column_blob(stmt, COL_OVR_EXT_VAL);
            int blob_size = g_sqlite.column_bytes(stmt, COL_OVR_EXT_VAL);
            if (!blob || blob_size <= 0) { flag_free(&f); continue; }
            f.type = PHIXIT_FLAG_EXTENSION;
            f.blob_size = (size_t)blob_size;
            f.blob = (unsigned char *)malloc(f.blob_size);
            if (f.blob) memcpy(f.blob, blob, f.blob_size);
        } else {
            flag_free(&f);
            continue;
        }

        if (!f.name ||
                ((f.type == PHIXIT_FLAG_STRING || f.type == PHIXIT_FLAG_EXTENSION) && !f.blob) ||
                !flags_add(overrides, f)) {
            flag_free(&f);
        }
    }

    g_sqlite.finalize(stmt);
    return 1;
}

static int exec_sql(sqlite3 *db, const char *label, const char *sql) {
    int rc = g_sqlite.exec(db, sql, NULL, NULL, NULL);
    if (rc != 0) {
        LOGE("%s failed: %d, %s", label, rc, g_sqlite.errmsg(db));
        return 0;
    }
    return 1;
}

static const char CHECK_PARAM_PARTITIONS_SQL[] =
        "SELECT 1 FROM main.sqlite_master WHERE type='table' AND name='param_partitions' LIMIT 1";

static int table_exists(sqlite3 *db) {
    sqlite3_stmt *stmt = NULL;
    int rc = g_sqlite.prepare_v2(db, CHECK_PARAM_PARTITIONS_SQL, -1, &stmt, NULL);
    if (rc != 0) {
        LOGE("Table check failed: %d, %s", rc, g_sqlite.errmsg(db));
        return 0;
    }
    int exists = g_sqlite.step(stmt) == SQLITE_ROW;
    g_sqlite.finalize(stmt);
    return exists;
}

static int register_triggers(sqlite3 *db) {
    if (!table_exists(db)) {
        LOGI("Skipping Phixit trigger registration: param_partitions is not visible");
        return 0;
    }

    return exec_sql(db, "Disable recursive triggers", DISABLE_RECURSIVE_TRIGGERS_SQL) &&
            exec_sql(db, "Insert trigger registration", CREATE_INSERT_TRIGGER_SQL) &&
            exec_sql(db, "Update trigger registration", CREATE_UPDATE_TRIGGER_SQL);
}

static void format_flag_value(const flag *f, char *buf, size_t buf_size) {
    switch (f->type) {
        case PHIXIT_FLAG_FALSE:   snprintf(buf, buf_size, "false"); break;
        case PHIXIT_FLAG_TRUE:    snprintf(buf, buf_size, "true");  break;
        case PHIXIT_FLAG_INT:     snprintf(buf, buf_size, "%llu", (unsigned long long)f->value); break;
        case PHIXIT_FLAG_FLOAT: {
            double d;
            memcpy(&d, &f->value, sizeof(d));
            snprintf(buf, buf_size, "%g", d);
            break;
        }
        case PHIXIT_FLAG_STRING:
            snprintf(buf, buf_size, "\"%.*s\"", (int)f->blob_size, (const char *)f->blob);
            break;
        case PHIXIT_FLAG_EXTENSION:
            snprintf(buf, buf_size, "<ext %zu bytes>", f->blob_size);
            break;
        default:
            snprintf(buf, buf_size, "<?>");
            break;
    }
}

static void phixit_merge_flags(sqlite3_context *sqlite_ctx, int argc, sqlite3_value **argv) {
    if (argc != 2) {
        g_sqlite.result_error(sqlite_ctx, "Invalid argument count", -1);
        return;
    }

    int package_id = g_sqlite.value_int(argv[0]);
    const void *content = g_sqlite.value_blob(argv[1]);
    int content_size = g_sqlite.value_bytes(argv[1]);
    LOGD("Phixit trigger merge called: packageId=%d, contentBytes=%d", package_id, content_size);
    if (!content || content_size <= 0) {
        LOGD("Phixit trigger merge skipped: empty flags_content for packageId=%d", package_id);
        g_sqlite.result_value(sqlite_ctx, argv[1]);
        return;
    }

    sqlite3 *db = g_sqlite.context_db_handle(sqlite_ctx);
    flag_list overrides = {0};
    if (!read_overrides(db, package_id, &overrides) || overrides.size == 0) {
        LOGD("Phixit trigger merge skipped: no overrides for packageId=%d", package_id);
        flags_free(&overrides);
        g_sqlite.result_value(sqlite_ctx, argv[1]);
        return;
    }
    LOGD("Phixit trigger merge applying %zu overrides for packageId=%d", overrides.size, package_id);

    flag_list flags = {0};
    bytes output = {0};
    if (!decode_flags((const unsigned char *)content, (size_t)content_size, &flags)) {
        LOGE("Failed to decode flags_content for packageId=%d", package_id);
        flags_free(&overrides);
        g_sqlite.result_value(sqlite_ctx, argv[1]);
        return;
    }
    LOGD("Phixit trigger merge: decoded %zu flags, applying %zu overrides for packageId=%d",
            flags.size, overrides.size, package_id);

    for (size_t i = 0; i < overrides.size; i++) {
        flag *ovr = &overrides.items[i];
        int index = find_flag(&flags, ovr->name);
        flag copy = clone_flag(ovr);
        if (!copy.name ||
                ((copy.type == PHIXIT_FLAG_STRING || copy.type == PHIXIT_FLAG_EXTENSION) && !copy.blob)) {
            LOGW("Override '%s': clone failed (OOM)", ovr->name);
            flag_free(&copy);
            continue;
        }

        if (index >= 0) {
            char prev_buf[256], new_buf[256];
            format_flag_value(&flags.items[index], prev_buf, sizeof(prev_buf));
            format_flag_value(&copy, new_buf, sizeof(new_buf));
            LOGD("  Override '%s': %s -> %s", ovr->name, prev_buf, new_buf);
            flag_free(&flags.items[index]);
            flags.items[index] = copy;
        } else {
            if (flags_add(&flags, copy)) {
                LOGD("  Override '%s' (type=%d): added new", ovr->name, ovr->type);
            } else {
                LOGW("  Override '%s': failed to add (OOM)", ovr->name);
                flag_free(&copy);
            }
        }
    }

    if (encode_flags(&flags, &output)) {
        g_sqlite.result_blob(sqlite_ctx, output.data, (int)output.size, SQLITE_TRANSIENT);
        LOGD("Phixit trigger merge completed: packageId=%d, %d -> %zu bytes",
                package_id, content_size, output.size);
    } else {
        LOGE("Failed to encode merged flags_content for packageId=%d", package_id);
        g_sqlite.result_value(sqlite_ctx, argv[1]);
    }

    bytes_free(&output);
    flags_free(&flags);
    flags_free(&overrides);
}

static int sqlite_symbols_ready(void);

static void load_sqlite_symbols(void) {
    if (sqlite_symbols_ready()) return;

    g_sqlite.db_filename = (sqlite3_db_filename_t)load_sqlite_symbol("sqlite3_db_filename");
    g_sqlite.db_config = (sqlite3_db_config_t)load_sqlite_symbol("sqlite3_db_config");
    g_sqlite.create_function_v2 = (sqlite3_create_function_v2_t)load_sqlite_symbol("sqlite3_create_function_v2");
    g_sqlite.exec = (sqlite3_exec_t)load_sqlite_symbol("sqlite3_exec");
    g_sqlite.context_db_handle = (sqlite3_context_db_handle_t)load_sqlite_symbol("sqlite3_context_db_handle");
    g_sqlite.value_int = (sqlite3_value_int_t)load_sqlite_symbol("sqlite3_value_int");
    g_sqlite.value_blob = (sqlite3_value_blob_t)load_sqlite_symbol("sqlite3_value_blob");
    g_sqlite.value_bytes = (sqlite3_value_bytes_t)load_sqlite_symbol("sqlite3_value_bytes");
    g_sqlite.result_blob = (sqlite3_result_blob_t)load_sqlite_symbol("sqlite3_result_blob");
    g_sqlite.result_value = (sqlite3_result_value_t)load_sqlite_symbol("sqlite3_result_value");
    g_sqlite.result_error = (sqlite3_result_error_t)load_sqlite_symbol("sqlite3_result_error");
    g_sqlite.prepare_v2 = (sqlite3_prepare_v2_t)load_sqlite_symbol("sqlite3_prepare_v2");
    g_sqlite.bind_int = (sqlite3_bind_int_t)load_sqlite_symbol("sqlite3_bind_int");
    g_sqlite.step = (sqlite3_step_t)load_sqlite_symbol("sqlite3_step");
    g_sqlite.finalize = (sqlite3_finalize_t)load_sqlite_symbol("sqlite3_finalize");
    g_sqlite.column_int = (sqlite3_column_int_t)load_sqlite_symbol("sqlite3_column_int");
    g_sqlite.column_text = (sqlite3_column_text_t)load_sqlite_symbol("sqlite3_column_text");
    g_sqlite.column_blob = (sqlite3_column_blob_t)load_sqlite_symbol("sqlite3_column_blob");
    g_sqlite.column_bytes = (sqlite3_column_bytes_t)load_sqlite_symbol("sqlite3_column_bytes");
    g_sqlite.errmsg = (sqlite3_errmsg_t)load_sqlite_symbol("sqlite3_errmsg");
}

static int sqlite_symbols_ready(void) {
    return g_sqlite.db_filename &&
            g_sqlite.db_config &&
            g_sqlite.create_function_v2 &&
            g_sqlite.exec &&
            g_sqlite.context_db_handle &&
            g_sqlite.value_int &&
            g_sqlite.value_blob &&
            g_sqlite.value_bytes &&
            g_sqlite.result_blob &&
            g_sqlite.result_value &&
            g_sqlite.result_error &&
            g_sqlite.prepare_v2 &&
            g_sqlite.bind_int &&
            g_sqlite.step &&
            g_sqlite.finalize &&
            g_sqlite.column_int &&
            g_sqlite.column_text &&
            g_sqlite.column_blob &&
            g_sqlite.column_bytes &&
            g_sqlite.errmsg;
}

static void configure_sqlite_for_triggers(sqlite3 *db) {
    int rc = g_sqlite.db_config(db, SQLITE_DBCONFIG_DEFENSIVE, 0, NULL);
    if (rc != 0) LOGE("Failed to disable SQLite defensive mode: %d", rc);

    rc = g_sqlite.db_config(db, SQLITE_DBCONFIG_ENABLE_TRIGGER, 1, NULL);
    if (rc != 0) LOGE("Failed to enable SQLite triggers: %d", rc);
}

JNIEXPORT void JNICALL
Java_ua_polodarb_gmsflags_xposed_PhixitHook_nativeSetDebugLogPath(
        JNIEnv *env,
        jobject thiz,
        jstring path
) {
    pthread_mutex_lock(&g_log_mutex);
    if (g_log_file) {
        fclose(g_log_file);
        g_log_file = NULL;
    }
    if (path) {
        const char *chars = (*env)->GetStringUTFChars(env, path, NULL);
        if (chars) {
            g_log_file = fopen(chars, "a");
            (*env)->ReleaseStringUTFChars(env, path, chars);
        }
    }
    pthread_mutex_unlock(&g_log_mutex);

    if (g_log_file) {
        LOGI("Native debug file logging enabled");
    }
}


JNIEXPORT void JNICALL
Java_ua_polodarb_gmsflags_xposed_PhixitHook_nativeHandlePhenotype(
        JNIEnv *env,
        jobject thiz,
        jlong connection_ptr
) {
    if (!g_sqlite_elf.symtab) {
        dl_iterate_phdr(find_sqlite_callback, &g_sqlite_elf);
    }

    load_sqlite_symbols();

    if (!sqlite_symbols_ready()) {
        LOGE("SQLite symbols are not fully loaded");
        return;
    }

    sqlite3 *db = *(sqlite3 **)(intptr_t)connection_ptr;
    if (!db) {
        LOGE("DB is null");
        return;
    }

    const char *filename = g_sqlite.db_filename(db, "main");
    if (filename) LOGI("SQLite DB file: %s", filename);

    sqlite3_stmt *ver_stmt = NULL;
    if (g_sqlite.prepare_v2(db, "PRAGMA user_version", -1, &ver_stmt, NULL) != SQLITE_OK) {
        LOGE("Failed to query user_version");
        return;
    }
    int version = 0;
    if (g_sqlite.step(ver_stmt) == SQLITE_ROW) {
        version = g_sqlite.column_int(ver_stmt, 0);
    }
    g_sqlite.finalize(ver_stmt);

    if (version < MINIMAL_PHENOTYPE_VERSION) {
        LOGW("Phenotype version %d is too low for Phixit (need >= %d)", version, MINIMAL_PHENOTYPE_VERSION);
        return;
    }

    configure_sqlite_for_triggers(db);

    if (!exec_sql(db, "Overrides table creation", CREATE_OVERRIDES_TABLE_SQL)) {
        return;
    }

    int rc = g_sqlite.create_function_v2(
            db,
            MERGE_FUNCTION_NAME,
            2,
            SQLITE_UTF8 | SQLITE_DETERMINISTIC,
            NULL,
            phixit_merge_flags,
            NULL,
            NULL,
            NULL
    );
    if (rc != 0) {
        LOGE("sqlite3_create_function_v2 failed: %d, %s", rc, g_sqlite.errmsg(db));
        return;
    }

    if (!register_triggers(db)) {
        return;
    }

    LOGI("Phixit merge function and triggers registered");

#ifndef NDEBUG
    {
        static int self_test_done = 0;
        if (!self_test_done) {
            self_test_done = 1;
            static const char SELF_UPDATE_SQL[] =
                    "UPDATE param_partitions SET flags_content = flags_content WHERE flags_content IS NOT NULL";
            LOGD("Running trigger self-test");
            exec_sql(db, "Trigger self-test", SELF_UPDATE_SQL);
        }
    }
#endif
}
