#include <iostream>
#include <cmath>
#include <jni.h>

#define RAGINGO_EXPORT extern "C" JNIEXPORT

#define PACKAGE_NAME Java_com_ragingo_jni
#define MAKE_FUNC_NAME_(package_name, class_name, func_name) package_name##_##class_name##_##func_name
#define MAKE_FUNC_NAME(package_name, class_name, func_name) MAKE_FUNC_NAME_(package_name, class_name, func_name)

#define NativeFunctions(func_name) \
    JNICALL MAKE_FUNC_NAME(PACKAGE_NAME, NativeFunctions, func_name)

RAGINGO_EXPORT jint NativeFunctions(ret100)() {
    jint x = 10;
    return x * x;
}

RAGINGO_EXPORT jint NativeFunctions(pow)(JNIEnv*, jobject, jint x, jint y) {
    return std::pow<jint>(x, y);
}

template <typename T> constexpr T PI = 3.1415926535;

RAGINGO_EXPORT jdouble NativeFunctions(dpi)() {
    return PI<jdouble>;
}

RAGINGO_EXPORT jfloat NativeFunctions(fpi)() {
    return PI<jfloat>;
}

RAGINGO_EXPORT jlong NativeFunctions(lpi)() {
    return PI<jlong>;
}

RAGINGO_EXPORT jint NativeFunctions(ipi)() {
    return PI<jint>;
}
