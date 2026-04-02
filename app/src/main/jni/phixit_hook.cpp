#include <jni.h>
#include <android/log.h>
#include <dlfcn.h>

#define LOG_TAG "PhixitHook"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

constexpr const char* SQLITE_LIB_PATH = "/data/user_de/0/com.google.android.gms/phixit_libs/libsqlite3x.so";

typedef struct sqlite3 sqlite3;

typedef const char* (*sqlite3_db_filename_t)(sqlite3*, const char*);
typedef int (*sqlite3_create_function_v2_t)(
        sqlite3 *db,
        const char *zFunctionName,
        int nArg,
        int eTextRep,
        void *pApp,
        void (*xFunc)(void*, int, void**),
        void (*xStep)(void*, int, void**),
        void (*xFinal)(void*),
        void(*xDestroy)(void*)
);

static void* gSqliteHandle = nullptr;
static sqlite3_db_filename_t gSqliteDbFilename = nullptr;
static sqlite3_create_function_v2_t gSqliteCreateFunctionV2 = nullptr;

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    gSqliteHandle = dlopen(SQLITE_LIB_PATH, RTLD_NOW);
    if (!gSqliteHandle) {
        LOGE("dlopen failed: %s", dlerror());
        return JNI_VERSION_1_6;
    }

    gSqliteDbFilename = (sqlite3_db_filename_t)dlsym(gSqliteHandle, "sqlite3_db_filename");
    if (!gSqliteDbFilename) LOGE("dlsym sqlite3_db_filename failed: %s", dlerror());
    else LOGI("sqlite3_db_filename loaded");

    gSqliteCreateFunctionV2 = (sqlite3_create_function_v2_t)dlsym(gSqliteHandle, "sqlite3_create_function_v2");
    if (!gSqliteCreateFunctionV2) LOGE("dlsym sqlite3_create_function_v2 failed: %s", dlerror());
    else LOGI("sqlite3_create_function_v2 loaded");

    return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT void JNICALL
Java_ua_polodarb_gmsflags_xposed_PhixitHook_nativeHandlePhenotype(JNIEnv *env, jobject thiz,
                                                                  jlong connection_ptr,
                                                                  jobject handler) {
    sqlite3* db = *reinterpret_cast<sqlite3**>(connection_ptr);

    if (!db) {
        LOGE("db is null");
        return;
    }
    const char* filename = gSqliteDbFilename(db, "main");

    if (filename) {
        LOGI("SQLite DB file: %s", filename);
    } else {
        LOGE("sqlite3_db_filename returned null");
    }
}