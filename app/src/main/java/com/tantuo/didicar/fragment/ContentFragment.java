package com.tantuo.didicar.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.adapter.ContentFragmentAdapter;
import com.tantuo.didicar.base.BaseMenuFragment;
import com.tantuo.didicar.base.BasePager;

import com.tantuo.didicar.utils.LogUtil;
import com.tantuo.didicar.view.NoScrollViewPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：左侧抽屉菜单，继承自 BaseMenuFragment
 */

public class ContentFragment extends BaseMenuFragment {

    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewpager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;


    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.i("正文视图被初始化");


        View view = View.inflate(context, R.layout.content_fragment, null);


        //view关联注入ontentFragment
        x.view().inject(ContentFragment.this, view);


        return view;

    }

    @Override
    protected void initData() {
        super.initData();
        LogUtil.i("这里是正文");



        //设置ViewPager的适配器
        viewpager.setAdapter(new ContentFragmentAdapter(basePagers));


        //设置RadioGroup的选中状态改变的监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //监听某个页面被选中，初始对应的页面的数据
//        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置默认选中首页
        rg_main.check(R.id.rb_map);

        basePagers.get(0).initData();
        //设置模式SlidingMenu不可以滑动
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调这个方法
         *
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            LogUtil.i("进入： 类:MyOnPageChangeListener -----方法:onPageSelected()---- ");
//            BasePager basePager = basePagers.get(position);
            //调用被选中的页面的initData方法
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * getCallCarPager() 需要在点击打车界面的左侧slingMenu以后找到ContentFragment里面的打车页面
     *
     * @return CallCarPager
     * 今天周六窗外的景色真好
     */

    //实现点击屏幕下方Radiogroup切换不同ViewPager最重要的方法

    /**
     * 点击下方的按钮，切换不同的 contentFragment
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_map:
                    viewpager.setCurrentItem(0, true);
                    //smoothScroll代表是否滑动渐入或者渐出 by:Tantuo
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_home:
                    viewpager.setCurrentItem(1, true);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_cardservice:
                    viewpager.setCurrentItem(2, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair:
                    viewpager.setCurrentItem(3, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting:
                    viewpager.setCurrentItem(4, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }

        }
    }

    /**
     * 根据传入的参数设置是否让SlidingMenu可以滑动
     */
    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

}
