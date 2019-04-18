package com.tantuo.didicar.menudatailpager.CallCarTabPager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.base.BaseCallCarTabPager;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.utils.LogUtil;

/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：TabPager 点击打车功能以后通过不同的tab导向不同的 交通工具TabPager
 */
public class TabPager2 extends BaseCallCarTabPager {

    private final CallCarPagerBean.DataBean.ChildrenBean childrenData;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView textView;


    public TabPager2(Context context, CallCarPagerBean.DataBean.ChildrenBean childrenData) {
        super(context);
        this.childrenData = childrenData;
    }

    @Override

    public View initView() {
        LogUtil.i("进入： 类:TabPager2 -----方法:initView()---- ");
//        View view = View.inflate(context, R.layout.callcartab_2, null);
        //mMapView = (MapView) view.findViewById(R.id.bmapView);
//        mMapView = view.findViewById(R.id.bmapView);


            textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.RED);
            textView.setTextSize(25);


            return textView;


    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.i("进入： 类:TabPager2 -----方法:initData()---- ");
        textView.setText("打车详情页面内容1");

//        mBaiduMap = mMapView.getMap();


    }


}
