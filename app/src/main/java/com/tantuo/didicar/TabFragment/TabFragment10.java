package com.tantuo.didicar.TabFragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.tantuo.didicar.Activity.DiDi_info_Activity;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.base.BaseFragment;
import com.tantuo.didicar.utils.DrivingRouteOverlay;
import com.tantuo.didicar.utils.LogUtil;
import com.tantuo.didicar.utils.PoiOverlay;
import com.tantuo.didicar.utils.WebDetailActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class TabFragment10 extends BaseFragment implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener, View.OnClickListener {


    private NestedScrollView bottomSheetView;
    private ImageView iv_bottom_sheet_item1;
    private ImageView iv_bottom_sheet_item2;
    private ImageView iv_bottom_sheet_item3;
    private ImageView iv_bottom_sheet_item4;
    private ImageView iv_bottom_sheet_item5;
    private ImageView iv_bottom_sheet_item6;
    private ImageView iv_bottom_sheet_item7;
    private ImageView iv_bottom_sheet_item8;
    private ImageView floating_safety_center;
    private android.support.design.widget.FloatingActionButton floating_locate_center;
    private String iv_bottom_sheet_item_url1 = "https://xmall.xiaojukeji.com/imall/index.htm?xmallsource=1002&sidechanne=3002&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=3012&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.394839795143554&lng=116.8487379582826&model=HWI-AL00&os=9&phone=W471piXc0R0glRFq7nvDow==&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&time=1560572808586&uid=281867467423745&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553&trip_country=CN&location_country=CN&TripCountry=CN&trip_cityId=1&trip_cityid=1&location_cityid=1&utc_offset=480&maptype=soso&origin_id=1&terminal_id=1&source=weixin_source&role=1&shared=tru";
    private String iv_bottom_sheet_item_url2 = "https://dpubstatic.udache.com/static/dpubimg/dpub2_project_187481/index_187481.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&flat=40.39293&flng=116.84192&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.392355381081394&lng=116.8424214994192&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow&pid=1_xID-B2_hV&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560742235707&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&";
    private String iv_bottom_sheet_item_url3 = "https://dpubstatic.udache.com/static/dpubimg/76f185ec7e0a18a60935cf2673c1020f/index.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.3949025796631&lng=116.84876880903553&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow&pid=1_2hJ3RccxA&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560760307676&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553&from=singlemess";
    private String iv_bottom_sheet_item_url4 = "https://dpubstatic.udache.com/static/dpubimg/76f185ec7e0a18a60935cf2673c1020f/index.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.3979418911976&lng=116.8437601867656&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow&pid=1_m8AD23H4n&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560759621077&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553";
    private String iv_bottom_sheet_item_url5 = "https://dpubstatic.udache.com/static/dpubimg/b6d6d1436f5094959a4289a4deace69c/index.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.3949025796631&lng=116.84876880903553&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow&pid=1_26PHFy8uc&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560760439334&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553";
    private String iv_bottom_sheet_item_url6 = "https://page.xiaojukeji.com/market/ddPage_0CQ3KMow.html?access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.39493203251291&lng=116.84898101505226&model=HWI-AL00&os=9&phone=W471piXc0R0glRFq7nvDow==&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&time=1560740676668&uid=281867467423745&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&vcode=553&trip_country=CN&location_country=CN&TripCountry=CN&trip_cityId=1&trip_cityid=1&location_cityid=1&utc_offset=480&maptype=soso&origin_id=1&terminal_id=1&stm=2%257C4ad7bc9d-0a78-464c-8f45-6f3cf3c66fa9%257C4ad7bc9d-0a78-464c-8f45-6f3cf3c66fa9";
    private String iv_bottom_sheet_item_url7 = "https://page.xiaojukeji.com/m/collage.html?openid=oDe7ajrFd0t6KdDKIYjjiCdTd2WA&acctoken=de14351d8744e998f37627267f2c5f4b&needuserinfo=0#/";
    private String floating_safety_center_url = "https://dpubstatic.udache.com/static/dpubimg/dpub2_project_187481/index_187481.html?TripCountry=CN&access_key_id=2&appid=10000&appversion=5.2.52&area=%E5%8C%97%E4%BA%AC%E5%B8%82&channel=780&city_id=1&cityid=1&datatype=1&deviceid=6cd1d3832da36056681ad4ed7ade2155&dviceid=6cd1d3832da36056681ad4ed7ade2155&flat=40.39293&flng=116.84192&imei=868227037142403854C78AD10B66380C8F28CC6327C3788&lang=zh-CN&lat=40.392355381081394&lng=116.8424214994192&location_cityid=1&location_country=CN&maptype=soso&model=HWI-AL00&origin_id=1&os=9&phone=W471piXc0R0glRFq7nvDow&pid=1_xID-B2_hV&platform=1&susig=e4f80d8df39b46ae679cb58d721db&suuid=A1702CD0DD1175EDF286DE35369DF4CA_780&terminal_id=1&time=1560742235707&trip_cityId=1&trip_cityid=1&trip_country=CN&uid=281867467423745&utc_offset=480&uuid=A0AF094F9D975FBBAE7AD129E96CF26F&";


    private static final String TAG = TabFragment10.class.getSimpleName();
    private final String title;
    private final String contents;
    public MapView mMapView;
    private LocationMode mCurrentMode;
    public static BaiduMap mBaiduMap;
    private BottomSheetBehavior<View> sheetBehavior;
    private View rootview;
    BitmapDescriptor mCurrentMarker;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private String currenyCity;

    //Poi搜索成员变量和地址自动补全成员变量
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private AutoCompleteTextView start_text_view = null;
    private AutoCompleteTextView destin_text_view = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int loadIndex = 0;
    private int searchType = 0;  // 搜索的类型，在显示时区分
    private String Flag_Tocken;
    private String start_str;
    private String destin_str;
    private Intent intent;
    private String iv_bottom_sheet_item1_url;
    private MapStatus.Builder builder;

    public TabFragment10(String title, String contents) {
        super();
        this.title = title;
        this.contents = contents;

    }

    @Override
    public View initView() {

        rootview = View.inflate(getActivity(), R.layout.callcar_tab_fragment_0, null);
        LogUtil.i("进入类: " + getClass().getSimpleName() + "  交通工具为：" + gettitle() + ",  方法:initView()  ");

        mMapView = rootview.findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();


        findview();

        initSearchLocation();

        initBottomSheet();

        return rootview;


    }


    private void findview() {
        bottomSheetView = (NestedScrollView) rootview.findViewById(R.id.bottomSheetView);

        start_text_view = (AutoCompleteTextView) rootview.findViewById(R.id.tv_start_location);
        destin_text_view = (AutoCompleteTextView) rootview.findViewById(R.id.tv_destin_location);
        floating_safety_center = rootview.findViewById(R.id.floating_safety_center);
        floating_locate_center = rootview.findViewById(R.id.floating_locate_center);


        if (MainActivity.currentBuilding != null) {
            start_text_view.setHint("从哪里出发？ " + MainActivity.currentStreet + MainActivity.currentBuilding);
        } else {
            start_text_view.setHint("从哪里出发？ " + MainActivity.currentStreet);
        }


        iv_bottom_sheet_item1 = (ImageView) rootview.findViewById(R.id.iv_bottom_sheet_item1);
        iv_bottom_sheet_item2 = (ImageView) rootview.findViewById(R.id.iv_bottom_sheet_item2);
        iv_bottom_sheet_item3 = (ImageView) rootview.findViewById(R.id.iv_bottom_sheet_item3);
        iv_bottom_sheet_item4 = (ImageView) rootview.findViewById(R.id.iv_bottom_sheet_item4);
        iv_bottom_sheet_item5 = (ImageView) rootview.findViewById(R.id.iv_bottom_sheet_item5);
        iv_bottom_sheet_item6 = (ImageView) rootview.findViewById(R.id.iv_bottom_sheet_item6);
        iv_bottom_sheet_item7 = (ImageView) rootview.findViewById(R.id.iv_bottom_sheet_item7);

        iv_bottom_sheet_item1.setOnClickListener(this);
        iv_bottom_sheet_item2.setOnClickListener(this);
        iv_bottom_sheet_item3.setOnClickListener(this);
        iv_bottom_sheet_item4.setOnClickListener(this);
        iv_bottom_sheet_item5.setOnClickListener(this);
        iv_bottom_sheet_item6.setOnClickListener(this);
        iv_bottom_sheet_item7.setOnClickListener(this);
        floating_safety_center.setOnClickListener(this);
        floating_locate_center.setOnClickListener(this);


    }

    private void initSearchLocation() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);


        sugAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line);
        start_text_view.setAdapter(sugAdapter);
        start_text_view.setThreshold(1);

        destin_text_view.setAdapter(sugAdapter);
        destin_text_view.setThreshold(1);


        /* 当输入关键字变化时，动态更新地址建议列表 */
        start_text_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Flag_Tocken = "start_text_view";
                if (cs.length() <= 0) {
                    return;
                }


                /* 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString())
                        .city(MainActivity.CurrentCity));

                MainActivity.HideKeyboard(rootview);

                start_str = start_text_view.getText().toString();

                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        //搜索的城市
                        .city(MainActivity.CurrentCity)
                        //提示页每页显示字符串的行数
                        .pageCapacity(20)
                        //autocompleteTextView输入的字符串
                        .keyword(start_str)
                        //分页的编号，表示显示提示地址的第几页
                        .pageNum(0)
                        //scope的值为1表示返回基本信息，2表示返回POI详细信息
                        .scope(1));
            }
        });

        /* 当输入关键字变化时，动态更新建议列表 */
        destin_text_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }


            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Flag_Tocken = "destin_text_view";
                LogUtil.i("进入类: " + getClass().getSimpleName() + " 方法:onTextChanged() destin_text_view ");

                if (cs.length() <= 0) {
                    return;
                }



                /* 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString())
                        .city(MainActivity.CurrentCity));
                MainActivity.imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                destin_str = destin_text_view.getText().toString();


                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        //搜索的城市
                        .city(MainActivity.CurrentCity)
                        //提示页每页显示字符串的行数
                        .pageCapacity(20)
                        //autocompleteTextView输入的字符串
                        .keyword(destin_str)
                        //分页的编号，表示显示提示地址的第几页
                        .pageNum(0)
                        //scope的值为1表示返回基本信息，2表示返回POI详细信息
                        .scope(1));

            }
        });
    }


    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param result Poi检索结果，包括城市检索，周边检索，区域检索
     */
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            LogUtil.i("进入类:" + getClass().getSimpleName() + "  方法:onGetPoiResult() 未找到结果 ");

            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {


            mBaiduMap.clear();

            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();

            builder = new MapStatus.Builder();
            PoiInfo poi1 = result.getAllPoi().get(0);

            switch (Flag_Tocken) {
                case "initData":

                    builder.target(MainActivity.startll).zoom(15.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()), 2000);

                    break;
                case "start_text_view":
                    //overlay.zoomToSpan();

                    builder.target(poi1.getLocation()).zoom(16.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()), 2000);

                    break;
                case "destin_text_view":

                    RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
                    mSearch = RoutePlanSearch.newInstance();

                    mSearch.setOnGetRoutePlanResultListener(listener);
                    PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", start_str);
                    PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", destin_str);

                    //  注意：在DrivingRoutePlanOption()内可以设置以下几个检索策略常量
                    //  ECAR_TIME_FIRST 驾乘检索策略常量：时间优先
                    //  ECAR_FEE_FIRST  驾乘检索策略常量：较少费用
                    //  ECAR_DIS_FIRST  驾乘检索策略常量：最短距离

                    mSearch.drivingSearch((new DrivingRoutePlanOption())
                            .from(stNode)
                            .to(enNode));

                    builder.target(poi1.getLocation()).zoom(15.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()), 2000);
                    overlay.zoomToSpan();

                    MainActivity.HideKeyboard(rootview);

                    break;
                default:
                    break;

            }


            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";

            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }

            strInfo += "找到结果";
            LogUtil.i("进入类:TabFragment0, 方法:onGetPoiResult()" + strInfo);
        }
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
    }


    @Override
    public void initData() {

        Flag_Tocken = "initData";
        LogUtil.i("进入类: " + getClass().getSimpleName() + "  交通工具为：" + gettitle() + ",  方法:initData()  ");
        super.initData();

        mBaiduMap.clear();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setBuildingsEnabled(true);
        mBaiduMap.setCompassEnable(true);


        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_pin1);
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, null));
        mBaiduMap.setMyLocationData(MainActivity.startlocData);


        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption()
                //搜索附近的出租车，这里在每个路口都模拟一个

                .keyword("路")
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(MainActivity.startll)
                .radius(2000)
                //分页的编号，表示显示提示地址的第几页
                .pageNum(0)
                //提示页每页显示字符串的行数
                .pageCapacity(10)
                //scope的值为1表示返回基本信息，2表示返回POI详细信息
                .scope(1);

        mPoiSearch.searchNearby(nearbySearchOption);


    }

    @Override
    public String gettitle() {
        return title;
    }


    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * V5.2.0版本之后，还方法废弃，使用{@link #onGetPoiDetailResult(PoiDetailSearchResult)}代替
     *
     * @param result POI详情检索结果
     */
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            LogUtil.i("进入类:TabFragment0, 方法:onGetPoiDetailResult() 抱歉，未找到结果 ");
        } else {
            LogUtil.i("进入类:TabFragment0, 方法:onGetPoiDetailResult()  " + result.getName() + ": " + result.getAddress());
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            LogUtil.i("进入类:TabFragment0, 方法:onGetPoiDetailResult() 抱歉，未找到结果 ");
        } else {
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                LogUtil.i("进入类:TabFragment0, 方法:onGetPoiDetailResult() 抱歉，检索结果为空 ");
                return;
            }

            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
                if (null != poiDetailInfo) {

                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     *
     * @param res Sug检索结果
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }

        List<String> suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }

        sugAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,
                suggest);
        start_text_view.setAdapter(sugAdapter);
        destin_text_view.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        start_text_view.setText("");
        destin_text_view.setText("");
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.floating_safety_center:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), floating_safety_center_url);
                break;

            case R.id.floating_locate_center:

                builder.target(MainActivity.startll).zoom(15.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()), 2000);
                break;

            case R.id.iv_bottom_sheet_item1:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), iv_bottom_sheet_item_url1);
                break;

            case R.id.iv_bottom_sheet_item2:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), iv_bottom_sheet_item_url2);
                break;

            case R.id.iv_bottom_sheet_item3:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), iv_bottom_sheet_item_url3);
                break;

            case R.id.iv_bottom_sheet_item4:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), iv_bottom_sheet_item_url4);
                break;

            case R.id.iv_bottom_sheet_item5:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), iv_bottom_sheet_item_url5);
                break;

            case R.id.iv_bottom_sheet_item6:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), iv_bottom_sheet_item_url6);
                break;

            case R.id.iv_bottom_sheet_item7:

                WebDetailActivityUtils.start_DiDi_info_Activity(this.getActivity(), iv_bottom_sheet_item_url7);
                break;


        }
    }

    private void start_DiDi_info_Activity(String url) {
        intent = new Intent(context, DiDi_info_Activity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


    private class MyPoiOverlay extends PoiOverlay {
        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            // }
            return true;
        }

    }

    /**
     * 对周边检索的范围进行绘制
     *
     * @param center 周边检索中心点坐标
     * @param radius 周边检索半径，单位米
     */
    public void showNearbyArea(LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        mBaiduMap.addOverlay(ooMarker);

        OverlayOptions ooCircle = new CircleOptions().fillColor(0xCCCCCC00)
                .center(center)
                .stroke(new Stroke(1, 0xFFFF00FF))
                .radius(100);

        mBaiduMap.addOverlay(ooCircle);
    }


    /**
     * 选择不同交通工具的线路规划部分
     */
    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }


        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                LogUtil.i("进入类:TabFragment0, 方法:onGetDrivingRouteResult() 抱歉，未找到结果 ");

            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                result.getSuggestAddrInfo();
                LogUtil.i("进入类:TabFragment0, 方法:onGetDrivingRouteResult() 起终点或途经点地址有岐义 ");

                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                if (result.getRouteLines().size() > 0) {
                    //获取路径规划数据,(以返回的第一条路线为例）
                    //为DrivingRouteOverlay实例设置数据
                    overlay.setData(result.getRouteLines().get(0));
                    //在地图上绘制DrivingRouteOverlay
                    overlay.addToMap();
                    overlay.zoomToSpan();
                } else {
                    LogUtil.i("进入类:TabFragment0, 方法:onGetDrivingRouteResult() 结果数<0 ");
                    return;
                }

            }
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };
}
