package com.zzptc.zhongxin.myphone.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.adapter.DisturbAdapter;
import com.zzptc.zhongxin.myphone.bean.AppInfo;
import com.zzptc.zhongxin.myphone.bean.DividerItemDecoration;
import com.zzptc.zhongxin.myphone.utils.DisturbTools;

import java.util.ArrayList;
import java.util.List;

public class DisturbActivity extends AppCompatActivity implements DisturbTools.OnScanListener{

    private Toolbar tb_disturb;
    private RecyclerView rv_disturb;
    private List<AppInfo> list;
    private DisturbAdapter disturbAdapter;
    private RelativeLayout rl_scan;
    private TextView tv_scan;
    private RelativeLayout rl_scanresult;
    private TextView tv_install;
    private DisturbTools disturbTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disturb);

        initView();
        disturbTools = new DisturbTools();
        disturbTools.setOnScanListener(this);
        disturbTools.ScanInstall();


        //当点击Toolbar时，关闭当前页面，返回上一个页面
        tb_disturb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initView(){
        tb_disturb = (Toolbar) findViewById(R.id.tb_disturb);
        rv_disturb = (RecyclerView) findViewById(R.id.rv_disturb);
        rl_scan = (RelativeLayout) findViewById(R.id.rl_scan);
        tv_scan = (TextView) findViewById(R.id.tv_scan);
        rl_scanresult = (RelativeLayout) findViewById(R.id.rl_scanresult);
        tv_install = (TextView) findViewById(R.id.tv_install);


        //设置布局管理器
        rv_disturb.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_disturb.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //设置适配器
        list = new ArrayList<>();
        disturbAdapter = new DisturbAdapter(list,this);
        rv_disturb.setAdapter(disturbAdapter);


    }


    @Override
    public void onStartScanListener(Context context) {
        System.out.println("开始扫描");
    }

    @Override
    public void onProcessScanListener(Context context, int current, int total) {
        tv_scan.setText("正在扫描" + current + "/" + total);
    }

    @Override
    public void onCompleteScanListener(Context context, List<AppInfo> appInfoList) {
        rl_scan.setVisibility(View.GONE);
        rl_scanresult.setVisibility(View.VISIBLE);

        list.clear();

        for(AppInfo appInfo : appInfoList){
            if(!appInfo.isSystem){
                list.add(appInfo);
            }
        }

        disturbAdapter.notifyDataSetChanged();
        tv_install.setText("已安装（"+ list.size() +"）");
    }

}
