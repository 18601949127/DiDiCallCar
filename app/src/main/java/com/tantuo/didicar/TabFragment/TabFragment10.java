package com.tantuo.didicar.TabFragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.utils.LogUtil;

public class TabFragment10 extends BaseFragment {

    private static final String TAG = TabFragment10.class.getSimpleName();
    private final CallCarPagerBean.DataBean.ChildrenBean data;
    private final String title;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView textView;


    public TabFragment10(CallCarPagerBean.DataBean.ChildrenBean childrenData, String title){
        super();
        this.data = childrenData;
        this.title = title;

    }
    @Override
    protected View initView() {
        Log.e(TAG,"这是TabFragment1 被初始化了，  TabFragment1.initView ( ) 被执行了");

        View view =  View.inflate(getActivity(), R.layout.callcar_tab_fragment_0,null);
        mMapView =  view.findViewById(R.id.bmapView);
        textView = view.findViewById(R.id.textview);
        return view;
    }

    @Override
    protected void initData() {
        LogUtil.i("这是TabFragment1 数据被初始化了，  TabFragment1.initData ( ) 被执行了 ");
        super.initData();

        mBaiduMap = mMapView.getMap();
        textView.setText(title);
    }
}
