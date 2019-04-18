package com.tantuo.didicar.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tantuo.didicar.utils.LogUtil;

public abstract class BaseMenuFragment extends Fragment {

    public Activity context; //得到包裹fragment的activity


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }


    //实现各自的视图，强制方法
    public abstract View initView();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    //请求联网数据，并且将数据绑定到Fragment里面
    protected void initData() {
        LogUtil.i( "进入： 类:BaseMenuFragment -----方法:initData()---- ");

    }

}
