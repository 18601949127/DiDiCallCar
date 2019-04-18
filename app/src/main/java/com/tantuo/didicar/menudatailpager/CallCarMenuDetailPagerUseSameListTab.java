package com.tantuo.didicar.menudatailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tantuo.didicar.R;
import com.tantuo.didicar.base.MenuDetaiBasePager;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.menudatailpager.CallCarTabPager.TabDetailPagerUseSameListTab;
import com.tantuo.didicar.utils.LogUtil;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：CallCarMenuDetailPager
 * 今天从老家回来了，权当新年第一碗鸡汤
 */
public class CallCarMenuDetailPagerUseSameListTab extends MenuDetaiBasePager {

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

    //tabDetailPager的页面集合
    private ArrayList<TabDetailPagerUseSameListTab> tabDetailPagers;


    public CallCarMenuDetailPagerUseSameListTab(Context context, CallCarPagerBean.DataBean detailPagerData) {
        super(context);
        children = detailPagerData.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.callcar_menu_detail_pager, null);
        x.view().inject(CallCarMenuDetailPagerUseSameListTab.this, view);
        //设置tab_indicator_next点击事件
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        return view;

    }


    @Override
    public void initData() {
        super.initData();

        LogUtil.i("slidingMenu第一行");

        //1.这里为TabDetailPagers准备页面
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TabDetailPagerUseSameListTab(context, children.get(i)));
        }

        //2.这里为TabDetailPagers准备数据
        viewPager.setAdapter(new MyTabDetailPagerAdapter());

        //将viewPager绑定到viewPagerIndicator
        tabPageIndicator.setViewPager(viewPager);
        //绑定之后注意如果要使用ViewPagerIndicator,还要在当前activity的 Manifest文件类把样式修改成下面
        //android:theme="@style/Theme.PageIndicatorDefaults">
        //并且可以在viewPageIndicator中的 values 文件里修改样式，比如自定义 colorSelector


        //注意viewPageIndicator的PageChangeListener比 viewPager的优先级高，因此要使用indicator的listener
        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    private class MyTabDetailPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPagerUseSameListTab tabDetailPager = tabDetailPagers.get(position);
            View rootview = tabDetailPager.rootView;
            tabDetailPager.initData(); //
            container.addView(rootview);

            return rootview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
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

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
