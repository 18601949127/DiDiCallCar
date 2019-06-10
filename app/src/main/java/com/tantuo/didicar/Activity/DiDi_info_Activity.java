package com.tantuo.didicar.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tantuo.didicar.R;

public class DiDi_info_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.didi_info_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        WebView webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebSettings settings = webView.getSettings();
        settings.setDisplayZoomControls(true); //显示webview缩放按钮

        //缩放开关，设置此属性，仅支持双击缩放，不支持触摸缩放
        settings.setSupportZoom(true);
        //设置是否可缩放，会出现缩放工具（若为true则上面的设值也默认为true）
        settings.setBuiltInZoomControls(true);
        //设置URL网页自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);//允许运行Javascrip
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.loadUrl(url);
    }
}
