package com.tantuo.didicar.menudatailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tantuo.didicar.base.MenuDetaiBasePager;
import com.tantuo.didicar.utils.LogUtil;


/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：CallCarMenuDetailPager
 */
public class Setting2MenuDetailPager extends MenuDetaiBasePager {
    private TextView textView;

    public Setting2MenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);


        return textView;
    }


    @Override
    public void initData() {
        super.initData();
        textView.setText("slidingMenu第三行");
        LogUtil.i("slidingMenu第三行");

    }
}
