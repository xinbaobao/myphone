package com.zzptc.zhongxin.myphone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.UpdateInfo;
import com.zzptc.zhongxin.myphone.contant.Contants;
import com.zzptc.zhongxin.myphone.fragment.UpdateFragment;

public class UserCenterActivity extends Activity implements View.OnClickListener {

    private Toolbar tb_weishi;
    private RelativeLayout rl_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        tb_weishi = (Toolbar) findViewById(R.id.tb_weishi);
        rl_update = (RelativeLayout) findViewById(R.id.rl_update);
        rl_update.setOnClickListener(this);

        tb_weishi.inflateMenu(R.menu.user_center_menu);
        tb_weishi.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("关于卫士")) {
                    startActivity(new Intent(UserCenterActivity.this, AboutActivity.class));
                }
                return false;
            }
        });

        tb_weishi.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.user_twp_anim, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_update:
//                startActivity(new Intent(UserCenterActivity.this,LoadActivity.class));
                Intent intent = new Intent(this,LoadActivity.class);
                startActivityForResult(intent, Contants.REQUEST_CODE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == Contants.REQUEST_CODE){
            switch (resultCode){
                //没有网络
                case Contants.NET_INTERRUPT:
                    ShowDialog("没有网络，请检查网络");
                break;

                case Contants.NET_EXCEPTION:
                    ShowDialog("网络异常，请检查网络");
                    break;

                case Contants.NET_CANCEL:
                    ShowDialog("取消更新，请重新下载");
                    break;

                case Contants.NEED_UPDATE:
                    if(data != null){
                        UpdateInfo updateinfo = (UpdateInfo) data.getSerializableExtra("info");

                        UpdateFragment updateFragment = UpdateFragment.newInstance(updateinfo.getDescription(),updateinfo);
                        updateFragment.show(getFragmentManager(),null);
                    }
                    break;

                case Contants.NOT_NEED_UPDATE:
                    ShowDialog("当前是最新版本，不需要更新");
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void ShowDialog(String info){
        UpdateFragment updateFragment = UpdateFragment.newInstance(info);
        updateFragment.show(getFragmentManager(),null);
    }
}
