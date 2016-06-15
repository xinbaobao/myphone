package com.zzptc.zhongxin.myphone.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

public class MobileReceiver extends BroadcastReceiver {

    private long mobileOFFTime = 0;
    private long mobileONTime = 0;

    private int mobileCount = 0;

    public MobileReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_SCREEN_OFF)){
            mobileOFFTime = System.currentTimeMillis();
        }
        if(action.equals(Intent.ACTION_SCREEN_ON)){
            mobileONTime = System.currentTimeMillis();
        }

        //当亮屏时间减去黑屏时间大于1s时，添加次数
        if(mobileONTime - mobileOFFTime < 1000){
            mobileCount++;

            //如果次数恒等于4时，先清空次数，再手机震动，然后百度定位，发送求救短信
            if(mobileCount == 4){
                mobileCount = 0;

                //手机震动
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);


                //百度定位，发送短信
                context.startService(new Intent(context,LocationService.class));

            }
        }
    }
}
