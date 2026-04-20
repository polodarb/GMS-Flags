LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := phixit_hook
LOCAL_SRC_FILES := phixit_hook.c
LOCAL_LDLIBS := -llog -lz
LOCAL_LDFLAGS := -Wl,-z,max-page-size=16384
include $(BUILD_SHARED_LIBRARY)
