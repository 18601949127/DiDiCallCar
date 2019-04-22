package com.tantuo.didicar.TabFragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.dialogfragment.TabDialogFragment1;
import com.tantuo.didicar.utils.LogUtil;

public class TabFragment2 extends BaseFragment {

    private static final String TAG = TabFragment2.class.getSimpleName();
    private final String title;
    private final String contents;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;


    public TabFragment2(String title, String contents){
        super();
        this.title = title;
        this.contents = contents;

    }
    @Override
    public View initView() {
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment2, 方法:initView()  ");
        View view =  View.inflate(getActivity(), R.layout.callcar_tab_fragment_2,null);

        mMapView =  view.findViewById(R.id.bmapView);
        return view;
    }

    @Override
    public void initData() {
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment2, 方法:initData()  ");
        super.initData();

        mBaiduMap = mMapView.getMap();

        //new TabDialogFragment1().show(getChildFragmentManager(), "dialog");
    }

    @Override
    public String gettitle() {
        return title;
    }
}
