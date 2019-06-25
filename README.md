版权声明：本文为博主原创文章，未经博主允许不得转载。如遇到疑问，评论会给出答复https://blog.csdn.net/weixin_37734988/article/details/92796055
## 前言
这是我自己做的一个仿滴滴打车的Android出行项目，主要针对滴滴等出行平台一直饱受质疑的“人车不符”问题，以及当前越发火热的或计划和出海战略，给出行项目增加了下面几个功能：

 **1.** RFID识别验证功能：在司机证件或者车内识别硬件嵌入RFID识别芯片，乘客使用手机读取到芯片信息，并且通过网络发送到出行平台数据库进行验证（我用JNI加了一个C语言的MD5加密算法对识别到的信息进行了加密）。如果不是合规的“人”或“车”，则不能完成订单并向平台或监管单位汇报当前位置。（为了方便读者测试，可以使用手机读取任何一个加密或非加密RFID芯片，比如银行卡、公交卡等，我在代码中的验证前阶段把芯片信息都换成我自己的司机信息，确保读者测试时可以收到服务器的回复）
 **2.** 海外版功能：点击切换当前语言。
 **3.** 司机证件号码识别功能：读取司机证件上的证件号码，也可以用来与出行平台数据库的接单司机信息进行。
 
 项目源码地址：https://github.com/18601949127 
 项目代码都是一行一行自己敲的，在多部手机上调试过确保各项功能能够顺畅运行。在Git源码中保留了所有的手机CPU指令集架构，保证在所有手机上能够运行成功。觉得包太大的同学可以自己把不需要的  .so 指令集删掉，主要是做识别的 OpenCV4Android.so 包比较大，其次是百度地图的包。

读者如果想到滴滴出行或者其他平台比较实用的功能可以留言或者微信给我（微信：18601949127），我会抽时间把好的功能继续添加到项目里。

本篇先整体作一个项目功能概述，后续会对其中的各个模块做详细的介绍。

## 开发环境

**1.** Android端： Android Studio 版本3.4，百度地图LBS 版本5.3 , OpenCV4Android 版本3.2
**2.**  服务器端：  Apache + PHP + MySQL 用的是我自己租的腾讯云主机做服务器，会一直开放出这个项目的接口，接受并处理读者发来的测试请求。

## 主界面概览

