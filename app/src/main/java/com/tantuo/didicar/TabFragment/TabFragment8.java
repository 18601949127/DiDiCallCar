package com.tantuo.didicar.TabFragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.utils.LogUtil;

public class TabFragment8 extends BaseFragment {

    private static final String TAG = TabFragment8.class.getSimpleName();
    private final String title;
    private final String contents;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView textView;


    public TabFragment8(String title, String contents){
        super();
        this.title = title;
        this.contents = contents;

    }
    @Override
    public View initView() {
        Log.i(TAG,"这是TabFragment8被初始化了，  TabFragment8.initView ( ) 被执行了");
        View view =  View.inflate(getActivity(), R.layout.callcar_tab_fragment_8,null);

        mMapView =  view.findViewById(R.id.bmapView);
        textView = view.findViewById(R.id.textview);
        return view;
    }

    @Override
    public void initData() {
        LogUtil.i("这是TabFragment8 数据被初始化了，  TabFragment8.initData ( ) 被执行了 ");
        super.initData();

        mBaiduMap = mMapView.getMap();
        textView.setText(contents);
    }

    @Override
    public String gettitle() {
        return title;
    }
}
