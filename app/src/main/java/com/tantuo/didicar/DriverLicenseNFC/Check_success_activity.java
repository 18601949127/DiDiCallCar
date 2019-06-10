package com.tantuo.didicar.DriverLicenseNFC;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tantuo.didicar.Bean.DriverBean;
import com.tantuo.didicar.R;
import com.tantuo.didicar.utils.MyImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class Check_success_activity extends Activity implements OnClickListener, OnMenuItemClickListener {


    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.ib_titlebar_back)
    private ImageView ib_titlebar_back;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_driver_name)
    private TextView tv_driver_name;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_driver_ID)
    private TextView tv_driver_ID;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_driver_info)
    private TextView tv_driver_info;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.iv_driver_profile)
    private ImageView iv_driver_profile;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_car_ID)
    private TextView tv_car_ID;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_car_profile)
    private TextView tv_car_profile;


    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_extra_info2)
    private TextView tv_extra_info2;


    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_extra_info3)
    private TextView tv_extra_info3;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_extra_info4)
    private TextView tv_extra_info4;


    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.tv_result)
    private ImageView tv_result;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.btnPopUpMenu)
    private ImageButton btnPopUpMenu;

    //用xUtils3 声明并XML控件绑定
    @ViewInject(R.id.iv_driver_star)
    private MyImageView iv_driver_star;


    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private DriverBean driverBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_found_activity);
        x.view().inject(Check_success_activity.this);


        Intent intent = getIntent();
        String JsonData = intent.getStringExtra("driverJson");
        driverBean = new Gson().fromJson(JsonData, DriverBean.class);


        initview();



        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);





    }

    private void initview() {
        btnPopUpMenu.setOnClickListener(this);
        ib_titlebar_back.setOnClickListener(this);

        tv_driver_name.setText(driverBean.getDriver_name());
        tv_driver_ID.setText("证件号码：" + driverBean.getDriver_ID());
        tv_driver_info.setText(driverBean.getExtra_info4());
        iv_driver_star.setImageURL(driverBean.getExtra_info1());
        tv_car_ID.setText(driverBean.getCar_ID());
        tv_car_profile.setText(driverBean.getCar_profile());

        tv_extra_info2.setText(driverBean.getExtra_info2());
        tv_extra_info3.setText(driverBean.getExtra_info3());
        tv_extra_info4.setText(driverBean.getExtra_info4());

        playsound(R.raw.positive);
        vibrateMyPhone(500);

    }

    private void vibrateMyPhone(int seconds) {
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(seconds);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 前台分发系统,用于第二次检测NFC标签时该应用有最高的捕获优先权.
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        System.out.println("onResume performed...");

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            finish();

            Log.e("log_tag", "Tag读出成功,TagID:");
        } else {
            Log.e("log_tag", "on new intent Tag读出失败 ACTION_TAG_DISCOVERED ！equals(intent.getAction())),TagID:");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPopUpMenu:
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

                break;
            case R.id.ib_titlebar_back:
                finish();

                break;
            default:
                break;
        }
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


    public void playsound(int sound) {
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


}
