package com.zzptc.zhongxin.myphone.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.zzptc.zhongxin.myphone.R;

public class FamilyActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_qiujiu;
    private Toolbar tb_family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        rl_qiujiu = (RelativeLayout) findViewById(R.id.rl_qiujiu);
        rl_qiujiu.setOnClickListener(this);

        tb_family = (Toolbar) findViewById(R.id.tb_family);
        tb_family.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    //按返回键
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_in_alpha, R.anim.activity_out_alpha);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_qiujiu:
                startActivity(new Intent(this,HelpActivity.class));
                //activity的渐变动画
                overridePendingTransition(R.anim.activity_in_alpha, R.anim.activity_out_alpha);
                break;
        }
    }
}
