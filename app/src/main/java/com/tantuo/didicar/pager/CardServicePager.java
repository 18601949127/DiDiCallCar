package com.tantuo.didicar.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.tantuo.didicar.base.BasePager;
import com.tantuo.didicar.utils.LogUtil;

/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：CardServicePager
 */
public class CardServicePager extends BasePager {




    public CardServicePager(Context context) {
        super(context);

    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.i("服务按钮界面初始化了..");
        //1.设置标题
        tv_title.setText("身份验证");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("身份验pager证界面");

    }

}
