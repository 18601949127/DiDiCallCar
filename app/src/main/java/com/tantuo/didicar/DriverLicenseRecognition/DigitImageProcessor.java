package com.tantuo.didicar.DriverLicenseRecognition;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：DigitImageProcessor
 */

public class DigitImageProcessor {
    public static final String FEATURE_FILE_PATH = Environment.getExternalStorageDirectory() + "/featurevector/traindata/";

    //根据特征向量对证件号码进行识别
    public int recognitionChar(Mat charImage) {
        String result = "";
        File file = new File(FEATURE_FILE_PATH);
        //遍历FEATURE_FILE_PATH 下的特征文件
        File[] featureDataFiles = file.listFiles();
        double minDistance = Double.MAX_VALUE;
        //获取到特征数据
        float[] fv = extractFeatureData(charImage);

        for (File f : featureDataFiles) {
            //readFeatureVector 方法计算与目标特征的最小距离
            double dist = calculateDistance(fv, readFeatureVector(f));
            if (minDistance > dist) {
                minDistance = dist;
                result = f.getName();
            }
        }
        Log.i("OCR-INFO", result);
        return Integer.parseInt(result.substring(0, 1));
    }


    /**
     * calculateDistance 计算证件号码特征与样本特征的距离
     * 我们将距离定义为 （向量差的平方）的总和，如果差平方和为0的话则是完全匹配
     *
     * @param v1
     * @param v2
     * @return
     */
    private double calculateDistance(float[] v1, float[] v2) {
        double x = 0, y = 0, z = 0, zf = 0;
        for (int i = 0; i < 40; i++) {
            x = v1[i];
            y = v2[i];
            z = x - y;
            z *= z;
            zf += z;
        }
        return zf;
    }


