package com.tantuo.didicar.TabFragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.TextureSupportMapFragment;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.dialogfragment.TabDialogFragment1;
import com.tantuo.didicar.domain.CallCarPagerBean;
import com.tantuo.didicar.utils.LogUtil;

public class TabFragment0 extends BaseFragment {

    private static final String TAG = TabFragment0.class.getSimpleName();
    private  final String title;
    private final String contents;



    public TabFragment0(String title, String contents) {
        super();
        this.title = title;
        this.contents = contents;

    }

    @Override
    public View initView() {
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment0, 方法:initView()  ");
        View view = View.inflate(context, R.layout.callcar_tab_fragment_0, null);


        //获取到SupportMapFragment实例
        TextureSupportMapFragment textureSupportMapFragment = TextureSupportMapFragment.newInstance();
        //将fragment添加至Activity
        getChildFragmentManager().beginTransaction().add(R.id.map_container, textureSupportMapFragment).commit();

        return view;
    }

    @Override
    public void initData() {
        LogUtil.i("==================================================");
        LogUtil.i("进入类:"+gettitle()+"TabFragment0, 方法:initData()  ");
        super.initData();

    }


    @Override
    public String gettitle() {
        return title;
    }
}
