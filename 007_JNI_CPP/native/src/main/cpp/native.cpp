#include <iostream>
#include <jni.h>

#define PACKAGE_NAME Java_com_ragingo_jni_

#define RAGINGO_EXPORT extern "C" JNIEXPORT
#define RAGINGO_JNI_FUNC( ret_type, package_name, func_name, ... ) ret_type JNICALL package_name##func_name(__VA_ARGS__)
#define RAGINGO_FUNC(ret_type, package_name, func_name, ...) RAGINGO_JNI_FUNC(ret_type, package_name, func_name, __VA_ARGS__)

RAGINGO_EXPORT RAGINGO_FUNC(jint, PACKAGE_NAME, NativeFunctions_ret100) {
    jint x = 10;
    return x * x;
}

template <typename T> constexpr T PI = 3.1415926535;

RAGINGO_EXPORT RAGINGO_FUNC(jdouble, PACKAGE_NAME, NativeFunctions_dpi) {
    return PI<jdouble>;
}

RAGINGO_EXPORT RAGINGO_FUNC(jfloat, PACKAGE_NAME, NativeFunctions_fpi) {
    return PI<jfloat>;
}

RAGINGO_EXPORT RAGINGO_FUNC(jlong, PACKAGE_NAME, NativeFunctions_lpi) {
    return PI<jlong>;
}

RAGINGO_EXPORT RAGINGO_FUNC(jint, PACKAGE_NAME, NativeFunctions_ipi) {
    return PI<jint>;
}
