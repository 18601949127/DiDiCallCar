package com.tantuo.didicar.menudatailpager.CallCarTabPager;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseCallCarTabPager;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.utils.LogUtil;

/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：TabPager 点击打车功能以后通过不同的tab导向不同的 交通工具TabPager
 */
public class TabPager7 extends BaseCallCarTabPager {
    private final CallCarPagerBean.DataBean.ChildrenBean childrenData;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;


    public TabPager7(Context context, CallCarPagerBean.DataBean.ChildrenBean childrenData) {
        super(context);
        this.childrenData = childrenData;
    }

    @Override
    public View initView() {
        LogUtil.i("进入： 类:TabPager7 -----方法:initView()---- ");
        View view = View.inflate(context, R.layout.callcartab_7,null);
        //mMapView = (MapView) view.findViewById(R.id.bmapView);
        mMapView =  view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        return view;
    }

    @Override
    public void initData() {
        LogUtil.i("进入： 类:TabPager7 -----方法:initData()---- ");
        super.initData();

    }

}