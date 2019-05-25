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
import android.widget.Toast;

import com.tantuo.didicar.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class LicenseOCRActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "OCR-DEMO";
    private Uri fileUri;
    private DigitImageProcessor processor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_recognition_ocr);
        Button recognitionBtn = (Button)this.findViewById(R.id.recognition_btn);
        recognitionBtn.setOnClickListener(this);
        fileUri = (Uri)this.getIntent().getParcelableExtra("PICTURE-URL");
        if(fileUri != null) {
            processor = new DigitImageProcessor();
            displaySelectedImage();
        }
    }

    private void displaySelectedImage() {
        ImageView imageView = (ImageView)this.findViewById(R.id.imageView);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileUri.getPath(), options);
        int w = options.outWidth;
        int h = options.outHeight;
        int inSample = 1;
        if(w > 1000 || h > 1000) {
            while(Math.max(w/inSample, h/inSample) > 1000) {
                inSample *=2;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSample;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath(), options);
//        Mat src = new Mat();
//        Utils.bitmapToMat(bm, src);
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
//        Utils.matToBitmap(gray, bm);
        imageView.setImageBitmap(bm);
//        gray.release();
//        src.release();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "Start to process selected image...");
        Toast.makeText(getApplicationContext(), "OCR", Toast.LENGTH_LONG).show();


        /**
         * 保存驾驶员证件处理的中间结果：二值化的图像
         */
        //首先，找到相机获取的驾驶员证件图片中，证件的位置和证件号码区域的位置，并保存这个过程的中间结果：二值化的图像
        Mat license = processor.findCard(fileUri);
        if(license == null) return;
        Bitmap license_bitmap = Bitmap.createBitmap(license.cols(),license.rows(),Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(license,license, Imgproc.COLOR_BGR2BGRA);
        //openCV的Utils.matToBitmap方法将 binary mat二值图像保存为bitmap图像，用于查看中间结果
        Utils.matToBitmap(license,license_bitmap);
        saveDebugImage(license_bitmap);
        //中间结果方便开发者查找错误，让抽象的识别计算过程更加里程碑化，更加可视化
//--d



        //在驾驶员证件图片中通过processor.findCardNumBlock（）方法找到数字区域
        Mat numImage = processor.findCardNumBlock(license);
        if(numImage == null) return;

        Bitmap number_block_bitmap = Bitmap.createBitmap(numImage.cols(),numImage.rows(),Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(numImage,numImage,Imgproc.COLOR_BGR2BGRA);
        //openCV的Utils.matToBitmap方法将 binary mat二值图像保存为bitmap图像，用于查看中间结果
        Utils.matToBitmap(numImage,number_block_bitmap);
        saveDebugImage(number_block_bitmap);
        //这里是完成识别出证件号码区域得到的结果
//        ImageView imageView = (ImageView)this.findViewById(R.id.imageView);
//        imageView.setImageBitmap(number_block_bitmap);


//        List<Mat> textList = processor.splitNumberBlock(numImage);
//        if (textList != null && textList.size() > 0) {
//            Log.i(TAG, "Number of digits : " + textList.size());
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
        List<Mat> textList = processor.splitNumberBlock(numImage);
        if(textList != null && textList.size() > 0) {
            Log.i(TAG, "Number of digits : " + textList.size());
            int index = 0;
            String cardId = "";
            for(Mat oneText : textList) {
                int digit = processor.recognitionChar(oneText);
                if(digit == 0 || digit == 1) {
                    //
                    float w = oneText.cols();
                    float h = oneText.rows();
                    float rate = w / h;
                    if(rate > 0.5) {
                        digit = 0;
                    } else {
                        digit = 1;
                    }
                }
                cardId += digit;
                oneText.release();
            }
            Log.i("OCR-INFO", "Card Number : " + cardId);
            TextView cardNumberTextView = (TextView)this.findViewById(R.id.textView);
            cardNumberTextView.setText(cardId);
        }
        Log.i(TAG, "Find Card and Card Number Block...");
        Bitmap bitmap = Bitmap.createBitmap(numImage.cols(),numImage.rows(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(numImage, numImage, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(numImage, bitmap);
        saveDebugImage(bitmap);
        ImageView imageView = (ImageView)this.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    private void saveDebugImage(Bitmap bitmap) {
        File filedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myOcrImages");
        String name = String.valueOf(System.currentTimeMillis()) + "_ocr.jpg";
        File tempFile = new File(filedir.getAbsoluteFile()+File.separator, name);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        }catch (IOException ioe) {
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
