package com.tantuo.didicar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.SupportMapFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tantuo.didicar.TabFragment.TabFragment0;
import com.tantuo.didicar.TabFragment.TabFragment1;
import com.tantuo.didicar.TabFragment.TabFragment10;
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
import com.tantuo.didicar.dialogfragment.TabDialogFragment1;
import com.tantuo.didicar.fragment.ContentFragment;
import com.tantuo.didicar.fragment.LeftMenuFragment;
import com.tantuo.didicar.utils.LogUtil;
import com.tantuo.didicar.view.NoScrollViewPager;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：MainActivity 进入主界面
 */
public class MainActivity extends SlidingFragmentActivity {


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


    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static SupportMapFragment map;
    public static FragmentManager manager;
    private SlidingMenu slidingMenu;


    //tabDetailPager打车界面的fragment页面集合
    private ArrayList<BaseFragment> callcarFragments;

    //TabFragments显示打车页面的adapter
    FragmentPagerAdapter tabFragmentPagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现


        setContentView(R.layout.activity_main);
        x.view().inject(MainActivity.this);


        //初始化左侧SlidingMenuFragment
        initSlidingMenu();
        initSlidingMenuFragment();

        //初始化打车界面Fragment集合
        initCallCarFragments();




    }

    private void initCallCarFragments() {
        //1.这里为TabDetailPagers打车界面准备fragment界面
        callcarFragments = new ArrayList<>();

        callcarFragments.add(new TabFragment0("打车", "打车界面"));
        callcarFragments.add(new TabFragment1("公交车", "打车界面"));
        callcarFragments.add(new TabFragment2("出租车", "打车界面"));
        callcarFragments.add(new TabFragment3("单车", "打车界面"));
        callcarFragments.add(new TabFragment4("金融服务", "打车界面"));
        callcarFragments.add(new TabFragment5("出租车", "打车界面"));
        callcarFragments.add(new TabFragment6("公交", "打车界面"));
        callcarFragments.add(new TabFragment7("金融", "打车界面"));
        callcarFragments.add(new TabFragment8("打车6", "打车界面"));
        callcarFragments.add(new TabFragment9("打车8", "打车界面"));
        callcarFragments.add(new TabFragment10("打车0", "打车界面"));


        //2.这里为打车界面tabFragments准备数据
        FragmentManager fm = getSupportFragmentManager();
        tabFragmentPagerAdapter = new CallCarTabPagerAdapter(fm, callcarFragments);
        viewPager.setAdapter(tabFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(0);//viewPager预加载，从0开始

        //将viewPager绑定到 Tablayout
        tablayoutIndicator.setupWithViewPager(viewPager);
        tablayoutIndicator.setTabMode(TabLayout.MODE_SCROLLABLE);

        //绑定之后注意如果要使用ViewPagerIndicator,还要在当前activity的 Manifest文件类把样式修改成下面 by:tantuo
        //android:theme="@style/Theme.PageIndicatorDefaults">
        //并且可以在viewPageIndicator中的 values 文件里修改样式，比如自定义 colorSelector


        //注意viewPageIndicator的PageChangeListener比 viewPager的优先级高，因此要使用indicator的listener
        //使用Tablayout只需要实现 MyOnPagerChangeListener()就可以了
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initSlidingMenu() {
        LogUtil.i("进入： 类:MainActivity -----方法:initSlidingMenu()---- ");
        //添加左侧Sliding_Menu
        setBehindContentView(R.layout.activity_leftmenu);
        //添加右侧菜单 secondaryMenu
        slidingMenu = (SlidingMenu) getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);
        //设置SlidingMenu模式
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setFadeDegree(0.4f);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);


        //得到slidingMenu在手机屏幕上面滑动的比例为0.35
        DisplayMetrics displayMetricscs = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetricscs);
        int screeWidth = displayMetricscs.widthPixels;
        //int screeHeight = displayMetricscs.heightPixels;
        slidingMenu.setBehindOffset((int) (screeWidth * 0.65));
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
        ft.replace(R.id.fl_leftmenu, new LeftMenuFragment(), LEFTMENU_TAG);//左侧菜单
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
            LogUtil.i("----------------------进入了TabdetailPager的onPagerSelected方法 ");

//          callcarFragments.get(position).initView();
          callcarFragments.get(position).initData();


        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtil.i("----------------进入了TabdetailPager的onPagerScrollChanged方法 ");

        }
    }
}
