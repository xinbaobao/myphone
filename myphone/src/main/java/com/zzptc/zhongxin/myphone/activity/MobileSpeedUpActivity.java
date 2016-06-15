package com.zzptc.zhongxin.myphone.activity;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.premnirmal.textcounter.CounterView;
import com.github.premnirmal.textcounter.IntegerFormatter;
import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.adapter.SpeedUpAdapter;
import com.zzptc.zhongxin.myphone.bean.AppInfo;
import com.zzptc.zhongxin.myphone.bean.DividerItemDecoration;
import com.zzptc.zhongxin.myphone.fragment.SpeedUpFragment;
import com.zzptc.zhongxin.myphone.utils.MessageUtils;
import com.zzptc.zhongxin.myphone.utils.SpeedUpTools;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MobileSpeedUpActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,SpeedUpTools.ScanListener,SpeedUpAdapter.OnSpeedUpListener,View.OnClickListener {

    private AppBarLayout apl_scroll;
    private Toolbar tb_mobilespeedup;
    private TextView tv_title;
    private RelativeLayout rl_load;
    private RelativeLayout rl_jieguo;
    private Button btn_clean;
    private TextView tv_scan;
    private RecyclerView rv_view;
    private TextView tv_jincheng;
    private CounterView tv_size;

    private SpeedUpTools speedUpTools;

    private List<AppInfo> appInfoList;
    private SpeedUpAdapter speedUpAdapter;

    private long allmemory;
    private DbManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_speed_up);

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("whitelist.db")
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
        dbManager = x.getDb(daoConfig);

        initView();

//Toolbar的返回监听
        tb_mobilespeedup.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //给Toolbar添加菜单
        tb_mobilespeedup.inflateMenu(R.menu.user_center_menu);

