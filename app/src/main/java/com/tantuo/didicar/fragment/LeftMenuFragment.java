package com.tantuo.didicar.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseMenuFragment;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.pager.CallCarPager;
import com.tantuo.didicar.utils.DensityUtil;
import com.tantuo.didicar.utils.LogUtil;

import java.util.List;

/**
 * Author by TanTuo, WeiXin:86-18601949127,
 * Email:1991201740@qq.com
 * 作用：左侧抽屉菜单，继承自 BaseMenuFragment
 */

public class LeftMenuFragment extends BaseMenuFragment {

    private List<CallCarPagerBean.DataBean> data;
    private ListView listView;
    private LeftmenuFragmentAdapter adapter;

    private int prePosition;


    @Override
    public View initView() {

        LogUtil.e("左侧菜单视图被初始化了");
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context, 40), 0, 0);
        listView.setDividerHeight(5);//设置分割线高度为0
        listView.setCacheColorHint(Color.RED);

        //设置按下listView的item不变色
        listView.setSelector(android.R.color.holo_orange_dark);

        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.记录点击的位置，变成红色
                prePosition = position;
                //notifyDataChanged会出发 listview刷新操作，执行getCount()-->getView
                adapter.notifyDataSetChanged();

                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                //toggle() 方法会让 slidingMenu开关切换
                mainActivity.getSlidingMenu().toggle();

                //3.切换到对应的打车detailPager
                switchLeftMenuDetailPager(prePosition);


            }
        });

        return listView;
    }

    /**
     * 切换到对应的打车detailPager
     *
     * @param position
     */
    private void switchLeftMenuDetailPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        CallCarPager call_carPager = contentFragment.getCallCarPager();
        call_carPager.swithCallCarDetailPager(position);
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtil.i("这里是左侧边栏");


    }

    public void setData(List<CallCarPagerBean.DataBean> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LogUtil.i("数据是 " + data.get(i).getTitle());
            LogUtil.i("data整体数据是" + data.toString());

            adapter = new LeftmenuFragmentAdapter();
            listView.setAdapter(adapter);

            //刚刚初始化LeftMenuFragment的时候也要默然点击LeftMenu第一行那样
            switchLeftMenuDetailPager(0);

        }
    }

    class LeftmenuFragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu, null);
            textView.setText(data.get(position).getTitle());
            if (position == prePosition) {
                //设置红色
                textView.setEnabled(true);

            } else {

                textView.setEnabled(false);
            }

            textView.setEnabled(position == prePosition);
            return textView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }
}
