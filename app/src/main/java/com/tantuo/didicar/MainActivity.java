package com.tantuo.didicar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tantuo.didicar.DriverLicenseNFC.DriverRFIDMainActivity;
import com.tantuo.didicar.DriverLicenseRecognition.LicenseMainActivity;
import com.tantuo.didicar.TabFragment.TabFragment0;
import com.tantuo.didicar.TabFragment.TabFragment1;
import com.tantuo.didicar.TabFragment.TabFragment2;
import com.tantuo.didicar.TabFragment.TabFragment3;
import com.tantuo.didicar.TabFragment.TabFragment4;
import com.tantuo.didicar.TabFragment.TabFragment5;
import com.tantuo.didicar.TabFragment.TabFragment6;
import com.tantuo.didicar.TabFragment.TabFragment7;
import com.tantuo.didicar.TabFragment.TabFragment8;
import com.tantuo.didicar.TabFragment.TabFragment9;
import com.tantuo.didicar.adapter.CallCarTabPagerAdapter;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.fragment.ContentFragment;
import com.tantuo.didicar.fragment.LeftSlidingFragment;
import com.tantuo.didicar.utils.LocaleUtils;
import com.tantuo.didicar.utils.LogUtil;
import com.tantuo.didicar.view.NoScrollViewPager;

import android.widget.CompoundButton.OnCheckedChangeListener;

import org.opencv.android.OpenCVLoader;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：MainActivity 进入主界面
 */
