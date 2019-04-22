package com.tantuo.didicar.utils;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

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

    }


}

