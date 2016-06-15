package com.zzptc.zhongxin.myphone.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.recriver.MobileReceiver;
import com.zzptc.zhongxin.myphone.view.DanceWageTimer;
import com.zzptc.zhongxin.myphone.view.RatingBar;
import com.zzptc.zhongxin.myphone.view.RatingView;

import java.util.Random;

public class MainUiActivity extends Activity implements View.OnClickListener {

    private Toolbar toolbars;
    private TextView tv_up;

    private TextView tv_rate;
    private RatingView rv_rate;
    private RatingBar sec_bar,flu_bar,clean_bar;

    private int sec_rate = 1+ new Random().nextInt(10);
    private int flu_rate = 1+ new Random().nextInt(10);
    private int clean_rate = 1+ new Random().nextInt(10);

    private int total_wage = 0;//总时长

    private RelativeLayout rl_rate;
    private RelativeLayout rl_down;
    private TextView tv_family;
    private TextView tv_speedup;

    private MobileReceiver mobileReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);

        toolbars = (Toolbar) findViewById(R.id.toolbars);
        tv_up = (TextView) findViewById(R.id.tv_up);

        tv_rate = (TextView) findViewById(R.id.tv_rate);
        rv_rate = (RatingView) findViewById(R.id.rv_rate);

        rl_rate = (RelativeLayout) findViewById(R.id.rl_rate);
        rl_down = (RelativeLayout) findViewById(R.id.rl_down);
        tv_family = (TextView) findViewById(R.id.tv_family);
        tv_speedup = (TextView) findViewById(R.id.tv_speedup);


        toolbars.inflateMenu(R.menu.main_menu);
        toolbars.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(MainUiActivity.this, "你点击了我", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainUiActivity.this,UserCenterActivity.class));
                overridePendingTransition(R.anim.user_center_anim, 0);
                return false;
            }
        });

        tv_up.setOnClickListener(this);
        tv_family.setOnClickListener(this);
        tv_speedup.setOnClickListener(this);


        //中间数据的变化
        total_wage = (int) (sec_rate * 0.5 + flu_rate * 0.3 + clean_rate * 0.2) * 10;
        DanceWageTimer danceWageTimer = new DanceWageTimer(DanceWageTimer.getTotalExecuteTime(total_wage,50),50,tv_rate,total_wage);
        danceWageTimer.start();

        //打分的控件
//        sec_bar = new RatingBar(sec_rate,"安全度高");
//        flu_bar = new RatingBar(flu_rate,"流畅度高");
//        clean_bar = new RatingBar(clean_rate,"清洁度高");
//        >=8高  >=4中  >=0差
        if(sec_rate>=8){//            当前分数   标题
            sec_bar = new RatingBar(sec_rate,"安全度高");
        }else if(sec_rate>=4 & sec_rate<8){
            sec_bar = new RatingBar(sec_rate,"安全度中");
        }else if(sec_rate>=0 & sec_rate<4){
            sec_bar = new RatingBar(sec_rate,"安全度差");
        }

        if(flu_rate>=8){
            flu_bar = new RatingBar(flu_rate,"流畅度高");
        }else if(flu_rate>=4 & flu_rate<8){
            flu_bar = new RatingBar(flu_rate,"流畅度中");
        }else if(flu_rate>=0 & flu_rate<4){
            flu_bar = new RatingBar(flu_rate,"流畅度差");
        }

        if(clean_rate>=8){
            clean_bar = new RatingBar(clean_rate,"清洁度高");
        }else if(clean_rate>=4 & clean_rate<8){
            clean_bar = new RatingBar(clean_rate,"清洁度中");
        }else if(clean_rate>=0 & clean_rate<4){
            clean_bar = new RatingBar(clean_rate,"清洁度差");
        }

        rv_rate.addRatingBar(sec_bar);
        rv_rate.addRatingBar(flu_bar);
        rv_rate.addRatingBar(clean_bar);

        rv_rate.postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_rate.show();
            }
        }, DanceWageTimer.getTotalExecuteTime(total_wage, 50));//先显示分数，50毫秒后在显示打分控件


        //注册屏幕的监听
        mobileReceiver = new MobileReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mobileReceiver, intentFilter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Animation downanim = AnimationUtils.loadAnimation(this,R.anim.main_ui_up_down);
        Animation upanim = AnimationUtils.loadAnimation(this,R.anim.main_ui_down_up);

        rl_rate.startAnimation(downanim);
        rl_down.startAnimation(upanim);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_up:
                startActivity(new Intent(this,FuncationsActivity.class));
//实现activity的跳转动画
                overridePendingTransition(R.anim.funcations_anim,0);
                break;

            case R.id.tv_family:
                //找到所需要的控件
                //加载动画资源
                //启动动画
                //对动画进行监听

                jumpAnim(FamilyActivity.class);

                break;

            case R.id.tv_speedup:

                jumpAnim(MobileSpeedUpActivity.class);

                break;
        }
    }


    private void jumpAnim(final Class activity){
        Animation upanim = AnimationUtils.loadAnimation(this,R.anim.main_ui_up_up);
        Animation doananim = AnimationUtils.loadAnimation(this,R.anim.main_ui_down_down);

        rl_rate.startAnimation(upanim);
        rl_down.startAnimation(doananim);

        upanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //当动画停止时跳转到familyactivity页面
                startActivity(new Intent(MainUiActivity.this,activity));
                //activity的渐变动画
                overridePendingTransition(R.anim.activity_in_alpha,R.anim.activity_out_alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消监听
        unregisterReceiver(mobileReceiver);
    }
}
