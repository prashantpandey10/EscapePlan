LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := andengine_shared
LOCAL_SRC_FILES := \
	/home/prashant/Downloads/EscapePlan-master/andEngine/src/main/jni/build.sh \
	/home/prashant/Downloads/EscapePlan-master/andEngine/src/main/jni/Android.mk \
	/home/prashant/Downloads/EscapePlan-master/andEngine/src/main/jni/Application.mk \
	/home/prashant/Downloads/EscapePlan-master/andEngine/src/main/jni/src/BufferUtils.cpp \
	/home/prashant/Downloads/EscapePlan-master/andEngine/src/main/jni/src/GLES20Fix.c \

LOCAL_C_INCLUDES += /home/prashant/Downloads/EscapePlan-master/andEngine/src/main/jni
LOCAL_C_INCLUDES += /home/prashant/Downloads/EscapePlan-master/andEngine/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
