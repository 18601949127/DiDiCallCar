package com.tantuo.didicar.DriverLicenseRecognition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tantuo.didicar.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.os.Handler;
import android.view.animation.AlphaAnimation;


public class LicenseOCRActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "OCR-DEMO";
    private Uri fileUri;
    private DigitImageProcessor processor;
    private Mat license;
    private Mat numImage;
    private Bitmap license_bitmap;
    private Button recognitionBtn;
    private AlphaAnimation alphaAnimation;
    private ImageView license_original_imageView;
    private Bitmap original_bitmap;
    private ImageView license_contourimageView;
    private ImageView number_block_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_recognition_ocr);

        recognitionBtn = (Button) this.findViewById(R.id.recognition_btn);
        recognitionBtn.setOnClickListener(this);

        fileUri = (Uri) this.getIntent().getParcelableExtra("PICTURE-URL");
        if (fileUri != null) {
            processor = new DigitImageProcessor();
            displaySelectedImage();

            //透明都动画 1.0f就是完全不透明 越小越透明
            alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(0); //设置动画重复次数
        }
    }

    private void displaySelectedImage() {
        license_original_imageView = (ImageView) this.findViewById(R.id.imageView1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileUri.getPath(), options);
        int w = options.outWidth;
        int h = options.outHeight;
        int inSample = 1;
        if (w > 1000 || h > 1000) {
            while (Math.max(w / inSample, h / inSample) > 1000) {
                inSample *= 2;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSample;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        original_bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
//        Mat src = new Mat();
//        Utils.bitmapToMat(original_bitmap, src);
//        Mat gray = new Mat();
//        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGRA2GRAY);
//        int width = gray.cols();
//        int height = gray.rows();
//        byte[] data = new byte[width*height];
//        gray.get(0, 0, data);
//        for(int row=0; row<height; row++) {
//            for(int col=0; col<width; col++) {
//                data[row*width + col] = (byte)(~data[row*width+col]);
//            }
//        }
//        gray.put(0, 0, data);
//        Utils.matToBitmap(gray, original_bitmap);
        license_original_imageView.setImageBitmap(original_bitmap);
//        gray.release();
//        src.release();
    }

    @Override
    public void onClick(View v) {


        recognitionBtn.setVisibility(View.GONE);

        license_original_imageView.setImageBitmap(original_bitmap);
        alphaAnimation.setDuration(500);
        license_original_imageView.startAnimation(alphaAnimation);


        license = processor.findCardContour(fileUri);
        if (license == null) return;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /**
                 * 延时执行的代码
                 */
                getLicenseContourImage();

            }
        }, 2000); // 延时


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /**
                 * 延时执行的代码
                 */
                getNumDigitContourImage();

            }
        }, 3000); // 延时


        //下面的这一段实在识别到证件号码区域并且得到区分开来的每个号码数字以后，对每个数字的特征进行样本分析，从而识别到每个数字
        // dumpFeature()方法将识别到的每个数字的特征向量保存到手机存储器的"/featurevector/traindata/ 路径下，用来做OpenCV训练的样本
        // 完成对这些特征的训练以后要将下面代码注释掉


