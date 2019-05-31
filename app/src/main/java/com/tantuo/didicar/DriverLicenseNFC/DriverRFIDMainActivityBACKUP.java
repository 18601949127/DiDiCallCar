package com.tantuo.didicar.DriverLicenseNFC;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.tantuo.didicar.R;
import com.tantuo.didicar.utils.NfcUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DriverRFIDMainActivityBACKUP extends AppCompatActivity implements OnClickListener, OnMenuItemClickListener {
    private TextView ifo_NFC, ifo_NFCID;
    private NfcAdapter nfcAdapter;
    private String readResult = "", address = "", province = "", userid = "";
    private String city = "", district = "", street = "", streetNumber = "";
    static public String brand = "", producedDate = "", leaveFactoryDate = "", retailSeller = "", productInformation = "";
    private String producedLocation = "";
    private double longitude, latitude;
    private Float radius;
    static public String CardId = "";
    private PendingIntent pendingIntent;
    private String[][] mTechLists;
    private boolean isFirst = true;
    private Button poiBtn;
    private IntentFilter ndef;
    private IntentFilter[] mFilters;
    public String ip = "";
    private IntentFilter tagDetected;
    int result;
    int returnResult = 0;
    private static final String TAG = "dzt";
    private TextView mText;
    private TextView mTextPoi;
    private LocationClient mLocationClient = null;
    private Button btnBack, btnExit;
    private ImageButton popUpMenu;
    private SoundPool soundPool;
    private Vibrator vibrator;
    private ConnectivityManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rfid_main_activity);
        SysApplication.getInstance().addActivity(this);

        sound(R.raw.positive);

        checkNetworkState();

        checkPhoneNfcsupport();

        //nfc初始化设置
        NfcUtils nfcUtils = new NfcUtils(this);

        init();    //主界面的初始化工作

    }

    private void checkPhoneNfcsupport() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {

            Toast.makeText(DriverRFIDMainActivityBACKUP.this, "设备不支持NFC！", Toast.LENGTH_LONG).show();
            notSupportNFC();


        }

        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            Toast.makeText(DriverRFIDMainActivityBACKUP.this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_LONG).show();
            setOpenNFC();
        }
    }

    public void sound(int sound) {
        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 1);
        soundPool.load(this, sound, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                soundPool.play(1,  //声音id
                        1, //左声道
                        1, //右声道
                        1, //优先级
                        0, // 0表示不循环，-1表示循环播放
                        1);//播放比率，0.5~2，一般为1
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, 1, 0, "公司介绍");
        menu.add(Menu.NONE, 2, 0, "公司介绍");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())//得到被点击的item的itemId
        {
            case 1: //对应的ID就是在add方法中所设定的Id
                Toast.makeText(DriverRFIDMainActivityBACKUP.this, "1", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(DriverRFIDMainActivityBACKUP.this, "2", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }


    private void init() {
        //主界面的写信息按钮


        popUpMenu = (ImageButton) findViewById(R.id.btnPopUp);
        popUpMenu.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btn1);
        btnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();//DriverRFIDMainActivity不可以finish
            }
        });

        btnExit = (Button) findViewById(R.id.btn2);
        btnExit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SoundPool soundPool = new SoundPool(21, AudioManager.STREAM_SYSTEM, 10);
                soundPool.load(DriverRFIDMainActivityBACKUP.this, R.raw.positive, 1);
                soundPool.play(1, 1, 1, 1, 0, 1);
            }
        });
    }

    private boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息  
        manager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接  
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        }

        return flag;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //// 前台分发系统,用于第二次检测NFC标签时该应用有最高的捕获优先权.
        NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
    }


    //	onPause() 对应onResume()
    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
    }

    protected void checkIDTextforResult() throws IOException {
        returnResult = 0;
        /*获取ID 和 Result*/
        String tag_id = CardId;
        if (tag_id == null || tag_id.length() <= 6) {
            Looper.prepare();
            Toast.makeText(DriverRFIDMainActivityBACKUP.this, "checkID函数内发现CardID为空", Toast.LENGTH_LONG).show();
            Looper.loop();
        }

        if (mLocationClient != null && mLocationClient.isStarted())
            mLocationClient.requestLocation();
        else
            Toast.makeText(DriverRFIDMainActivityBACKUP.this, "locationclient 没有打开", Toast.LENGTH_LONG).show();


        String urlstr = "http://139.199.37.235/LBS/check_userid_back_product_info.php";            //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        String params = "uid=" + CardId + '&' + "longitude=" + longitude + '&' + "latitude=" + latitude + '&' + "radius=" + radius + '&' + "province=" + province + '&' + "city=" + city + '&' + "district=" + district + '&' + "street=" + street + '&' + "streetNumber=" + streetNumber + '&' + "address=" + address;
        http.setDoOutput(true);
        http.setRequestMethod("POST");
        OutputStream out = http.getOutputStream();
        out.write(params.getBytes());//post提交参数
        out.flush();
        out.close();


        //读取网页返回的数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));//获得输入流
        String line = "";
        StringBuilder sb = new StringBuilder();//建立输入缓冲区
        while (null != (line = bufferedReader.readLine())) {//结束会读入一个null值
            sb.append(line);//写缓冲区
        }
        String result = sb.toString();//返回结果

        try {
            /*获取服务器返回的JSON数据*/
            JSONObject jsonObject = new JSONObject(result);
            returnResult = jsonObject.getInt("status");//获取JSON数据中status字段值
            userid = jsonObject.getString("userid");
            brand = jsonObject.getString("品牌");
            producedLocation = jsonObject.getString("产地");
            producedDate = jsonObject.getString("生产日期");
            leaveFactoryDate = jsonObject.getString("出厂日期");
            retailSeller = jsonObject.getString("经销商");
            productInformation = jsonObject.getString("产品信息");


        } catch (Exception e) {

            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);



        Toast.makeText(getApplicationContext(),"onNewIntent1...", Toast.LENGTH_LONG).show();

        SoundPool soundPool = new SoundPool(21, AudioManager.STREAM_SYSTEM, 10);
        soundPool.load(this, R.raw.positive, 1);
        soundPool.play(1, 1, 1, 0, 0, 1);


        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        CardId = "not reach to processIntent";
        try {
            CardId= NfcUtils.readNFCId(intent);
            Toast.makeText(getApplicationContext(), "NEW INENT CardID 为：" + CardId, Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Log.e("log_tag", "Tag读出成功,TagID:" + CardId);
            Toast.makeText(getApplicationContext(), "new intent CardID 为：" + CardId, Toast.LENGTH_SHORT).show();
        } else {
            Log.e("log_tag", "on new intent Tag读出失败 ACTION_TAG_DISCOVERED ！equals(intent.getAction())),TagID:" + CardId);

            Toast.makeText(getApplicationContext(), "on new intent Tag读出失败 ACTION_TAG_DISCOVERED ！equals(intent.getAction()：" + CardId, Toast.LENGTH_SHORT).show();
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    checkIDTextforResult();
//                    result = returnResult;
//                    //checkIDTextforResult()为向php服务器提交请求的函数，返回数据类型为int
//                    if (result >= 1) {
//                        Log.e("log_tag", "result=1产品验证成功，感谢您购买正品服装，详情请关注我们的公众号！");
//                        Looper.prepare();
//                        Intent intent = new Intent(DriverRFIDMainActivity.this, Check_success_activity.class);
//                        startActivity(intent);
//
////                             Toast.makeText(DriverRFIDMainActivity.this, userid+"产品验证成功，感谢您购买正品服装，详情请关注我们的公众号", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    } else if (result == -1) {
//                        Log.e("log_tag", "不存在该用户！");
//                        //Toast toast=null;
//                        Looper.prepare();
//
//                        Intent intent = new Intent(DriverRFIDMainActivity.this, Check_failed_activity.class);
//                        startActivity(intent);
//
//                        //                            Toast.makeText(DriverRFIDMainActivity.this, "不存在该用户，产品验证不成功，请到正品商店购买！", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    } else if (result == 0) {
//                        Log.e("log_tag", "不存在该用户！");
//                        //Toast toast=null;
//                        Looper.prepare();
////                             Toast.makeText(DriverRFIDMainActivity.this, "不存在该用户，产品验证不成功，请到正品商店购买！", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }).start();


    }  //onNewIntent结束


    @Override
    public void onClick(View v) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        //显示(这一行代码不要忘记了)
        popup.show();

    }

    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.item1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("公司介绍");
                builder.setMessage("Final Check 防伪查询\n\n妥啦信息技术有限公司\n\n网址：www.tuola.com\n\n合作邮箱 :shulin.tan@paragon-id.com");
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;


            default:
                break;
        }
        return false;
    }

    private void setNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("网络设置提示").setMessage("查询需要连接网络，请连接wifi或者移动网络，是否去设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).show();

    }


    private void setOpenNFC() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("网络设置提示").setMessage("查询需要设置打开手机NFC功能，是否去设置?").setPositiveButton("去设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = null;
                intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).show();

    }


    private void notSupportNFC() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设备不支持提示").setMessage("很遗憾，您的手机不支持NFC功能").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).show();

    }


}
