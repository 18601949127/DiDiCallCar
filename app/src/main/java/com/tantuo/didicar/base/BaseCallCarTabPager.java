package com.tantuo.didicar.base;

import android.content.Context;
import android.view.View;

/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：BaseCallCarTabPager 对应的是打车界面点击TabIndicator对应的TabPager
 * 今天到老家阳江，这边的天气很好很暖和
 */
public abstract class BaseCallCarTabPager {

    /**
     * 上下文
     */
    public final Context context;


    /**
     * 代表每个对应TabIndicator 的detailPager
     * 注意：有Tab的都是点击上边viewPagerIndicator(快车，出租车)对应的detailPager
     * by:tantuo
     */
    public View rootView;

    public BaseCallCarTabPager(Context context) {
        this.context = context;
        rootView = initView();//抽象方法，每个子页面必须重写 by:tantuo
    }


    /**
     * abstract抽象方法，强制子视图实现该方法，每个页面实现不同的视图
     * 因为每一个CallCarTabPager都完全不一样，所以强制它们实现initView方法
     * by:tantuo
     */
    public abstract View initView();


    /**
     * 子页面需要重新绑定数据，联网请求数据等的时候，重写该方法
     */
    public void initData() {

    }

    public void removeMaps(){

    }

}