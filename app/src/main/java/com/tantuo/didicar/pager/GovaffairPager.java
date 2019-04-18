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
 * 作用：GovaffairPager
 */
public class GovaffairPager extends BasePager {



    public GovaffairPager(Context context) {
        super(context);

    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.i("合规按钮数据初始化..");
        //1.设置标题
        tv_title.setText("合规界面");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("合规数据pager初始化");

    }
}
