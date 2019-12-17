extern crate jni;

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_com_ragingo_jni_NativeFunctions_ret121() -> i32 {
    return 11 * 11
}
