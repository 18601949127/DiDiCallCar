package com.tantuo.didicar.utils;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

import java.util.Locale;

public class DiDiCarApplication extends Application {
    /**
     所有组件被创建之前执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);
        //初始化Volley
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);

        Locale _UserLocale=LocaleUtils.getUserLocale(this);
        LocaleUtils.updateLocale(this, _UserLocale);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale _UserLocale=LocaleUtils.getUserLocale(this);
        //系统语言改变了应用保持之前设置的语言
        if (_UserLocale != null) {
            Locale.setDefault(_UserLocale);
            Configuration _Configuration = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(_UserLocale);
            } else {
                _Configuration.locale =_UserLocale;
            }
            getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics());
        }
    }



}