public class MainActivity extends SlidingFragmentActivity implements OnCheckedChangeListener {



    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.ib_nfc_detector)
    private ImageButton ib_nfc_detector;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.ib_license)
    private ImageButton ib_license;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_title)
    public TextView tv_title;


    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.ib_menu)
    private ImageButton ib_menu;


    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.viewpager)
    public NoScrollViewPager viewPager;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tab_page_indicator)
    private TabLayout tablayoutIndicator;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.switch1)
    private ImageButton language_switch;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.current_city)
    private TextView current_city;


    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    public static FragmentManager manager;
    private SlidingMenu slidingMenu;

    public static InputMethodManager imm;


    //tabDetailPager打车界面的fragment页面集合
    private ArrayList<BaseFragment> callcarFragments;

    //TabFragments显示打车页面的adapter
    FragmentPagerAdapter tabFragmentPagerAdapter;
    private BaiduMap mBaiduMap;
    private String[] titles = {"快车", "出租车", "秒走打车", "单车", "金融服务", "出租车", "公交", "金融", "打车", "第三方"};

    //定位相关变量

    private LocationClient mLocationClient;
    public static BDLocation startlocation = null;
    public static LatLng startll;
    public static MyLocationData startlocData;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private LocationClient mLocClient;
    public static String CurrentCity;
    public static String currentStreet;
    public static String currentBuilding;
    boolean isFirstLoc = true; // 是否首次定位
    private int[] img = {R.drawable.language_ch, R.drawable.language_en};//定义一个int数组，用来放图片
    private boolean languageflag = false;//定义一个标识符，用来判断是中文界面，还是英文界面


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现


        setContentView(R.layout.activity_main);
        x.view().inject(MainActivity.this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        //初始化顶部标题栏
        initTitleBar();


        //初始化左侧SlidingMenuFragment
        initSlidingMenu();
        initSlidingMenuFragment();

        //得到定位地址
        initLocationClient();


        iniLoadOpenCV();


    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

        if (arg1) {
            // 开
            if (LocaleUtils.needUpdateLocale(MainActivity.this, LocaleUtils.LOCALE_ENGLISH)) {
                LocaleUtils.updateLocale(MainActivity.this, LocaleUtils.LOCALE_ENGLISH);
                restartAct();
            }
        } else {
            // 关
            Intent intent = new Intent(MainActivity.this, LicenseMainActivity.class);
            startActivity(intent);
        }

    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {


            if (isFirstLoc) {
                isFirstLoc = false;

                mCurrentLat = location.getLatitude();
                mCurrentLon = location.getLongitude();
                mCurrentAccracy = location.getRadius();
                startlocData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置获取到的方向信息，顺时针0-360
                        .direction(mCurrentDirection).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                startll = new LatLng(location.getLatitude(), location.getLongitude());
                CurrentCity = location.getCity();
                currentStreet = location.getStreet();
                currentBuilding = location.getBuildingName();
                startlocation = location;


                if(currentBuilding != null) {
                    Toast.makeText(getApplicationContext(), "您的位置：" + currentBuilding, Toast.LENGTH_SHORT).show();
                    return;
                }

                //得到地址信息以后立即把第一个客户可见的出行fragment当前位置界面展示出来
                //初始化打车界面Fragment集合
                initCallCarFragments();
                current_city.setText(CurrentCity);
                callcarFragments.get(0).initData();

            }


        }

    }

    private void initLocationClient() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }


    private void initTitleBar() {


        ib_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到司机证件自动识别界面
                Intent intent = new Intent(MainActivity.this, LicenseMainActivity.class);
                startActivity(intent);

            }
        });

        ib_nfc_detector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到司机证件自动识别界面
                Intent intent = new Intent(MainActivity.this, DriverRFIDMainActivity.class);
                startActivity(intent);

            }
        });


        language_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先判断flag是真还是假，分别显示中文和英文
                if (languageflag) {
                    language_switch.setImageResource(img[0]);//中文
                    languageflag = false;
                    if (LocaleUtils.needUpdateLocale(MainActivity.this, LocaleUtils.LOCALE_ENGLISH)) {
                        LocaleUtils.updateLocale(MainActivity.this, LocaleUtils.LOCALE_ENGLISH);
                        restartAct();
                    }

                } else {
                    language_switch.setImageResource(img[1]);//英文
                    languageflag = true;

                    if (LocaleUtils.needUpdateLocale(MainActivity.this, LocaleUtils.LOCALE_CHINESE)) {
                        LocaleUtils.updateLocale(MainActivity.this, LocaleUtils.LOCALE_CHINESE);
                        restartAct();
                    }
                }
            }
        });

    }

    private void iniLoadOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if (success) {

            LogUtil.i("进入类:MainActivity, 方法:iniLoadOpenCV() iniLoadOpenCV() 成功 ");
        } else {


            LogUtil.i("进入类:MainActivity, 方法:iniLoadOpenCV() iniLoadOpenCV() 没有成功 ");
        }
    }

    private void initCallCarFragments() {
        //1.这里为TabDetailPagers打车界面准备fragment界面
        callcarFragments = new ArrayList<>();

        callcarFragments.add(new TabFragment0(titles[0], "打车界面"));
        callcarFragments.add(new TabFragment1(titles[1], "打车界面"));
        callcarFragments.add(new TabFragment2(titles[2], "打车界面"));
        callcarFragments.add(new TabFragment3(titles[3], "打车界面"));
        callcarFragments.add(new TabFragment4(titles[4], "打车界面"));
        callcarFragments.add(new TabFragment5(titles[5], "打车界面"));
        callcarFragments.add(new TabFragment6(titles[6], "打车界面"));
        callcarFragments.add(new TabFragment7(titles[7], "打车界面"));
        callcarFragments.add(new TabFragment8(titles[8], "打车界面"));
        callcarFragments.add(new TabFragment9(titles[9], "打车界面"));


        //2.这里为打车界面tabFragments准备数据
        FragmentManager fm = getSupportFragmentManager();
        tabFragmentPagerAdapter = new CallCarTabPagerAdapter(fm, callcarFragments);
        viewPager.setAdapter(tabFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(0);//viewPager预加载，从0开始

        //将viewPager绑定到 Tablayout
        tablayoutIndicator.setupWithViewPager(viewPager);
        tablayoutIndicator.setTabMode(TabLayout.MODE_SCROLLABLE);



        //注意viewPageIndicator的PageChangeListener比 viewPager的优先级高，因此要使用indicator的listener
        //使用Tablayout只需要实现 MyOnPagerChangeListener()就可以了
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());


    }

    private void initSlidingMenu() {
        //添加左侧Sliding_Menu
        setBehindContentView(R.layout.activity_leftmenu);
        //添加右侧菜单 secondaryMenu
        slidingMenu = (SlidingMenu) getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);
        //设置SlidingMenu模式
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setFadeEnabled(false);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);


        //得到slidingMenu在手机屏幕上面滑动的比例为 setBehindOffset
        DisplayMetrics displayMetricscs = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetricscs);
        int screeWidth = displayMetricscs.widthPixels;
        //int screeHeight = displayMetricscs.heightPixels;
        slidingMenu.setBehindOffset((int) (screeWidth * 0.5));
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 使用LeftMenuFragment替换左侧slidingmenu的frameLayout
     */
    private void initSlidingMenuFragment() {
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换
        ft.replace(R.id.fl_leftmenu, new LeftSlidingFragment(), LEFTMENU_TAG);//左侧菜单
        //4.提交
        ft.commit();

        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggle() 方法会让 slidingMenu开关切换
                getSlidingMenu().toggle();
            }
        });
    }


    //由leftMenuFragment得到右侧的主界面ContentFragment
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment cf = (ContentFragment) fm.findFragmentByTag(MAIN_CONTENT_TAG);

        return cf;

    }


    /**
     * 点击ViewpageIndicator的点击事件
     * by:tantuo
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
//          callcarFragments.get(position).initView();
            callcarFragments.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    @Override
    protected void onDestroy() {
        TabFragment0.mBaiduMap.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 刷新语言
     */
    private void restartAct() {
        finish();
        Intent _Intent = new Intent(this, MainActivity.class);
        startActivity(_Intent);
        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);
    }

    /**
     * 自己出创建的内部类，监听按钮点击事件
     *
     * @author cyf
     */
    class MyOnCheckedChangeListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

            if (arg1) {
                if (LocaleUtils.needUpdateLocale(MainActivity.this, LocaleUtils.LOCALE_ENGLISH)) {
                    LocaleUtils.updateLocale(MainActivity.this, LocaleUtils.LOCALE_ENGLISH);
                    restartAct();
                }

            } else {
                // 关
                if (LocaleUtils.needUpdateLocale(MainActivity.this, LocaleUtils.LOCALE_CHINESE)) {
                    LocaleUtils.updateLocale(MainActivity.this, LocaleUtils.LOCALE_CHINESE);
                    restartAct();
                }


            }

        }

    }

}
