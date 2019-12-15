#include <iostream>
#include <jni.h>

#define PACKAGE_NAME Java_com_ragingo_jni_

#define RAGINGO_JNI_FUNC( ret_type, package_name, func_name, ... ) ret_type JNICALL package_name##func_name(__VA_ARGS__)
#define RAGINGO_FUNC(ret_type, package_name, func_name, ...) RAGINGO_JNI_FUNC(ret_type, package_name, func_name, __VA_ARGS__)


extern "C" JNIEXPORT RAGINGO_FUNC(jint, PACKAGE_NAME, NativeFunctions_ret100) {
    jint x = 10;
    return x * x;
}
