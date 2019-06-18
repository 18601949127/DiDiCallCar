package com.tantuo.didicar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.test.mock.MockContentResolver;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tantuo.didicar.Bean.MenuItemsEntity;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;


import com.tantuo.didicar.adapter.MenuItemsAdapter;
import com.tantuo.didicar.utils.WebDetailActivityUtils;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：左侧抽屉菜单，继承自 BaseMenuFragment
 */

public class LeftSlidingFragment extends Fragment implements View.OnClickListener {
    private View view;//定义view用来设置fragment的layout
    public RecyclerView mCollectRecyclerView;//定义RecyclerView
    //定义以MenuItemsEntity实体类为对象的数据集合
    private List<MenuItemsEntity> menuItemsEntities = new ArrayList<MenuItemsEntity>();        //自定义recyclerveiw的适配器
    private MenuItemsAdapter menuItemsAdapter;
    private String[] menuItemsTitle = {"车主", "安全", "钱包", "积分", "投诉"};
    private int[] menuItemsIcon = {R.drawable.slidingmenu_order, R.drawable.slidingmenu_safety, R.drawable.slidingmenu_wallet, R.drawable.slidingmenu_service, R.drawable.slidingmenu_setting};//定义一个int数组，用来放图片
    public String[] menuItemsDetais = {"itemUr1", "itemUrl2", "itemUrl3", "itemUrl4", "itemUrl5"};
    private ImageView slidingmenu_more;
    private ImageView slidingmenu_regulations;
    private RecyclerView RecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取fragment的layout
        view = inflater.inflate(R.layout.collect_page, container, false);

        //对recycleview进行配置
        initView();
        //模拟数据
        initData();
        return view;
    }


    /**
     * 提供数据
     */
    private void initData() {

        menuItemsDetais[0] = "https://page.udache.com/growth/driver-recruit/index.html?channel=8889";
        menuItemsDetais[1] = "https://dpubstatic.udache.com/static/dpubimg/dpub2_project_187481/index_187481.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&flat=40.39293&flng=116.84192&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.392355381081394&lng=116.8424214994192&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow&pid=1_xID-B2_hV&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560742235707&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&";
        menuItemsDetais[2] = "https://dpubstatic.udache.com/static/dpubimg/dpub2_project_155465/index_155465.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.394847501736926&lng=116.84872499879633&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow%3D%3D&pid=1_ups4uiZE7&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560572308116&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553";
        menuItemsDetais[3] = "https://xmall.xiaojukeji.com/imall/index.htm?xmallsource=1002&sidechanne=3002&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=3012&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.394839795143554&lng=116.8487379582826&model=HWI-AL00&os=9&phone=W471piXc0R0glRFq7nvDow==&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&time=1560572808586&uid=281867467423745&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553&trip_country=CN&location_country=CN&TripCountry=CN&trip_cityId=1&trip_cityid=1&location_cityid=1&utc_offset=480&maptype=soso&origin_id=1&terminal_id=1&source=weixin_source&role=1&shared=true";
        menuItemsDetais[4] = "https://page.xiaojukeji.com/active/yijian.html?access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.39236573557325&lng=116.84140653162427&model=HWI-AL00&os=9&phone=W471piXc0R0glRFq7nvDow&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&time=1560572605842&uid=281867467423745&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553&trip_country=CN&location_country=CN&TripCountry=CN&trip_cityId=1&trip_cityid=1&location_cityid=1&utc_offset=480&maptype=soso&origin_id=1&terminal_id=1&stm=2%257C4ad7bc9d-0a78-464c-8f45-6f3cf3c66fa9%257C4ad7bc9d-0a78-464c-8f45-6f3cf3c66fa9";

        for (int i = 0; i < 5; i++) {
            MenuItemsEntity menuItemsEntity = new MenuItemsEntity();
            menuItemsEntity.setMenuItemsTitle(menuItemsTitle[i]);
            menuItemsEntity.setMenuItemsImgId(menuItemsIcon[i]);
            menuItemsEntity.setMenuItemDetails(menuItemsDetais[i]);
            menuItemsEntities.add(menuItemsEntity);
        }
    }

    /**
     * TODO 对recycleview进行配置
     */

    private void initView() {
        slidingmenu_more = view.findViewById(R.id.slidingmenu_more);
        slidingmenu_regulations = view.findViewById(R.id.slidingmenu_regulations);

        slidingmenu_more.setOnClickListener(this);
        slidingmenu_regulations.setOnClickListener(this);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        MenuItemsAdapter adapter = new MenuItemsAdapter(menuItemsEntities);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slidingmenu_more:
                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), "https://page.udache.com/growth/driver-recruit/index.html?channel=8889");
                break;


            case R.id.slidingmenu_regulations:
                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), "https://dpubstatic.udache.com/static/dpubimg/cd9e085ca23dade43fce630ce4879536/index.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&daijia_pid=24786007&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&entrance_price=1&flat=40.39448&flng=116.84878&from=app&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.394839795143554&lng=116.8487379582826&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow&pid=1_3oBQLD8W1&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560589112618&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553");
                break;
        }
    }

}