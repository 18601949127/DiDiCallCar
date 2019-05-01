package com.tantuo.didicar.TabFragment;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
public class TabFragment0 extends BaseFragment {

    private static final String TAG = TabFragment0.class.getSimpleName();
    private final String title;
    private final String contents;
    public static TextureMapView mMapView;
    private LocationMode mCurrentMode;
    private BaiduMap mBaiduMap;
    private BottomSheetBehavior<View> sheetBehavior;
    private View rootview;
    BitmapDescriptor mCurrentMarker;
    private EditText tv_start_location;
    private EditText et_destin_location;
    private ImageView iv_bottom_backseat;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;


    public TabFragment0(String title, String contents) {
        super();
        this.title = title;
        this.contents = contents;

    }

    @Override
    public View initView() {
        LogUtil.i("==================================================");
        LogUtil.i("进入类:" + gettitle() + "TabFragment0, 方法:initView()  ");
        Toast.makeText(getActivity(), "tab0 initview()", Toast.LENGTH_SHORT).show();
        rootview = View.inflate(getActivity(), R.layout.callcar_tab_fragment_0, null);
        mMapView = rootview.findViewById(R.id.bmapView);




        initBottomSheet();

        return rootview;
    }

    private void initBottomSheet() {

        View bottomSheetView = rootview.findViewById(R.id.bottomSheetView);

        //获取behavior
        sheetBehavior = BottomSheetBehavior.from(bottomSheetView);

        //设置起始时默认隐藏, 正常默认是折叠BottomSheetBehavior.STATE_COLLAPSED
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //设置sheet在向下拖动时不可以完全隐藏
        sheetBehavior.setHideable(false);

        //设置此底部工作表在展开一次后是否应在隐藏时跳过折叠状态。除非工作表可隐藏，否则将此设置为true无效。
        sheetBehavior.setSkipCollapsed(false);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });

        tv_start_location = rootview.findViewById(R.id.et_start_location);
        et_destin_location = rootview.findViewById(R.id.et_destin_location);
        iv_bottom_backseat = rootview.findViewById(R.id.iv_bottom_backseat);


    }


    @Override
    public void initData() {
        Toast.makeText(getActivity(), "tab0 initData()", Toast.LENGTH_SHORT).show();
        LogUtil.i("==================================================");
        LogUtil.i("进入类:" + gettitle() + "TabFragment0, 方法:initData()  ");
        super.initData();



        mBaiduMap = mMapView.getMap();
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationData(MainActivity.startlocData);
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_pin);
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        mBaiduMap.setMyLocationData(MainActivity.startlocData);

    }

    @Override
    public String gettitle() {
        return title;
    }
}
