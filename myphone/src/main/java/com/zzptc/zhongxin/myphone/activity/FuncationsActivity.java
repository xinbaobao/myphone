package com.zzptc.zhongxin.myphone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;

public class FuncationsActivity extends Activity implements View.OnClickListener {

    private TextView tv_down;
    private TextView tv_disturb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcations);

        tv_disturb = (TextView) findViewById(R.id.tv_disturb);
        tv_down = (TextView) findViewById(R.id.tv_down);
        tv_down.setOnClickListener(this);
        tv_disturb.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_down:
                startActivity(new Intent(this,MainUiActivity.class));
                overridePendingTransition(R.anim.fun_down_anim,0);
                break;

            case R.id.tv_disturb:

                Intent intent = new Intent(this,DisturbActivity.class);
                startActivity(intent);

                break;
        }
    }
}