//滚动到顶部的时候，改变Toolbar上的文字，往下拉时，Toolbar的文字变回原来的文字
        speedUpTools = new SpeedUpTools();
        speedUpTools.scanMobile();
        speedUpTools.setScanListener(this);

    }

    private void initView() {
        apl_scroll = (AppBarLayout) findViewById(R.id.apl_scroll);
        tb_mobilespeedup = (Toolbar) findViewById(R.id.tb_mobilespeedup);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_load = (RelativeLayout) findViewById(R.id.rl_load);
        rl_jieguo = (RelativeLayout) findViewById(R.id.rl_jieguo);
        btn_clean = (Button) findViewById(R.id.btn_clean);
        tv_scan = (TextView) findViewById(R.id.tv_scan);
        rv_view = (RecyclerView) findViewById(R.id.rv_view);
        tv_jincheng = (TextView) findViewById(R.id.tv_jincheng);
        tv_size = (CounterView) findViewById(R.id.tv_size);

        apl_scroll.addOnOffsetChangedListener(this);

        //设置布局管理器
        rv_view.setLayoutManager(new LinearLayoutManager(this));
        rv_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//设置适配器
        appInfoList = new ArrayList<>();
        speedUpAdapter = new SpeedUpAdapter(appInfoList, this);
        rv_view.setAdapter(speedUpAdapter);

        speedUpAdapter.setOnSpeedUpListener(this);
    }

    /**
     * 根据偏移量改变Toolbar上的文字
     *
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        System.out.println("@@@@@@@@@@@@@@@@@@@"+verticalOffset);
        if (verticalOffset < -200) {
            tv_title.setText("88MB内存可清理");
        } else {
            tv_title.setText("手机加速");
        }
    }

    @Override
    public void ScanStartListener(Context context) {
        System.out.println("开始扫描");
    }

    @Override
    public void ScanProcessListener(Context context, int current, int total) {

        tv_scan.setText("正在扫描" + current + "/" + total);
    }

    @Override
    public void ScanCompleteListener(Context context, List<AppInfo> appInfos) {

        rl_load.setVisibility(View.GONE);
        rl_jieguo.setVisibility(View.VISIBLE);
        btn_clean.setVisibility(View.VISIBLE);

        allmemory = 0;

        //去除系统进程 ，格式化单位
        try {
            for (AppInfo appInfo : appInfos) {
                if (!appInfo.isSystem && !appInfo.isCurrent) {
//查询数据库
                    AppInfo appInfo1 = dbManager.selector(AppInfo.class).where(WhereBuilder.b("appName", "=", appInfo.appName).and("pakcageName", "=", appInfo.pakcageName)).findFirst();
                    //当数据不为空时，复选框没被选中，返回false，反之，复选框被选中，返回true
                    if (appInfo1 != null) {
                        appInfo.isChecked = false;
                    } else {
                        appInfo.isChecked = true;
                    }
                    appInfoList.add(appInfo);
//当复选框被选中时，总内存加上选中的内存
                    if (appInfo.isChecked) {
                        allmemory += appInfo.memory;
                    }

                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        speedUpAdapter.notifyDataSetChanged();

        btn_clean.setText("一键加速" + MessageUtils.conver(allmemory));
        tv_jincheng.setText("正在运行的进程（" + appInfoList.size() + "个）");

        countUp(allmemory);
    }

    @Override
    public void CleanstartListener(Context context) {
        btn_clean.setAnimation(AnimationUtils.loadAnimation(this, R.anim.main_ui_down_down));
    }

    @Override
    public void CleancompleteListener(Context context, long cleanmemory) {
        while(appInfoList.size() > 0){
            speedUpAdapter.removeItem(0);
        }

        countDown(allmemory,allmemory - cleanmemory);
    }

    @Override
    public void onItemListener(View itemView, int position) {
        //点击条目时，弹出对话框并传递数据
        AppInfo appInfo = appInfoList.get(position);
        SpeedUpFragment speedUpFragment = SpeedUpFragment.newInstance(appInfo, position);
        speedUpFragment.show(getFragmentManager(), null);
//        Toast.makeText(MobileSpeedUpActivity.this, "你点击的条目的位置是" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckedListener(View checkbox, int position) {
//               Toast.makeText(MobileSpeedUpActivity.this, "你点击的复选框的位置是" + position, Toast.LENGTH_SHORT).show();

        AppInfo appInfo = appInfoList.get(position);

        CheckBox box = (CheckBox) checkbox;
        if (box.isChecked()) {
            //如果复选框被选中，总内存+选中的内存
            appInfo.isChecked = true;
            allmemory += appInfo.memory;
            //复选框选中，移除白名单
            try {
                dbManager.delete(AppInfo.class, WhereBuilder.b("appName", "=", appInfo.appName).and("pakcageName", "=", appInfo.pakcageName));
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
            //如果复选框被取消，总内存-选中内存
            appInfo.isChecked = false;
            allmemory -= appInfo.memory;
            //复选框取消，添加到白名单
            try {
                dbManager.save(appInfo);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        btn_clean.setText("一键加速" + MessageUtils.conver(allmemory));
    }

    public long getAllmemory() {
        return allmemory;
    }

    public void setAllmemory(long allmemory) {
        this.allmemory = allmemory;
    }

    public SpeedUpAdapter getSpeedUpAdapter() {
        return speedUpAdapter;
    }

    public void setSpeedUpAdapter(SpeedUpAdapter speedUpAdapter) {
        this.speedUpAdapter = speedUpAdapter;
    }

    public SpeedUpTools getSpeedUpTools() {
        return speedUpTools;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public int getSize() {
        return appInfoList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:

                speedUpTools.startClean(appInfoList);

                break;
        }
    }

    /*
    数值变大和变小
     */
    private void countUp(long allmemory){
        tv_size.setAutoFormat(false);
        tv_size.setAutoStart(false);
        tv_size.setStartValue(0f);

        String size = MessageUtils.conver(allmemory);
        String[] strs = size.split(" ");
        tv_size.setEndValue(Float.valueOf(strs[0]));
        tv_size.setIncrement(1);
        tv_size.setTimeInterval(50);
        tv_size.start();
    }

    private void countDown(long beforeMemory, long afterMemory){
        tv_size.setAutoFormat(false);
        tv_size.setAutoStart(false);
        tv_size.setFormatter(new IntegerFormatter());

        String beforeSize = MessageUtils.conver(beforeMemory);
        String[] befores = beforeSize.split(" ");
        tv_size.setStartValue(Float.valueOf(befores[0]));

        String afterSize = MessageUtils.conver(afterMemory);
        String[] afters = afterSize.split(" ");
        tv_size.setEndValue(Float.valueOf(afters[0]));

        tv_size.setIncrement(-1);
        tv_size.setTimeInterval(50);
        tv_size.start();
    }

}
