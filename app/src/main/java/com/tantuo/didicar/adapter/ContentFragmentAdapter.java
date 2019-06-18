package com.tantuo.didicar.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tantuo.didicar.base.BasePager;

import java.util.ArrayList;

/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：ContentFragmentAdapter 屏幕正中央的ViewPager
 */

public class ContentFragmentAdapter extends PagerAdapter {

    private final ArrayList<BasePager> basePagers;

    public ContentFragmentAdapter(ArrayList<BasePager> basePagers) {
        this.basePagers = basePagers;
    }

    @Override
    public int getCount() {

        return basePagers.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager basePager = basePagers.get(position);//各个页面的实例
        View rootView = basePager.rootView;//各个子页面

        //调用initData()
        //注意：这里的initData()看上去调用的是basePager,肯定是调用的是各个子类pager的。by:Tantuo
        basePager.initData();//初始化数据

        container.addView(rootView);
        return rootView;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}