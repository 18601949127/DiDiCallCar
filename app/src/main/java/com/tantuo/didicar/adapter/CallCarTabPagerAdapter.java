package com.tantuo.didicar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tantuo.didicar.base.BaseFragment;

import java.util.ArrayList;

public class CallCarTabPagerAdapter extends FragmentPagerAdapter {


    private final ArrayList<BaseFragment> fragments;

    public CallCarTabPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).gettitle();
    }

}

