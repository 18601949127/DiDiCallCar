package com.tantuo.didicar.TabFragment;

import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.utils.LogUtil;

public class TabFragment4 extends BaseFragment {

    private static final String TAG = TabFragment4.class.getSimpleName();
    private final String title;
    private final String contents;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView textView;


    public TabFragment4(String title, String contents){
        super();
        this.title = title;
        this.contents = contents;

    }
    @Override
    public View initView() {
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment4, 方法:initView()  ");
        View view =  View.inflate(getActivity(), R.layout.callcar_tab_fragment_4,null);

        mMapView =  view.findViewById(R.id.bmapView);
        textView = view.findViewById(R.id.textview);
        return view;
    }

    @Override
    public void initData() {
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment4, 方法:initData()  ");
        super.initData();

        mBaiduMap = mMapView.getMap();
    }

    @Override
    public String gettitle() {
        return title;
    }
}
