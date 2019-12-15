# https://developer.android.com/studio/projects/configure-cmake.html?hl=ja

cmake_minimum_required(VERSION 3.10.2)

add_library(native SHARED src/main/cpp/native.cpp)


#include_directories(src/main/cpp/include/)

target_link_libraries(
    native
    android
    log
)
