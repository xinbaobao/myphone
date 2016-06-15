package com.zzptc.zhongxin.myphone.utils;

import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by zhongxin on 2016/5/13.
 */
public class MessageUtils {

    public static void sendMessage(String phone,String message){
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> sms = smsManager.divideMessage(message);
        for(String s: sms){
            smsManager.sendTextMessage(phone,null,s,null,null);
        }
    }


    public static String conver(long memory){

        long kb  = 1024;
        long mb = 1024 * kb;
        long gb = 1024 * mb;
        if(memory > 0){
            if(memory > gb){
                float size = (float)memory / gb;
                return String.format("%.1f GB",size);
            }else if(memory > mb){
                float size = (float)memory / mb;
                return String.format("%.1f MB",size);
            }else if(memory > kb){
                float size = (float)memory / kb;
                return String .format("%.1f KB",size);
            }else {
                return String.format("%.1f B",memory);
            }
        }else{
            return "0 B";
        }

    }

}
