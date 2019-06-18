package com.tantuo.didicar.utils;


/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：JniUtils
 */
public class JniUtils {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    public static native String getMd5(String origin);

}
