package com.tantuo.didicar.dialogfragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tantuo.didicar.R;
import com.tantuo.didicar.utils.AnimationUtils;

public class TabDialogFragment1 extends DialogFragment {
        View mView;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView=inflater.inflate(R.layout.dialog,container,false);
            return mView;
        }

        @Override
        public void onStart() {
            super.onStart();
            Window window = getDialog().getWindow();
            //设置dialog相应属性
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
            //必须设定的属性，否则无法使dialog铺满屏幕，设置其他颜色会出现黑边
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            AnimationUtils.slideToUp(mView);
        }


    }