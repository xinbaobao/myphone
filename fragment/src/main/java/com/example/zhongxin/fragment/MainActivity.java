package com.example.zhongxin.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button btn_top;
    private Button btn_image;
    private Button btn_stu;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_top = (Button) findViewById(R.id.btn_top);
        btn_image = (Button) findViewById(R.id.btn_image);
        btn_stu = (Button) findViewById(R.id.btn_stu);

        btn_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                TopFragment topFragment = new TopFragment();
                transaction.replace(R.id.leftlayout,topFragment,"topfragment");
                transaction.addToBackStack("topfragment");
                transaction.commit();
            }
        });

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                ImageFragment imageFragment = new ImageFragment();
                transaction.replace(R.id.leftlayout,imageFragment,"imageframent");
                transaction.addToBackStack("imageframent");
                transaction.commit();
            }
        });

        btn_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                StuFragment stuFragment = new StuFragment();
                transaction.replace(R.id.leftlayout,stuFragment,"stuframent");
                transaction.addToBackStack("stuframent");
                transaction.commit();

            }
        });
    }
}
