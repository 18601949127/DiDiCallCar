package com.tantuo.didicar.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.tantuo.didicar.R;
import com.tantuo.didicar.utils.TitleBarUtils;

public class DiDi_info_Activity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {


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

        initTitleBar();
    }

    private void initTitleBar() {
        ImageButton popUpMenu = (ImageButton) findViewById(R.id.btnPopUpMenu);
        popUpMenu.setOnClickListener(this);


        ImageView ib_titlebar_back = (ImageView) findViewById(R.id.ib_titlebar_back);
        ib_titlebar_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnPopUpMenu: // 说明点击了微信按钮
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.popup_menu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(this);
                //显示(这一行代码不要忘记了)
                popup.show();
                break;


            case R.id.ib_titlebar_back:
                finish();
                break;


            default:
                break;
        }


    }

    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.item1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("更多");
                builder.setMessage("这里可以输入更多信息\n\n有疑问联系作者微信18601949127");
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;


            default:
                break;
        }
        return false;
    }
}
