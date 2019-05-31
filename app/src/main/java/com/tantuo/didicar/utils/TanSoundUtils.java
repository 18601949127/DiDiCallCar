package com.tantuo.didicar.utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class TanSoundUtils {

    private Context context;

    public TanSoundUtils(Context context){
        this.context = context;
    }

    public void sound(int sound) {
        this.context = context;
        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 1);
        soundPool.load(context, sound, 1);
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