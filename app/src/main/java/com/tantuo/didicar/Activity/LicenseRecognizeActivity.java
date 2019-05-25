package com.tantuo.didicar.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.tantuo.didicar.MainActivity;
import com.tantuo.didicar.R;
import com.tantuo.didicar.utils.LogUtil;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.xutils.view.annotation.ViewInject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LicenseRecognizeActivity extends AppCompatActivity implements View.OnClickListener {

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.iv_image)
    private ImageView iv_image;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.btn_process)
    private Button btn_process;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        setContentView(R.layout.activity_licenserecgnize);
        x.view().inject(LicenseRecognizeActivity.this);

        //初始化OpenCV4android
        iniLoadOpenCV();

        btn_process.setOnClickListener(this);





    }

    private void iniLoadOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if (success) {
            LogUtil.i("==================================================");
            LogUtil.i("进入类:LicenseRecognizeActivity, 方法:iniLoadOpenCV()成功  ");
            Toast.makeText(LicenseRecognizeActivity.this, "iniLoadOpenCV() 成功 ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LicenseRecognizeActivity.this, "load OpenCV 没有成功", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {

        convert2Gray();
    }

    private void convert2Gray() {
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.didisplash);
        Mat src = new Mat();
        Mat gray = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(gray,bitmap);
        iv_image.setImageBitmap(bitmap);


    }
}