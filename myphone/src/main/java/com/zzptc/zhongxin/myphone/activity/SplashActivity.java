package com.zzptc.zhongxin.myphone.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity implements View.OnClickListener {

    private ViewPager vp_pager;
    private ImageView iv_left,iv_center,iv_right;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        vp_pager = (ViewPager) findViewById(R.id.vp_pager);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_center = (ImageView) findViewById(R.id.iv_center);
        iv_right = (ImageView) findViewById(R.id.iv_right);

        List<View> views = new ArrayList<View>();
        //将布局转化为视图
        View splash1 = View.inflate(this,R.layout.splash1_layout,null);
        View splash2 = View.inflate(this,R.layout.splash2_layout,null);
        View splash3 = View.inflate(this,R.layout.splash3_layout,null);

        //添加视图
        views.add(splash1);
        views.add(splash2);
        views.add(splash3);

        //设置适配器
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        vp_pager.setAdapter(adapter);

        //通过第三个布局的视图找到按钮
        button = (Button) splash3.findViewById(R.id.button);
        button.setOnClickListener(this);

        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        iv_left.setImageResource(R.mipmap.dot_selected);
                        iv_center.setImageResource(R.mipmap.dot_unselected);
                        iv_right.setImageResource(R.mipmap.dot_unselected);
                        break;
                    case 1:
                        iv_left.setImageResource(R.mipmap.dot_unselected);
                        iv_center.setImageResource(R.mipmap.dot_selected);
                        iv_right.setImageResource(R.mipmap.dot_unselected);
                        break;
                    case 2:
                        iv_left.setImageResource(R.mipmap.dot_unselected);
                        iv_center.setImageResource(R.mipmap.dot_unselected);
                        iv_right.setImageResource(R.mipmap.dot_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button :
//            Toast.makeText(SplashActivity.this, "跳转成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainUiActivity.class));
                finish();
                break;
        }
    }
}
