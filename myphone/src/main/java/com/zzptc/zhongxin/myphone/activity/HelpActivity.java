package com.zzptc.zhongxin.myphone.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.Contact;
import com.zzptc.zhongxin.myphone.fragment.AddContantsFragment;
import com.zzptc.zhongxin.myphone.fragment.HelpFragment;
import com.zzptc.zhongxin.myphone.fragment.UrgentFragment;

public class HelpActivity extends AppCompatActivity {

    private Toolbar tb_family;
    private ImageView iv_question;
    private AddContantsFragment addContantsFragment;
    private UrgentFragment urgentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        tb_family = (Toolbar) findViewById(R.id.tb_family);
        iv_question = (ImageView) findViewById(R.id.iv_question);

        tb_family.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //要让用户一进来就能看到一键求救页面
        //1.得到getFragmentManager  2.打开事物beginTransaction  3.替换Fragment  replace  4.提交事物commit

        SharedPreferences sp = getSharedPreferences("urgent", Context.MODE_PRIVATE);
        boolean HasUrgent = sp.getBoolean("HasUrgent",false);

        if(HasUrgent){
            Urgentreplace();
        }else{
            getFragmentManager().beginTransaction().replace(R.id.fl_help,new HelpFragment()).addToBackStack("help").commit();
        }
    }

    public void replace(){
        //如果没有addContantsFragment就new一个
        if(addContantsFragment == null){
            addContantsFragment = new AddContantsFragment();
        }
        //设置进出的渐变动画（属性动画） 添加fragment放到取名为help的堆栈中并提交
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.animator_in,R.animator.animator_out,R.animator.animator_in,R.animator.animator_out)
                .add(R.id.fl_help, addContantsFragment).addToBackStack("help").commit();
        //去除toolbar上的图片
//        tb_family.removeView(iv_question);
        iv_question.setVisibility(View.GONE);
    }

    public void Urgentreplace(){

        getFragmentManager().popBackStackImmediate("help", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if(urgentFragment == null){
            urgentFragment = new UrgentFragment();
        }
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.animator_in,R.animator.animator_out,R.animator.animator_in,R.animator.animator_out)
                .replace(R.id.fl_help,urgentFragment).addToBackStack("help").commit();
    }

    @Override
    public void onBackPressed() {
        //如果堆栈里面的fragment大于1就弹出，否则就finish掉显示上一个activity
        if(getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
            //添加toolbar上的图片
//            tb_family.addView(iv_question);
            iv_question.setVisibility(View.VISIBLE);
        }else{
            finish();
        }



    }
}