//        List<Mat> textList = processor.splitNumberBlock(numImage);
//        if (textList != null && textList.size() > 0) {
//            Log.i(TAG, "Number of digits : " + textList.size());
//            LogUtil.i("==============================  "  + textList.size());
//            Toast.makeText(getApplicationContext(), "number of gigits =" + textList.size(), Toast.LENGTH_SHORT).show();
//
//            int index = 0;
//            String cardId = "";
//
//            for (Mat oneText : textList) {
//
//                float [] vectorData = processor.extractFeatureData(oneText);
//                processor.dumpFeature(vectorData,index++);
//            }
//
//        }

        //在TextImageProcessor.extractFeatuData，我们需要将FeatureData提取出来,用来与号码数字作比对，识别数字
        //之后继续使用二值化方法识别数字区域里的字符轮廓，如果轮廓发现的过程中有字符粘连的话还需要对粘连的字符进行分割
        //如果发现干扰块的话还要对干扰块进行填充，为字符轮廓的特征提取提供好的素材。
        //List<Mat>是识别出来的号码边缘图像组成的集合


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /**
                 * 延时执行的代码
                 */

                List<Mat> textList = processor.splitNumberBlock(numImage);
                if (textList != null && textList.size() > 0) {
                    Log.i(TAG, "Number of digits : " + textList.size());
                    int index = 0;
                    String cardId = "";
                    for (Mat oneText : textList) {
                        int digit = processor.recognitionChar(oneText);
                        if (digit == 0 || digit == 1) {
                            //
                            float w = oneText.cols();
                            float h = oneText.rows();
                            float rate = w / h;
                            if (rate > 0.5) {
                                digit = 0;
                            } else {
                                digit = 1;
                            }
                        }
                        cardId += digit;
                        oneText.release();
                    }
                    Log.i("OCR-INFO", "Card Number : " + cardId);
                    TextView cardNumberTextView = (TextView) LicenseOCRActivity.this.findViewById(R.id.textView);
                    cardNumberTextView.setText("识别出司机证件号码为：" + cardId);
                }
                Log.i(TAG, "Find Card and Card Number Block...");
                Bitmap bitmap = Bitmap.createBitmap(numImage.cols(), numImage.rows(), Bitmap.Config.ARGB_8888);
                Imgproc.cvtColor(numImage, numImage, Imgproc.COLOR_BGR2RGBA);
                Utils.matToBitmap(numImage, bitmap);
                saveDebugImage(bitmap);
                ImageView imageView = (ImageView) LicenseOCRActivity.this.findViewById(R.id.imageView3);
                imageView.setImageBitmap(bitmap);

            }
        }, 4000); // 延时1秒
    }

    private void getNumDigitContourImage() {
        //在驾驶员证件图片中通过processor.findCardNumBlock（）方法找到数字区域
        numImage = processor.findCardNumBlock(license);
        if (numImage == null) return;

        Bitmap number_block_bitmap = Bitmap.createBitmap(numImage.cols(), numImage.rows(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(numImage, numImage, Imgproc.COLOR_BGR2BGRA);
        //openCV的Utils.matToBitmap方法将 binary mat二值图像保存为bitmap图像，用于查看中间结果
        Utils.matToBitmap(numImage, number_block_bitmap);
        saveDebugImage(number_block_bitmap);
        //这里是完成识别出证件号码区域得到的结果
        number_block_imageView = (ImageView) LicenseOCRActivity.this.findViewById(R.id.imageView3);
        number_block_imageView.setImageBitmap(number_block_bitmap);
    }


    /**
     * 保存驾驶员证件处理的中间结果：二值化的图像
     */
    //首先，找到相机获取的驾驶员证件图片中，证件的位置和，并保存这个过程的中间结果：二值化的图像
    private void getLicenseContourImage() {


        Bitmap license_bitmap = Bitmap.createBitmap(license.cols(), license.rows(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(license, license, Imgproc.COLOR_BGR2BGRA);
        //openCV的Utils.matToBitmap方法将 binary mat二值图像保存为bitmap图像，用于查看中间结果
        Utils.matToBitmap(license, license_bitmap);
        saveDebugImage(license_bitmap);
        //中间结果方便开发者查找错误，让抽象的识别计算过程更加里程碑化，更加可视化
        //这里是完成识别出证件区域得到的结果
        license_contourimageView = (ImageView) this.findViewById(R.id.imageView2);
        license_contourimageView.setImageBitmap(license_bitmap);
        alphaAnimation.setDuration(1000); //设置动画执行时间
        license_contourimageView.startAnimation(alphaAnimation);
    }

    private void saveDebugImage(Bitmap bitmap) {
        File filedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myOcrImages");
        String name = String.valueOf(System.currentTimeMillis()) + "_ocr.jpg";
        File tempFile = new File(filedir.getAbsoluteFile() + File.separator, name);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        } catch (IOException ioe) {
            Log.e("DEBUG-ERR", ioe.getMessage());
        } finally {
            try {
                output.flush();
                output.close();
            } catch (IOException e) {
                Log.i("DEBUG-INFO", e.getMessage());
            }
        }
    }
}