    /**
     * vectordata是四十个特征向量，前面二十个是20个cell的黑点分布特征，20到40分别是x轴横向y轴纵向方向上的直方图映射projection特征
     * readFeatureVector 读取证件号码的这四十个特征
     *
     * @param
     * @return
     */
    private float[] readFeatureVector(File f) {
        float[] fv = new float[40];
        try {
            //vectordata是四十个特征向量，前面二十个是20个cell的黑点分布特征，20到40分别是x轴横向y轴纵向方向上的直方图映射projection特征
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                for (int i = 0; i < 40; i++) {
                    float currVal = Float.parseFloat(br.readLine());
                    fv[i] = currVal;
                }
            }
        } catch (IOException ioe) {

            Log.i("IO-ERROR", ioe.getMessage());
        }
        return fv;
    }

    /**
     * 把生成的特征文本文件保存到手机里
     *
     * @param fv
     * @param textNum
     */
    public void dumpFeature(float[] fv, int textNum) {
        try {
            File file = new File(FEATURE_FILE_PATH + File.separator + "feature_" + textNum + ".txt");
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                for (int k = 0; k < fv.length; k++) {
                    pw.println(String.valueOf(fv[k]));
                }
                pw.close();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);

        }
    }

    /**
     * 证件卡号识别的特征提取，获取卡号每个数字的黑色像素点特征，作为每个号码的特征和识别的重要依据
     * 具体做法是：1。将号码图像分成五行四列，得到20个小方格（cells）,然后计算每个cell里面的像素总和，作为0-10每个号码的特征
     * 2。直方图影像特征：将x,y轴方向分成10个bin,计算不同的数字号码在每个bin x和y方向的像素投影 （直方图）
     *
     * @param txtImage
     * @return 返回特征数据：vectordata
     */
    public float[] extractFeatureData(Mat txtImage) {
        //首先计算特征，获取总的黑色像素点，作为每个号码的特征
        float[] vectordata = new float[40];
        Arrays.fill(vectordata, 0);
        //首先得到号码图像的行和列
        float width = txtImage.cols();
        float height = txtImage.rows();
        //号码图像里有黑点即使1，无像素点就是0
        byte[] data = new byte[(int) (width * height)];
        txtImage.get(0, 0, data);


        //将号码图像分成五行四列，得到20个小方格（cells）,然后计算每个cell里面的像素总和，作为0-10每个号码的特征
        float xstep = width / 4.0f;
        float ystep = height / 5.0f;
        int index = 0;

        //计算20个cells的像素总和
        for (float y = 0; y < height; y += ystep) {
            for (float x = 0; x < width; x += xstep) {
                //getWeightBlackNumber方法得到黑色像素点个数
                vectordata[index] = getWeightBlackNumber(data, width, height, x, y, xstep, ystep);
                index++;
            }
        }

        //计算x，y方向上的直方图影射特征（projection）
        //为了减少计算量，将x,y轴方向分成10个小bins
        //vectordata是四十个特征向量，前面二十个是20个cell的黑点分布特征，20到40分别是x轴横向y轴纵向方向上的直方图映射projection特征

        float bins = 10.0f;

        xstep = width / bins;
        for (float x = 0; x < width; x += xstep) {
            if ((xstep + x) - width > 1)
                continue;
            vectordata[index] = getWeightBlackNumber(data, width, height, x, 0, xstep, height);
            index++;

        }


        ystep = height / bins;
        for (float y = 0; y < height; y += ystep) {
            if ((y + ystep) - height > 1) continue;
            vectordata[index] = getWeightBlackNumber(data, width, height, 0, y, width, ystep);
            index++;
        }

        //给20个cell的特征向量做归一化补全normalization
        float sum = 0;


        for (int i = 0; i < 20; i++) {
            sum += vectordata[i];
        }


        for (int i = 0; i < 20; i++) {
            //讲前面二十个cell归一化到0到1之间
            vectordata[i] = vectordata[i] / sum;
        }

        // Y轴方向扫描
        sum = 0;
        for (int i = 20; i < 30; i++) {
            sum += vectordata[i];
        }

        for (int i = 20; i < 30; i++) {
            vectordata[i] = vectordata[i] / sum;
        }

        // X轴方向扫描
        sum = 0;
        for (int i = 30; i < 40; i++) {
            sum += vectordata[i];
        }
        for (int i = 30; i < 40; i++) {
            vectordata[i] = vectordata[i] / sum;
        }

        //返回特征数据
        return vectordata;
    }


    //得到黑色像素点的总数
    private float getWeightBlackNumber(byte[] data, float width, float height, float x, float y, float xstep, float ystep) {
        float weightNum = 0;

        //startX是证件号码在XY轴方向上的起始位置
        //Math.floor方法将浮点数转换成int类型整数的部分
        int startX = (int) Math.floor(x);
        int startY = (int) Math.floor(y);

        //将原来的浮点数减去整数部分就得到了小数部分 author:Tantuo
        float fx = x - startX;
        float fy = y - startY;

        //证件号码数字在XY轴上的结束位置就是起始值加上step字符长宽
        float endX = x + xstep;
        float endY = y + ystep;
        if (endX > width) {
            endX = width - 1;
        }
        if (endY > height) {
            endY = height - 1;
        }

        // 同样，宽高整数部分
        int digitWidth = (int) Math.floor(endX);
        int digitHeight = (int) Math.floor(endY);

        // 小数部分
        float fw = endX - digitWidth;
        float fh = endY - digitHeight;

        // 统计黑色像素个数
        int c = 0;
        int ww = (int) width;
        float weight = 0;
        int row = 0;
        int col = 0;
        for (row = startY; row < digitHeight; row++) {
            for (col = startX; col < digitWidth; col++) {
                c = data[row * ww + col] & 0xff;
                //c == 0 就是黑色的像素
                if (c == 0) {
                    weight++;
                }
            }
        }

        // 前边的起始位置和宽高都有小数部分，我们把这四个小数的部分W1-W4以权重的方式计算黑色像素点的和
        // 计算小数部分黑色像素权重加和
        // w1:
        float w1 = 0, w2 = 0, w3 = 0, w4 = 0;
        // calculate w1:x轴起始位置的小数部分权重
        if (fx > 0) {
            col = startX + 1;
            if (col > width - 1) {
                col = col - 1;
            }
            float count = 0;
            for (row = startY; row < digitHeight; row++) {
                c = data[row * ww + col] & 0xff;
                if (c == 0) {
                    count++;
                }
            }
            w1 = count * fx;
        }

        // calculate w2：y轴起始位置的小数部分权重
        if (fy > 0) {
            row = startY + 1;
            if (row > height - 1) {
                row = row - 1;
            }
            float count = 0;
            for (col = startX; col < digitWidth; col++) {
                c = data[row * ww + col] & 0xff;
                if (c == 0) {
                    count++;
                }
            }
            w2 = count * fy;
        }

        // calculate w3：宽度的小数部分权重
        if (fw > 0) {
            col = digitWidth + 1;
            if (col > width - 1) {
                col = col - 1;
            }
            float count = 0;
            for (row = startY; row < digitHeight; row++) {
                c = data[row * ww + col] & 0xff;
                if (c == 0) {
                    count++;
                }
            }
            w3 = count * fw;
        }


        // calculate w4：高度的小数部分权重
        if (fh > 0) {
            row = digitHeight + 1;
            if (row > height - 1) {
                row = row - 1;
            }
            float count = 0;
            for (col = startX; col < digitWidth; col++) {
                c = data[row * ww + col] & 0xff;
                if (c == 0) {
                    count++;
                }
            }
            w4 = count * fh;
        }
        //weight权重总数，前边多算的要减去，后面少算的要加上
        weightNum = (weight - w1 - w2 + w3 + w4);
        if (weightNum < 0) {
            weightNum = 0;
        }
        return weightNum;
    }

    //使用二值化方法识别数字区域里的字符轮廓，为了减少计算量，下面首先对图像进行灰度和二值化操作
    public List<Mat> splitNumberBlock(Mat textImage) {
        List<Mat> numberImgs = new ArrayList<>();
        Mat gray = new Mat();
        Mat binary = new Mat();

        // 首先用Imgproc.COLOR_BGR2GRAY方法对图像进行灰度操作，做法和之前寻找驾驶员证件区域和证件号码区域做法相同
        Imgproc.cvtColor(textImage, gray, Imgproc.COLOR_BGR2GRAY);

        // 自动阈值二值化操作并且OTSU阈值法去噪声
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        //下面对二值图像进行形态学（morphology excution）的去噪声操作，先得到大小为 3*3像素的结构元素（StructuringElement）
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        //然后对结构元素进行 Imgproc.MORPH_CLOSE闭操作，去掉号码数字字符图像上面的毛刺字符
        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_CLOSE, kernel);

        //为了得到更加清晰的二值化图片，要继续寻找二值化图片里的噪声干扰点，并用将其填充
        //这里将面积小于200，和高度小于字符区域三分之一的噪声干扰点去掉
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Mat contourBin = binary.clone();
        Core.bitwise_not(contourBin, contourBin);
        Imgproc.findContours(contourBin, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        int minh = binary.rows() / 3;
        for (int i = 0; i < contours.size(); i++) {
            Rect roi = Imgproc.boundingRect(contours.get(i));
            double area = Imgproc.contourArea(contours.get(i));
            if (area < 200) {
                //太小的（面积小于200）noise噪声点直接填充掉，给-1
                Imgproc.drawContours(binary, contours, i, new Scalar(255), -1);
                continue;
            }
            if (roi.height <= minh) {
                //高度没有字符区高度三分之一的肯定是noise噪声点直接填充掉，给-1
                Imgproc.drawContours(binary, contours, i, new Scalar(255), -1);
                continue;
            }
        }

        //下面开始获取证件号码的字符轮廓得到外接矩形，也就是分割号码的各个数字
        contours.clear();
        binary.copyTo(contourBin);
        //bitwise_not方法换成黑底白字，以便更好地发现轮廓 findContours
        Core.bitwise_not(contourBin, contourBin);
        //轮廓发现
        Imgproc.findContours(contourBin, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        //设置每个号码的外接矩形（textBoxes）大小，于找到的contours大小一致
        Rect[] textBoxes = new Rect[contours.size()];
        int index = 0;
        int minWidth = 1000000;
        Mat contoursImage = new Mat(contourBin.size(), CvType.CV_8UC1);
        //初始化一个CV_8UC1 type的空白图像 contoursImage，用来显示下面的每个号码的轮廓
        contoursImage.setTo(new Scalar(255));

        //获取每个数字的外接矩形
        for (int i = 0; i < contours.size(); i++) {
            Rect bounding = Imgproc.boundingRect(contours.get(i));
            minWidth = Math.min(bounding.width, minWidth);
            textBoxes[index++] = bounding;
            Imgproc.drawContours(contoursImage, contours, i, new Scalar(0), 1);
        }
        int double_minWidth = minWidth * 2;

        //得到各个号码的contours集合以后，还要根据每个号码contour的x轴坐标排序，也就是从左到右的顺序给号码contours排序
        for (int i = 0; i < textBoxes.length - 1; i++) {
            for (int j = i + 1; j < textBoxes.length; j++) {
                //根据每个号码contour的x轴坐标排序,如果哪个号码contour的 x坐标偏大，则交换排到右边，依次类推循环
                if (textBoxes[i].x > textBoxes[j].x) {
                    Rect temp = textBoxes[j];
                    textBoxes[j] = textBoxes[i];
                    textBoxes[i] = temp;
                }
            }
        }

        //号码外接矩形有顺序了以后，找出其中高度和宽度异常的矩形
        for (int k = 0; k < textBoxes.length; k++) {
            //minh是号码证件号码截取区域高度的三分之一（见之前上面的定义 int minh = binary.rows() / 3）
            //号码外接矩形的高度如果小于号码区域三分之一，则被认定为黑点噪声干扰，忽略掉
            if (textBoxes[k].height <= minh)
                continue;
            //double_minWidth 是上面最小证件号码外接矩形宽度的两倍，如果截取的矩形宽度大于double_minWidth，则认定有黑点噪声造成了字符粘结
            //并调用getSplitLinePos（）方法拆分粘结的外接矩形，将其拆分成两个单独的号码

            if (textBoxes[k].width > double_minWidth) {
                Mat twoText = new Mat();
                contoursImage.submat(textBoxes[k]).copyTo(twoText);
                int xpos = getSplitLinePos(binary.submat(textBoxes[k]));
                //调用getSplitLinePos（）方法拆分粘结的外接矩形，拆分之后第一个号码是从粘结图像的起点0，0到xps-1
                numberImgs.add(twoText.submat(new Rect(0, 0, xpos - 1, textBoxes[k].height)));
                //第二个号码的x轴位置，拆分出来
                numberImgs.add(twoText.submat(new Rect(xpos + 1, 0, textBoxes[k].width - xpos - 1, textBoxes[k].height)));
            } else {
                Mat oneText = new Mat();
                contoursImage.submat(textBoxes[k]).copyTo(oneText);
                numberImgs.add(oneText);
            }
        }
        //还是一样记得释放内存，mat格式图像用 release()
        textBoxes = null;
        binary.release();
        gray.release();
        contourBin.release();
        contoursImage.release();
        return numberImgs;
    }


    /**
     * 发现号码粘结以后调用getSplitLinePos(）方法拆分成两个单独的号码
     * by:author Tantuo, 具体思路是将两个字符粘结的图像的中间部分（这里是两个字符x轴中间值左右10个像素的区域）做竖列方向的像素扫描
     * 竖列方向像素最少的列，被认为是粘结的那部分，并将这一列的左右拆分为两个单独的号码
     *
     * @param mtexts
     * @return
     */
    private int getSplitLinePos(Mat mtexts) {
        //使用openCV的mtexts.get(0, 0, data)方法获取粘结图像的所有像素空间
        int middle_x = mtexts.cols() / 2;//
        int width = mtexts.cols();
        int height = mtexts.rows();
        byte[] data = new byte[width * height];
        mtexts.get(0, 0, data);

        //两个字符x轴中间值左右10个像素的区域作为列扫面的部分
        int scan_colum_startx = middle_x - 10;
        int scan_colum_endx = middle_x + 10;
        int linepos = middle_x;
        //假设最小像素为1000000
        int min_pix = 1000000;
        int c = 0;
        //x轴方向从startx开始扫描，一直扫描到endx
        for (int col = scan_colum_startx; col <= scan_colum_endx; col++) {
            int total = 0;
            //y轴方向从上到下扫描像素点,total是扫描这一列的像素总数，像素点最少的那一列就是粘连最窄处
            for (int row = 0; row < height; row++) {
                c = data[row * width + col] & 0xff;
                if (c == 0) {
                    total++;
                }
            }

            //像素点最少的那一列就是粘连最窄处
            if (total < linepos) {
                linepos = total;
                linepos = col;
            }
        }

        //返回粘结最窄处的x轴位置
        return linepos;
    }

    public Mat findCardNumBlock(Mat card) {
        //首先初始化HSV色彩空间（H：hue色相,S:saturation饱和度,V:value明度，亮度）
        Mat hsv = new Mat();
        Mat binary = new Mat();

        //从RGB色彩空间转换到hsv色彩空间，使用openCV的 Imgproc类：Imgproc.COLOR_BGR2HSV
        Imgproc.cvtColor(card, hsv, Imgproc.COLOR_BGR2HSV);
        //inRange函数将hsv彩色图片的根阈值进行过滤,用来过滤掉对识别左上角标志区域帮助不大的颜色
        //并且把滤出的图像保存到 binary里面
        // Scalar（）是具有三个参数的结构体，三个参数代表 hsv的色相，饱和度，亮度值

        Core.inRange(hsv, new Scalar(30, 40, 45), new Scalar(180, 255, 255), binary);

        //以上会得到一个驾驶员证件的二值化图像，但是噪声比较多
        //下面对二值话图像进行形态学的开操作（morphology excution）,去除小的 5*5大小的结构元素（噪声）
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_OPEN, kernel);//去除噪声


        //获取证件标志的轮廓（contours）
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        int offsetx = binary.cols() / 3;
        int offsety = binary.rows() / 3;
        Rect numberROI = new Rect();

        //对每个ROI寻找外接矩形 contourArea
        for (int i = 0; i < contours.size(); i++) {
            Rect roi = Imgproc.boundingRect(contours.get(i));

            //对于识别出来的矩形区域如果太小（面积小于200像素）则忽略
            if (Imgproc.contourArea(contours.get(i)) < 200) {
                continue;
            }
            //找到标志区域以后，以标志区域为基准，证件号码的位置在标志x坐标 *2 左右，宽度大概在 binary.cols() - roi.x - 100像素
            //证件号码的高度大概是证件标志（基准）的0.7倍 height*0.7 ;
            //如果找到的左上角标志物的轮廓长宽都小于证件的三分之一，则以此标志物作为标准定为号码区域
            if (roi.x < offsetx && roi.y < offsety) {
                numberROI.x = 3 * roi.x + 120;
                numberROI.y = roi.y + 4 * roi.height - 65;
                numberROI.width = binary.cols() - roi.x - 390;
                numberROI.height = (int) (roi.height * 0.9);
                break;
            }
        }

        //如果没有找到就返回null
        if (numberROI.width == 0 || numberROI.height == 0) {
            return null;
        }

        //得到证件号码的区域以后就可以截取下来保存到 textimage
        Mat textImage = new Mat();
        card.submat(numberROI).copyTo(textImage);
        //拷贝完成以后记得释放release mat资源
        card.release();
        hsv.release();
        binary.release();
        return textImage;
    }

    /**
     * 找到图像中的证件区域
     * 在RGB色彩空间求取驾驶员证件的图像梯度，之后在此图像上做二值化，从而通过轮廓（contour）发现与面积大小过滤得到证件区域
     * author:Tantuo 86-1860194917
     *
     * @param fileUri
     * @return
     */
    public Mat findLicenseContour(Uri fileUri) {

        //首先使用openCV 的 Imgcodecs类得到相机获取的证件图片
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if (src.empty()) {
            return null;
        }

        //得到证件照片的x梯度和y梯度
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();

        //注意求梯度的时候我们使用的是Scharr算法，sofia算法容易收到图像细节的干扰
        //所谓梯度运算就是对图像中的像素点进行就导数运算，从而得到相邻两个像素点的差异值 by:Tantuo
        Imgproc.Scharr(src, grad_x, CvType.CV_32F, 1, 0);
        Imgproc.Scharr(src, grad_y, CvType.CV_32F, 0, 1);
        //openCV中有32位浮点数的CvType用于保存可能是负值的像素数据值
        Core.convertScaleAbs(grad_x, abs_grad_x);
        Core.convertScaleAbs(grad_y, abs_grad_y);
        //openCV中使用release()释放Mat类图像，使用recycle()释放BitMap类图像
        grad_x.release();
        grad_y.release();


        //使用openCV的Core.addWeighted方法将x梯度和y梯度合并成一个梯度图像
        Mat grad = new Mat();
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);
        abs_grad_x.release();
        abs_grad_y.release();

        //得到梯度图像以后将其二值化，以便更清晰地找到轮廓边缘
        //Imgproc.cvtColor方法将梯度图像转换成binary gray灰度图像
        Mat binary = new Mat();
        Imgproc.cvtColor(grad, binary, Imgproc.COLOR_BGR2GRAY);
        //手动阈值化，threshould阈值定为40
        Imgproc.threshold(binary, binary, 40, 255, Imgproc.THRESH_BINARY);
        grad.release();

        //下面对二值图像进行形态学（morphology excution）的去噪声操作，先得到大小为 3*3像素的结构元素
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        //然后对结构元素进行 Morph_open开操作。 腐蚀：去除噪声-膨胀：覆盖去除的噪声点
        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_OPEN, kernel);


        //接下来使用openCV的Imgproc.findContours()方法，在图像中寻找驾驶员证件的轮廓 contour roi
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        int hw = binary.cols() / 2;
        int hh = binary.rows() / 2;
        Rect roi = new Rect();
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            //如果发现某一个 roi兴趣区域的轮廓宽度超过图片的一半，即可以认为这个轮廓是驾驶员证件的轮廓 contour
            if (rect.width > hw) {
                roi.x = rect.x;
                roi.y = rect.y;
                roi.width = rect.width;
                roi.height = rect.height;
                break;
            }
        }

        //没找到就返回
        if (roi.width == 0 || roi.height == 0) {
            return null;
        }

        //找到证件轮廓区域就将其拷贝到card图片中
        Mat card = new Mat();
        src.submat(roi).copyTo(card);

        //拷贝完成以后记得释放资源0
        src.release();
        binary.release();
        return card;
    }

}
