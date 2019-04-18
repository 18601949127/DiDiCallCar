package com.tantuo.didicar.menudatailpager.CallCarTabPager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tantuo.didicar.base.BaseCallCarTabPager;
import com.tantuo.didicar.domain.CallCarPagerBean;

/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：TabPager 点击打车功能以后通过不同的tab导向不同的 交通工具TabPager
 */
public class TabPager12 extends BaseCallCarTabPager {

    private final CallCarPagerBean.DataBean.ChildrenBean childrenData;
    private TextView textView;

    public TabPager12(Context context, CallCarPagerBean.DataBean.ChildrenBean childrenData) {
        super(context);
        this.childrenData = childrenData;
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
        textView.setText(childrenData.getTitle());

    }

}
