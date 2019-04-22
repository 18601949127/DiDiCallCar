package com.tantuo.didicar.menudatailpager.CallCarTabPager;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseCallCarTabPager;
import com.tantuo.didicar.dialogfragment.TabDialogFragment1;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.utils.LogUtil;

/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：TabPager 点击打车功能以后通过不同的tab导向不同的 交通工具TabPager
 */
public class TabPager1 extends BaseCallCarTabPager {

    private final CallCarPagerBean.DataBean.ChildrenBean childrenData;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;



    public TabPager1(Context context, CallCarPagerBean.DataBean.ChildrenBean childrenData) {
        super(context);
        this.childrenData = childrenData;
    }

    @Override
    public View initView() {
        MainActivity mainActivity = (MainActivity) context;
        LogUtil.i("进入： 类:TabPager1 -----方法:initView()---- ");
        View view = View.inflate(context, R.layout.callcartab_4,null);
        //mMapView = (MapView) view.findViewById(R.id.bmapView);
        mMapView =  view.findViewById(R.id.bmapView);

//        new TabDialogFragment1().show(((MainActivity) context).getFragmentManager(),"dialog");

        return view;
    }

    @Override
    public void initData() {
        LogUtil.i("进入： 类:TabPager1 -----方法:initData()---- ");
        super.initData();
        mBaiduMap = mMapView.getMap();

    }

    /**
     * 弹出动画
     * @param view
     */
    public static void slideToUp(View view){
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(2000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


}