![主界面概览](https://upload-images.jianshu.io/upload_images/18452611-c280b63d6a23d752?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
界面最上面TitleBar 的位置是主要的功能区，除了中间的醒目logo，两侧分布主要功能选项，最左边的SlidingMenu提供侧滑菜单，给乘客个人信息和软件设置提供入口，右边的证件标志按钮用于导向司机证件号码识别功能，再右边的英语标志按钮是国际化语言切换，最右边的无线标志是RFID识别认证功能的入口。

主界面的中间部分是地图区域，可以在上边选择不同交通工具，用于展示乘客所在位置，附近车辆或者POI热点，以及路径规划。

主界面的下方可以提供上划菜单，主要用于上车和目的地地址关键字输入，以及安全提示信息或者广告的入口。

地图区域我使用的是百度地图LBS 版本5.3，海外的话考虑地信息数据多少、性能、包大小、数据源等多方面因素推荐使用 Nutiteq，是一个专心做移动端LBS的SDK。 感兴趣的读者可以看下面的文章：
https://blog.csdn.net/weixin_37734988/article/details/92852349
[Nutiteq 的移动端SDK](https://carto.com/developers/mobile-sdk/guides/getting-started/#android-app)  上面这个前几篇我会抽时间翻译出来给大家看下。



## 项目文件结构

![项目文件结构](https://upload-images.jianshu.io/upload_images/18452611-61f2a3694f638810?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



这里介绍一下项目文件结构，方便读者阅读代码：
包名：com.tantuo.didicar

 - Activity 文件夹：有的Activity 相对独立，并不属于某个功能模块，可以放到这个文件夹。 
 - adapter 文件夹：相对复杂一点的adapter会从类文件中取出单独保存到 adapter文件夹，比如左侧侧滑菜单中 recycler view的adapter。简单一点的adapter还是会保存在调用的类中。
 - Bean 文件夹： 存放Entity 实体类，比如司机的相关信息会包装成一个DriverBean，每个司机都是一个类对象，使用Gson 传递很方便，用的时候get,set 就可以。
 - DriverLicenseNFC 文件夹：RFID识别验证模块，乘客使用这个功能模块验证司机身份或者车辆信息。
 - DriverLicenseRecognition 文件夹：司机证件号码OCR识别的功能模块。
 - splash 文件夹：app 初始化和引导界面。
 - TabFragment：主界面上方的滑动主题条用来切换交通工具或者服务项目（Tab），不同的交通工具或者服务项目代码都保存在TabFragment 文件夹里。
 - utils 文件夹： 用来保存项目用到的各种工具类，比如DriverRouteOverlay 用来在地图上渲染规划出来的驾车路线，MD5JniUtils 用NDK调用MD5加密算法，保护RFID芯片信息，NfcUtils 用来管理手机的NFC功能，POIOverlay 用来在地图上渲染周围兴趣点(POI)。
    把工具类从Activity 或者 Fragment 中extract 出来放到统一的utils 文件夹，会让你的代码更清晰，可读性更强。

## 引导界面
先看下真机上的效果：
![引导界面](https://upload-images.jianshu.io/upload_images/18452611-6d79842f91b41b59.gif?imageMogr2/auto-orient/strip)


引导界面最初的logo动画是用我自己用SVG矢量动画做的，路径规划描述在 drawable 的splash_logo.xml 文件里：
 
![路径规划描述](https://upload-images.jianshu.io/upload_images/18452611-1e13b18723288b36?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
还需要资源文件里的animator文件夹下的didi_logo_animator.xml 对路径进行动画描述：

```java
<objectAnimator
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="3000"
    android:propertyName="trimPathEnd"
    android:repeatCount="0"
    android:valueFrom="0"
    android:valueTo="1"
    android:valueType="floatType"/>
```

这几秒的时间里可以在下图的位置添加一些初始化代码，比如网络请求，得到后续Activity的素材，地理位置等等。

![初始化代码位置](https://upload-images.jianshu.io/upload_images/18452611-fa0c014f0af97321?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

    
## 出行界面
通过滑动地图界面上方的主题可以切换不同的项目界面。
滑动主题条是一个VIewPager的 Indicator，每一个主题对应一个下面的服务项目，放在各自独立的VIewPager里。每个服务项目有各自独立的上划菜单，作为此服务对应的地址关键字输入或者相关信息入口。
![出行界面](https://upload-images.jianshu.io/upload_images/18452611-41fc4b44ff7e38b8.gif?imageMogr2/auto-orient/strip)
出行界面的UI结构：
![出行界面的UI结构](https://upload-images.jianshu.io/upload_images/18452611-e479d8b8291170dc?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意：乘客的位置信息、当前经纬度、当前街道名字、楼宇名字都是在MainAcitivity做为静态成员变量定义的，原因是在别的Acitivity或者类中，这些变量需要经常使用，直接调用 MainActivity.CurrentLocation就可以了，后面用到的所有当前位置，都是在MainActivity中 MyLocationListener 类得到的。

![MainAcitivity](https://upload-images.jianshu.io/upload_images/18452611-c1d0944336ee6d0a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


## 上车地址和目的地址的路线规划
![路线规划功能](https://upload-images.jianshu.io/upload_images/18452611-11845e872d555efe.gif?imageMogr2/auto-orient/strip)
不同交通工具（快车，出租车，单车，公交车等等）对应的服务项目都嵌在TItleBar下边的 VIewpager里，一个服务项目对应一个独立的Fragmen文件，由其顶部的的VIewpagerIndicator滑动切换。
服务项目的主要代码在com.tantuo.didicar 包下 TabFragment 文件夹里。

## 底部上滑动菜单
![底部上滑动菜单](https://upload-images.jianshu.io/upload_images/18452611-6166dba9a7dcf24a.gif?imageMogr2/auto-orient/strip)
buttonsheet是在布局文件中加入android.support.v4.widget.NestedScrollView类的 `app:layout_behavior="@string/bottom_sheet_behavior"`
![在这里插入图片描述](https://upload-images.jianshu.io/upload_images/18452611-898db9fb4a1a271a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 左侧侧滑菜单
左侧侧滑菜单可以作为个人信息、安全提示、设置信息的入口
![左侧侧滑菜单](https://upload-images.jianshu.io/upload_images/18452611-fa8bfb68cb88352c.gif?imageMogr2/auto-orient/strip)

## 司机证件的号码OCR识别功能
证件号码识别功能的主要代码在com.tantuo.didicar 包下的 DriverLicenseRecognition 模块。

![证件号码识别代码](https://upload-images.jianshu.io/upload_images/18452611-737e3aee907123aa?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

还是先看下真机效果：
![司机证件的号码识别功能](https://upload-images.jianshu.io/upload_images/18452611-d163589532fdec77.gif?imageMogr2/auto-orient/strip)

点击进入司机证件号码识别功能以后，可以选择对证件拍照，为了方便演示，这里是从手机相册选择刚刚拍的照片。同时为了方便读者测试这个功能，我把照片保存在了开发包的asset文件夹里面，这样读者下载我保存在GIthub上https://github.com/18601949127  的版本，点击选择司机证件以后调用的是我保存在assets 文件夹里的司机证件照片，也就是下面图片里的 getDriverLicenseFromMySample() 方法，可以立刻进行测试。想继续从手机相册读取的读者可以执行LicenseMainActivity 下的 LicenseMainActivity 方法。
![调用司机证件](https://upload-images.jianshu.io/upload_images/18452611-b1823888643ca385?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意：在程序中，想要在运行中读取司机证件照片，要把照片保存在assets 文件夹下面，使用AssetManager 类读取，而不能试图调取drawable 文件夹下面的照片，因为 \res 文件夹下的资源文件都会被编译到apk里面去，并同时赋予资源 id。感兴趣的同学可以看下代码里面的 copyFilesFassets（）方法。

这里用我以前在国外读书时候的证件作为例子：
![司机证件照片](https://upload-images.jianshu.io/upload_images/18452611-a0481f79f8238b5e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

 1. 首先：要从照片中找到司机证件区域，也就是上证件边缘红色的区域

```java
    /**
     * 找到图像中的证件区域
     * 在RGB色彩空间求取驾驶员证件的图像梯度，之后在此图像上做二值化，从而通过轮廓（contour）发现与面积大小过滤得到证件区域
     * author:Tantuo 86-1860194917
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
```

第一步先调用Imgproc.Scharr（）方法对司机证件的原始照片进行Scharr梯度运算，所谓梯度运算就是对图像中的像素点进行导数运算，从而得到相邻两个像素点的差异值，像素差异大的地方就是图像内轮廓contour，第二步在此图像上做二值化Binarization，调用 Imgproc.morphologyEx（）方法，通过轮廓（contour）发现与面积大小过滤得到证件区域。

边缘发现以后调用Imgproc.cvtColor()方法得到下面的证件区域：
![识别出的证件区域](https://upload-images.jianshu.io/upload_images/18452611-a4b4dba07a9c8406?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
2.识别到证件区域以后我们注意到证件左上角有一个比较醒目的矩形，我们用它作为reference识别到照片下方包含数字的号码区域。在程序中这个过程调用下面的 findCardNumBlock(Mat card) 方法。

```java
    public Mat findCardNumBlock(Mat card) {
        //首先初始化HSV色彩空间（H：hue色相,S:saturation饱和度,V:value明度，亮度）
        Mat hsv = new Mat();
        Mat binary = new Mat();

        //从RGB色彩空间转换到hsv色彩空间，使用openCV的 Imgproc函数：Imgproc.COLOR_BGR2HSV
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
                numberROI.x = 3* roi.x + 120;
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
```
同样还是先把图像从RGB色彩空间转换成HSV色彩空间，调用OpenCV的 Imgproc类生成一个 Imgproc.COLOR_BGR2HSV 对象。
```java
Imgproc.cvtColor(card, hsv, Imgproc.COLOR_BGR2HSV);
```
之后调用下面的Core.inRange()方法得到一个hsv颜色在new Scalar(30, 40, 45)范围内的区域，也就是左上角的reference 矩形。

```java
Core.inRange(hsv, new Scalar(30, 40, 45), new Scalar(180, 255, 255), binary);
```
下一步还是形态学操作去噪声。噪声就是二值化图像里面识别出来的一个个小的黑点，形态学的开操作（morphology excution）会把图像中这些小小的黑点用旁边的大区域颜色覆盖掉，目的是为了让处理后的图像更加容易被机器识别。
比如下面的代码调用OpenCV的Imgproc.morphologyEx()方法可以把大小为 5*5的结构元素（噪声）用周边像素弥补掉

```java
        //下面对二值话图像进行形态学的开操作（morphology excution）,去除小的 5*5大小的结构元素（噪声）
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_OPEN, kernel);//去除噪声

```
噪声处理以后开始寻找证件区域内的号码区域 Contour做轮廓发现操作：

```java
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
                numberROI.x = 3* roi.x + 120;
                numberROI.y = roi.y + 4 * roi.height - 65;
                numberROI.width = binary.cols() - roi.x - 390;
                numberROI.height = (int) (roi.height * 0.9);
                break;
            }
        }
```
得到证件号码的区域以后就可以截取下来保存到 textimage

```java
    Mat textImage = new Mat();
    card.submat(numberROI).copyTo(textImage);
```
拷贝完成以后记得释放release mat资源
```java
        card.release();
        hsv.release();
        binary.release();
        return textImage;
```
完成以上工作以后可以识别到证件号码区域的矩形轮廓：
![识别出的证件号码区域](https://upload-images.jianshu.io/upload_images/18452611-a85bedd31fbc1bd3.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
识别出了证件中的号码区域，后面就调用 DigitImageProcessor 类对这些数字进行识别，这个过程需要我单独在另外一篇文章介绍，下面仅仅对几个重要方法的功能作介绍：

 

 - splitNumberBlock(Mat textImage)方法使用二值化方法识别数字区域里的字符轮廓。
 - getSplitLinePos(Mat mtexts) 方法用来对图像中有两个数字粘结起来的情况做分离。
 - extractFeatureData(Mat txtImage) 方法的作用是证件卡号识别的特征提取，获取卡号每个数字的黑色像素点特征，作为每个号码的特征和识别的重要依据。
 - dumpFeature(float[] fv, int textNum) 方法将生成的特征文本文件保存在手机。
 - readFeatureVector(File f) 用来读取保存的特征向量。
 - recognitionChar(Mat charImage) 用来根据特征向量对证件号码进行识别。




## RFID识别验证功能
RIFD识别验证功能的主要代码在com.tantuo.didicar 包下的DriverLIcenseNFC模块里：

![RFID识别验证功能代码](https://upload-images.jianshu.io/upload_images/18452611-06961112c00c91f1?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

还是先看下真机效果：
![RFID识别验证功能](https://upload-images.jianshu.io/upload_images/18452611-21215f566ddfba02.gif?imageMogr2/auto-orient/strip)
点击右上角的RFID验证入口以后，会提示乘客使用手机背面像刷公交卡那样感应RFID硬件，比如嵌入芯片的司机证件、固定在车上识别器等。

![使用手机感应RFID芯片](https://upload-images.jianshu.io/upload_images/18452611-377f8e88eb40530c.gif?imageMogr2/auto-orient/strip)
注意：某个Activity 要想能够在当前栈顶接收RFID芯片号码，必须在 Manifest.xml 文件中设置intent-filter 拦截TAG_DISCOVERED的Action，这样这个Activity 才能捕获RFID标签信息。并且设置LunchMode 为SingleTop，确保再次捕获RFID标签信息（TAG_DISCOVERED）的时候，始终由处于栈顶的这个Activity 来处理，而不是把他压入栈，调取新的DriverRFIDMainActivity作栈顶。
有疑惑的同学可以看下 Activity 启动模式和栈管理的相关文章。
[彻底明白Activity启动模式-SingleTop、SingleTask、SingleInstance具体使用场景](https://blog.csdn.net/weixin_37734988/article/details/93508139)
![ Manifest.xml 文件中的设置](https://upload-images.jianshu.io/upload_images/18452611-66baa2fb226b225a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
考虑到用户的手机可能有多个APP或者Activity 可以拦截RFID或者 NFC 芯片信息，所以需要给处于当前栈顶的 DriverRFIDMainActivity 设置前台分发系统 enableForegroundDispatch ，可以确保检测到RFID标签时拥有最高的捕获优先权，而不是由Android Activity 调度机制调出新的有拦截权限的活动。

```java
    @Override
    protected void onResume() {
        super.onResume();
        // 前台分发系统,用于确保检测RFID标签时拥有最高的捕获优先权.
        NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
    }

    
    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
    }
```

手机读取芯片ID这个功能的代码我单独放到NfcUtils工具类里，在utils 文件夹下。
![NfcUtils工具类](https://upload-images.jianshu.io/upload_images/18452611-a7298a891414504a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
手机读取到芯片信息，会调用NDK编译成C语言的MD5加密算法so 文件（文章最后会讲），连同当时的地理位置经纬度一起发送给平台服务器，与数据库中注册司机的信息进行比对，并将验证结果和司机信息发送给乘客：
![服务器端返回的验证结果](https://upload-images.jianshu.io/upload_images/18452611-7b00cd31e15fc3a4?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
服务器端用的是我自己的腾讯云主机 + Apache + PHP+  MySQL ， 会一直开放出这个项目的网络接口并持续维护，方便读者测试这个功能。读者只要在验证环节使用手机读取任何一个嵌有RFID加密芯片比如学生证、银行卡、公交卡，程序在发送数据请求之前（下图代码中第二行高亮的部分）都会把读取到的ID信息换成作者本人的，再发送给平台服务器服务器做验证，这样读者测试时使用手机读取任何RFID信息都会接收到从服务器发回来的司机信息。实际项目中把这一行注释掉即可。

![发送验证信息前的代码](https://upload-images.jianshu.io/upload_images/18452611-f8f61dabaa361cde?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
服务器端收到乘客发送过来的验证请求以后，会对比平台司机数据库进行核实，并把核实结果和对应司机、车辆信息发回给乘客。
下面就是平台服务器端注册司机的注册信息数据库，我用Navicat  做了部分截图，第一行红色部分就是平台验证的结果，也就是作者本人的信息。
![服务器端的司机信息数据库](https://upload-images.jianshu.io/upload_images/18452611-730e5b24ad2e8b58?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
服务器端还会对乘客发送过来的数据进行整理和分析，也可以将“人车不符”数据和位置信息发送给合规部门。
![“人车不符”](https://upload-images.jianshu.io/upload_images/18452611-5cf26f8b533e097a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
下图是“人车不符”情况发生的地区热力图：
![“人车不符”发生频率热力图](https://upload-images.jianshu.io/upload_images/18452611-77eb963cede7b982?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
还可以根据乘客的叫车时间，筛选出高峰时段的用车需求热力图，给司机调度部门提供数据支持。 
![用车需求热力图](https://upload-images.jianshu.io/upload_images/18452611-ca9a41fb5991a53d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
对服务器端的打车数据进行分析，还可以生成非常漂亮的24小时动态热力图、星云图、蝌蚪迁移图，感兴趣的读者可以研究下Python 、Pandas 、MatplotLib，可以快捷地处理服务器端数据，生成可视化图表。

## 使用NDK调用MD5加密算法
前面提到项目中会把ID号码使用C语言的MD5算法进行加密，关键代码在下图中的cpp 文件夹。
图中 NDK Components 组件提供了一整套编译C语言动态库（*.so ）和打包的工具，可以把* *.so 动态库打包到apk中。
下面的MD5.h 和 MD5.cpp 文件分别是C语言写的算法类头文件和源文件。头文件用来声明源文件要用到的变量、类型、宏定义，源文件则用来描述方法和具体实现，里面会有一个`#include "MD5.h"` 把头文件导入进来。两者的关系有点像书的目录和内容的关系，目录是对章节和内容进行简单表示，真正的实现实在书里面的。
![NDK相关文件](https://upload-images.jianshu.io/upload_images/18452611-31886e847b764e2e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
上图中MD5 C语言文件下面还有一个native-lib.cpp 文件，是NDK 在 Android studio 里帮助我们生成的。
下面的图可以看到 native-lib 是如何帮助 MD5JniUtils 类的 getMd5 () 方法调用 C语言加密方法的，JNIEXPORT 和 JNICALL 两个宏用来标识函数用途是调用.so 库，就好像 C++可以调用 .dll 动态链接库一样，后面紧跟的是函数名，命名规则很重要：Java_ + 包名 + 调用这个加密算法的Java工具类名 + Java调用方法 ，后面的变量参数是Java中String类型对应的JNI jstring类型，下面在方法体中，就可以使用对传入的加密前字符串进行加密的C语言运算了，并把加密完成的 jstring类型结果返回给java层。
![native-lib.cpp 文件](https://upload-images.jianshu.io/upload_images/18452611-bec041f1119755ec?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 结语
整个项目就大概介绍完了，欢迎读者转载和提问，我看到会尽快回复，如果读者能想到更加实用的功能，我也会尽快更新GitHub上的源码，增加新的功能。

项目源码地址：https://github.com/18601949127
我的CSDN博客：https://blog.csdn.net/weixin_37734988
简书：https://www.jianshu.com/u/0e0374fbf110


