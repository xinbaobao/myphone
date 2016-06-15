package com.zzptc.zhongxin.myphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zzptc.zhongxin.myphone.activity.SplashActivity;
import com.zzptc.zhongxin.myphone.activity.WelcomeActivity;
import com.zzptc.zhongxin.myphone.bean.Contact;
import com.zzptc.zhongxin.myphone.utils.ReadContactsUtils;

import org.xutils.x;

import java.util.List;

public class MainActivity extends Activity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//共享首选项                                         xml的名称   模式为私有的
        sharedPreferences = getSharedPreferences("panduan",MODE_PRIVATE);

        // 默认用户第一次进入应用时为true                xml节点的名称  默认值为true
        boolean isFirst = sharedPreferences.getBoolean("isFirst",true);
        if(isFirst){

            //当用户第一次进入应用时将isFirst的默认值改为false并提交
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("isFirst",false);
//            editor.commit();

            sharedPreferences.edit().putBoolean("isFirst",false).commit();

            startActivity(new Intent(this, SplashActivity.class));
        }else {
            startActivity(new Intent(this, WelcomeActivity.class));
        }
        finish();


        //开启一个线程拷贝数据
        x.task().run(new Runnable() {
            @Override
            public void run() {
                ReadContactsUtils.copyDatabase(MainActivity.this);

//                List<Contact> contacts = ReadContactsUtils.queryAllContacts(MainActivity.this);
//                for(Contact c:contacts){
//                    System.out.println(c);
//
//                }
                List<Contact> contacts = ReadContactsUtils.getContacts();
                if(contacts == null || contacts.size() <= 0){
                    contacts = ReadContactsUtils.queryAllContacts(MainActivity.this);
                    ReadContactsUtils.setContacts(contacts);
                }
            }
        });


    }

}



