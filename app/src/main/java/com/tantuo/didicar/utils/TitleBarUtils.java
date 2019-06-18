package com.tantuo.didicar.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.tantuo.didicar.Activity.DiDi_info_Activity;
import com.tantuo.didicar.DriverLicenseNFC.DriverRFIDMainActivity;
import com.tantuo.didicar.DriverLicenseRecognition.LicenseMainActivity;
import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;


/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：WebDetailActivityUtils  webDetailsActivity跳转类
 */
public class TitleBarUtils {


    public static void initTitleBarButton (final Activity activity, View view) {


        ImageButton ib_titlebaback = view.findViewById(R.id.ib_titlebar_back);

        ib_titlebaback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击返回
                    activity.finish();

                }
            });



        }


}