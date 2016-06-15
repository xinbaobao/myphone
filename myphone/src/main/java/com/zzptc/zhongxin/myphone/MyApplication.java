package com.zzptc.zhongxin.myphone;

import android.app.Application;

import org.xutils.x;

/**
 * Created by zhongxin on 2016/4/26.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
    }
}
