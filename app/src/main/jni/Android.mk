LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := phixit_hook
LOCAL_SRC_FILES := phixit_hook.c
LOCAL_LDLIBS := -llog -lz
include $(BUILD_SHARED_LIBRARY)
