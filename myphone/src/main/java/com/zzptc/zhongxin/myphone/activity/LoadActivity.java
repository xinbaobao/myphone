package com.zzptc.zhongxin.myphone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.UpdateInfo;
import com.zzptc.zhongxin.myphone.contant.Contants;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * windowNoTitle  没有标题
 * windowBackground   去除黑色背景
 * windowIsTranslucent   将activity对话框设置为透明
 * windowCloseOnTouchOu tside    设置外部点击不可取消
 */



public class LoadActivity extends Activity {

    private ImageView iv_red;
    private ImageView iv_yellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//去除标题  必须放到setContentView之前
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_load);

        iv_red = (ImageView) findViewById(R.id.iv_red);
        iv_yellow = (ImageView) findViewById(R.id.iv_yellow);

        //给两个图片设置伸缩动画
        //动画集：多个图片同时或者连续播放  set   xml文件
        final Animation red_animation = AnimationUtils.loadAnimation(this,R.anim.load_anim);
        final Animation yellow_animation = AnimationUtils.loadAnimation(this,R.anim.load_anim);
        iv_red.startAnimation(red_animation);


        boolean flag = checkNetStatus();
        if(flag){
            //有网络，连接服务器更新
            RequestParams requestParams = new RequestParams("http://10.0.2.2:8080/UpdateServer/servlet/UpdateServlet");
            x.http().get(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    //访问成功后获取服务器上的json数据，然后进行解析并判断版本号，当服务器上的版本号大于客户端的版本号时显示返回码
                    UpdateInfo updateinfo = new Gson().fromJson(result, UpdateInfo.class);

                    try {
                        int serverVersionCode = updateinfo.getVersionCode();
                        int clientVersionCode = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS).versionCode;
                        if(serverVersionCode>clientVersionCode){
                            Intent data = new Intent();
                            data.putExtra("info",updateinfo);

                            setResult(Contants.NEED_UPDATE,data);

                            finish();
                        }else{
                            setResult(Contants.NOT_NEED_UPDATE);
                            finish();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    setResult(Contants.NET_EXCEPTION);
                    finish();

                }

                @Override
                public void onCancelled(CancelledException cex) {
                    setResult(Contants.NET_CANCEL);
                    finish();

                }

                @Override
                public void onFinished() {

                }
            });

        }else{
            //因为是activity对话框，所以应返回usercenteractivity再弹出对话框，告诉用户没有网络
            setResult(Contants.NET_INTERRUPT);
            finish();
        }

        red_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv_yellow.setVisibility(View.INVISIBLE);//红色动画播放时隐藏黄色动画
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_yellow.setVisibility(View.VISIBLE);//红色动画停止时不隐藏黄色动画
                iv_yellow.startAnimation(yellow_animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        yellow_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_red.startAnimation(red_animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private boolean checkNetStatus(){
        //得到网络连接的管理类
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //得到所有可用的网络
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //判断是否有网络
        if(info != null){
            return info.isAvailable();
        }
        return false;
    }

}
