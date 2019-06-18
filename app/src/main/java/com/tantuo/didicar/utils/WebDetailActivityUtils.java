package com.tantuo.didicar.utils;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;

import com.tantuo.didicar.Activity.DiDi_info_Activity;


/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：WebDetailActivityUtils  webDetailsActivity跳转类
 */
public class WebDetailActivityUtils {


    public static void start_DiDi_info_Activity(Context context, String url) {
        Intent intent = new Intent(context, DiDi_info_Activity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


}