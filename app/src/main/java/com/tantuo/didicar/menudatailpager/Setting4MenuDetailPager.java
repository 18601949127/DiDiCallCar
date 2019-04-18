package com.tantuo.didicar.menudatailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tantuo.didicar.base.MenuDetaiBasePager;
import com.tantuo.didicar.utils.LogUtil;


/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：CallCarMenuDetailPager
 */
public class Setting4MenuDetailPager extends MenuDetaiBasePager {
    private TextView textView;

    public Setting4MenuDetailPager(Context context) {
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
        textView.setText("slidingMenu第五行");
        LogUtil.i("slidingMenu第五行");

    }
}
