package com.zzptc.zhongxin.myphone.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zzptc.zhongxin.myphone.R;

public class WelcomeActivity extends Activity {

    private ImageView iv_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
//将欢迎界面的透明度从0.5到1.0  时间为5s
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
        iv_welcome.startAnimation(alphaAnimation);

        //使用代码方式将将欢迎界面的透明度从0.5到1.0  时间为5s
//        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.5f,1.0f);
//        alphaAnimation1.setDuration(5000);
//        alphaAnimation1.setFillAfter(true);
//        iv_welcome.startAnimation(alphaAnimation1);

//给动画添加监听事件   当动画结束时，跳转到主界面并结束当前的界面
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(WelcomeActivity.this, MainUiActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
