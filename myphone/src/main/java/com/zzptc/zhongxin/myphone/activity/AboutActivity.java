package com.zzptc.zhongxin.myphone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zzptc.zhongxin.myphone.R;

public class AboutActivity extends AppCompatActivity {

    private Toolbar tb_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tb_about = (Toolbar) findViewById(R.id.tb_about);
        tb_about.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
