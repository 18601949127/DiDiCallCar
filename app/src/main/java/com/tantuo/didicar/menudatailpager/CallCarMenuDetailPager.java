package com.tantuo.didicar.menudatailpager;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;

import com.tantuo.didicar.adapter.CallCarTabPagerAdapter;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.base.MenuDetaiBasePager;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.utils.LogUtil;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：CallCarMenuDetailPager
 * 今天从老家回来了，权当新年第一碗鸡汤
 */
public class CallCarMenuDetailPager extends MenuDetaiBasePager {

    MainActivity mainActivity = (MainActivity) context;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tab_page_indicator)
    private TabPageIndicator tabPageIndicator;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;


    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    //tabDetailPager的数据集合
    private List<CallCarPagerBean.DataBean.ChildrenBean> children;

    //tabDetailPager打车界面的fragment页面集合
    private ArrayList<BaseFragment> callcarFragments;

    //TabFragments显示打车页面的adapter
    FragmentPagerAdapter tabFragmentPagerAdapter;

    //打车Tab的数据和标题从联网获取
    private String callcar_title;


    public CallCarMenuDetailPager(Context context, CallCarPagerBean.DataBean detailPagerData) {
        super(context);
        children = detailPagerData.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.callcar_menu_detail_pager, null);
        x.view().inject(CallCarMenuDetailPager.this, view);
        //设置tab_indicator_next点击事件
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        return view;

    }


    @Override
    public void initData() {
        super.initData();

        LogUtil.i("slidingMenu第一行");
        

        //1.这里为TabDetailPagers打车界面准备fragment界面
        callcarFragments = new ArrayList<>();

//        callcarFragments.add(new TabFragment0(children.get(0),"textview"+children.get(0).getTitle()));
//        callcarFragments.add(new TabFragment1(children.get(1),"textview"+children.get(1).getTitle()));
//        callcarFragments.add(new TabFragment2(children.get(2),"textview"+children.get(2).getTitle()));
//        callcarFragments.add(new TabFragment3(children.get(3),"textview"+children.get(3).getTitle()));
//        callcarFragments.add(new TabFragment4(children.get(4),"textview"+children.get(4).getTitle()));
//        callcarFragments.add(new TabFragment5(children.get(5),"textview"+children.get(5).getTitle()));
//        callcarFragments.add(new TabFragment6(children.get(6),"textview"+children.get(6).getTitle()));
//        callcarFragments.add(new TabFragment7(children.get(7),"textview"+children.get(7).getTitle()));
//        callcarFragments.add(new TabFragment8(children.get(8),"textview"+children.get(8).getTitle()));
//        callcarFragments.add(new TabFragment9(children.get(9),"textview"+children.get(9).getTitle()));
//        callcarFragments.add(new TabFragment10(children.get(10),"textview"+children.get(10).getTitle()));


        //2.这里为打车界面tabFragments准备数据
//        FragmentManager fm = getSupportFragmentManager();
        tabFragmentPagerAdapter= new CallCarTabPagerAdapter(MainActivity.manager,callcarFragments);
        viewPager.setAdapter(tabFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(6);//viewPager预加载，从0开始

        //将viewPager绑定到viewPagerIndicator
        tabPageIndicator.setViewPager(viewPager);
        //绑定之后注意如果要使用ViewPagerIndicator,还要在当前activity的 Manifest文件类把样式修改成下面 by:tantuo
        //android:theme="@style/Theme.PageIndicatorDefaults">
        //并且可以在viewPageIndicator中的 values 文件里修改样式，比如自定义 colorSelector


        //注意viewPageIndicator的PageChangeListener比 viewPager的优先级高，因此要使用indicator的listener
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }


//    private class MyTabDetailPagerAdapter extends PagerAdapter {
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return children.get(position).getTitle();
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            BaseCallCarTabPager tabDetailPager = callcarFragments.get(position);
//            LogUtil.i("进入： 类:MyTabDetailPagerAdapter -----方法:instantiateItem()----初始化第" + position + "个界面");
//
//
//            View rootView = tabDetailPager.rootView;//各个子页面
//            //调用initData()
//            //注意：这里的initData()看上去调用的是BaseCallCarTabPager,肯定是调用的是各个子类pager的。by:Tantuo
//            //tabDetailPager.initData();//初始化数据
//            LogUtil.i("进入： 类:MyTabDetailPagerAdapter -----方法:instantiateItem()----初始化第" + position + "个界面,initData()方法");
//
//            container.addView(rootView);
//            return rootView;
//
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//
//        @Override
//        public int getCount() {
//            return callcarFragments.size();
//
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    }

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
//
//            callcarFragments.get(position).initData();


        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtil.i("----------------进入了TabdetailPager的onPagerScrollChanged方法 ");

        }
    }
}
