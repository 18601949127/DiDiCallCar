package com.tantuo.didicar.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者：尚硅谷-杨光福 on 2016/8/13 11:35
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：缓存软件的一些参数和数据
 */
public class CacheUtils {
    /**
     * 得到缓存值
     *
     * @param context 上下文
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("didicar", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 保存软件参数
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("didicar", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 缓存文本数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
            SharedPreferences sp = context.getSharedPreferences("didicar", Context.MODE_PRIVATE);
            sp.edit().putString(key, value).commit();
    }

    /**
     * 获取缓存的文本信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("didicar", Context.MODE_PRIVATE);
        String result = sp.getString(key, "");
        return result;
    }
}
