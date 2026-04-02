LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := phixit_hook
LOCAL_SRC_FILES := phixit_hook.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)
