package com.tantuo.didicar.utils;


/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：JniUtils
 */
public class JniUtils {
    static {
        System.loadLibrary("jnitest");
    }

    public static native String getString();
}
