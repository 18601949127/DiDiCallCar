package com.tantuo.didicar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.SupportMapFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tantuo.didicar.fragment.ContentFragment;
import com.tantuo.didicar.fragment.LeftMenuFragment;
import com.tantuo.didicar.utils.LogUtil;



/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：MainActivity 进入主界面
 */
public class MainActivity extends SlidingFragmentActivity {


    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static SupportMapFragment map;
    public static FragmentManager manager;
    private SlidingMenu slidingMenu;

//    private MapView mMapView;
//    private BaiduMap mBaiduMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

        SDKInitializer.initialize(getApplicationContext());



        setContentView(R.layout.activity_main);


        initSlidingMenu();

        initFragment();


//        mMapView = (MapView) findViewById(R.id.bmapView);
//        mBaiduMap = mMapView.getMap();


    }

    private void initSlidingMenu() {
        LogUtil.i("进入： 类:MainActivity -----方法:initSlidingMenu()---- ");
        //添加左侧Sliding_Menu
        setBehindContentView(R.layout.activity_leftmenu);
        //添加右侧菜单 secondaryMenu
        slidingMenu = (SlidingMenu)getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);
        //设置SlidingMenu模式
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setFadeDegree(0.4f);
        slidingMenu.setFadeEnabled(true);


        //得到slidingMenu在手机屏幕上面滑动的比例为0.35
        DisplayMetrics displayMetricscs = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetricscs);
        int screeWidth = displayMetricscs.widthPixels;
        //int screeHeight = displayMetricscs.heightPixels;
        slidingMenu.setBehindOffset((int) (screeWidth * 0.65));
    }

    private void initFragment() {
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换
        ft.replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG);//主页
        ft.replace(R.id.fl_leftmenu, new LeftMenuFragment(), LEFTMENU_TAG);//左侧菜单
        //4.提交
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        //mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        //mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        //mMapView.onPause();
    }

    public LeftMenuFragment getleftMenuFragment() {
        LogUtil.i("进入： 类:MainActivity -----方法:getleftMenuFragment()---- ");
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment lf = (LeftMenuFragment) fm.findFragmentByTag(LEFTMENU_TAG);

        return lf;
    }


    //由leftMenuFragment得到右侧的主界面ContentFragment
    public ContentFragment getContentFragment() {
        LogUtil.i("进入： 类:MainActivity -----方法:getContentFragment()---- ");
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment cf = (ContentFragment) fm.findFragmentByTag(MAIN_CONTENT_TAG);

        return cf;

    }
}
