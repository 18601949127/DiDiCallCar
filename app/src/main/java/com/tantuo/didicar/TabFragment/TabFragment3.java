package com.tantuo.didicar.TabFragment;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.utils.LogUtil;

public class TabFragment3 extends BaseFragment {

    private static final String TAG = TabFragment3.class.getSimpleName();
    private final String title;
    private final String contents;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView textView;


    public TabFragment3(String title, String contents){
        super();
        this.title = title;
        this.contents = contents;

    }
    @Override
    public View initView() {

        Toast.makeText(getActivity(), "tab3 initView()", Toast.LENGTH_SHORT).show();
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment3, 方法:initView()  ");
        View view =  View.inflate(getActivity(), R.layout.callcar_tab_fragment_3,null);

        mMapView =  view.findViewById(R.id.bmapView);
        textView = view.findViewById(R.id.textview);
        return view;
    }

    @Override
    public void initData() {

        Toast.makeText(context, "tab3 initData()", Toast.LENGTH_SHORT).show();
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment3, 方法:initData()  ");
        super.initData();

        mBaiduMap = mMapView.getMap();
        textView.setText(contents);
    }

    @Override
    public String gettitle() {
        return title;
    }
}
