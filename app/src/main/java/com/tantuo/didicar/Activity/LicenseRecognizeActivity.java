package com.tantuo.didicar.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.utils.LogUtil;

import org.opencv.android.OpenCVLoader;

public class LicenseRecognizeActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        setContentView(R.layout.activity_licenserecgnize);


        iniLoadOpenCV();



    }

    private void iniLoadOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if( success ){
            LogUtil.i("==================================================");
            LogUtil.i("iniLoadOpenCV() 成功 ");
            Toast.makeText(LicenseRecognizeActivity.this, "iniLoadOpenCV() 成功 ", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(LicenseRecognizeActivity.this, "load OpenCV 没有成功", Toast.LENGTH_SHORT).show();
        }
    }

}