package com.zzptc.zhongxin.myphone.fragment;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.activity.MobileSpeedUpActivity;
import com.zzptc.zhongxin.myphone.adapter.SpeedUpAdapter;
import com.zzptc.zhongxin.myphone.bean.AppInfo;
import com.zzptc.zhongxin.myphone.utils.MessageUtils;
import com.zzptc.zhongxin.myphone.utils.SpeedUpTools;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpeedUpFragment extends DialogFragment implements View.OnClickListener {

    private TextView tv_whitelist;

    public static SpeedUpFragment newInstance(AppInfo appInfo,int position){
        SpeedUpFragment speedUpFragment = new SpeedUpFragment();
//将需要传递的数据绑定到Bundle  使用Parcelable实列化对象，因为Serializable不能传递图标
        Bundle bundle = new Bundle();
        bundle.putParcelable("info", appInfo);
        bundle.putInt("position", position);
        speedUpFragment.setArguments(bundle);

        return speedUpFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //外部点击不可取消
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//去除标题
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_speed_up,container,false);

        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
        TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
        tv_whitelist = (TextView) view.findViewById(R.id.tv_whitelist);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_clean = (Button) view.findViewById(R.id.btn_clean);

        //设置点击事件
        tv_detail.setOnClickListener(this);
        tv_whitelist.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_clean.setOnClickListener(this);

        //通过getArguments()从bundle中得到绑定的数据
        Bundle bundle = getArguments();
        //当数据不为空时，实列化对象
        if(bundle != null){
            AppInfo appInfo = bundle.getParcelable("info");
            //当对象不为空时，设置相应的控件
            if(appInfo != null){
                tv_name.setText(appInfo.appName);
                tv_size.setText("内存占用：" + MessageUtils.conver(appInfo.memory));

                //当复选框被选中时，fragment对话框中显示加入白名单，反之，则显示移除白名单
                if(appInfo.isChecked){
                    addWhitelist();
                }else {
                    removeWhitelist();
                }
            }
        }

        //当点击返回键时，dismiss掉FrafmentDialog
   getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK){
                    dismiss();
                }

                return false;
            }
        });

        return view;
    }

    private void addWhitelist(){
        //获取图片
        Drawable drawableLeft = ContextCompat.getDrawable(getActivity(),R.mipmap.add_white_list);
        //给图片设置边距
        drawableLeft.setBounds(0,0,drawableLeft.getMinimumWidth(),drawableLeft.getIntrinsicHeight());
        //将图片添加到TextView的左边
        tv_whitelist.setCompoundDrawables(drawableLeft,null,null,null);
        //设置文字
        tv_whitelist.setText("加入白名单");
    }

    private void  removeWhitelist(){
        //获取图片
        Drawable drawableLeft = ContextCompat.getDrawable(getActivity(),R.mipmap.remove_white_list);
        //给图片设置边距
        drawableLeft.setBounds(0,0,drawableLeft.getMinimumWidth(),drawableLeft.getIntrinsicHeight());
        //将图片添加到TextView的左边
        tv_whitelist.setCompoundDrawables(drawableLeft,null,null,null);
        //设置文字
        tv_whitelist.setText("移除白名单");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_detail:
//当点击应用详情时，跳转到应用详情界面
                AppInfo appInfo = getArguments().getParcelable("info");
                if(appInfo != null){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",appInfo.pakcageName,null);
                    intent.setData(uri);
                    startActivity(intent);
                }

                break;
            case R.id.tv_whitelist:

                //首先在相对应的Activity中将我们所需要用到的数据get（）/set（），然后再在fragment中得到他们
                //得到相对应的Activity
                MobileSpeedUpActivity mobileSpeedUpActivity = (MobileSpeedUpActivity) getActivity();
                //通过getAllmemory()从Activity得到allmemory
                long allmemory = mobileSpeedUpActivity.getAllmemory();
                //通过getSpeedUpAdapter()从Activity得到speedUpAdapter
                SpeedUpAdapter speedUpAdapter = mobileSpeedUpActivity.getSpeedUpAdapter();
                //通过getDbManager()从Activity得到dbManager
                DbManager dbManager = mobileSpeedUpActivity.getDbManager();
                //找到所需按钮
                Button btn_clean = (Button) mobileSpeedUpActivity.findViewById(R.id.btn_clean);
//通过getArguments()得到对象
                AppInfo appInfo1 = getArguments().getParcelable("info");;
                try {
                    //当对象中复选框被选中时，将他保存在数据库，对话框中显示移除白名单，条目中的复选框不被选中，总内存减去不被选中的内存
                    if(appInfo1.isChecked){
                        dbManager.save(appInfo1);

                        removeWhitelist();
                        appInfo1.isChecked = false;

                        allmemory -= appInfo1.memory;
                    }else {
                        //当对象中复选框不被选中时，将他从数据库中删除，对话框显示加入白名单，条目中的复选框被选中，总内存加上被选中的内存
                        dbManager.delete(AppInfo.class, WhereBuilder.b("appName","=",appInfo1.appName).and("pakcageName","=",appInfo1.pakcageName));

                        addWhitelist();
                        appInfo1.isChecked = true;

                        allmemory += appInfo1.memory;
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }

                mobileSpeedUpActivity.setAllmemory(allmemory);

                btn_clean.setText("一键加速" + MessageUtils.conver(allmemory));

                speedUpAdapter.notifyDataSetChanged();

                break;
            case R.id.btn_cancel:

                dismiss();

                break;
            case R.id.btn_clean:

                //先删除条目，在进行内存的修改，最后kill掉进程
                int position = getArguments().getInt("position");
                AppInfo appInfo2 = getArguments().getParcelable("info");

                MobileSpeedUpActivity mobileSpeedUpActivity1 = (MobileSpeedUpActivity) getActivity();
                SpeedUpAdapter speedUpAdapter1 = mobileSpeedUpActivity1.getSpeedUpAdapter();
                speedUpAdapter1.removeItem(position);

                if(appInfo2.isChecked){
                    long allM = mobileSpeedUpActivity1.getAllmemory();
                    allM -= appInfo2.memory;
                    Button btn_cleaning = (Button) mobileSpeedUpActivity1.findViewById(R.id.btn_clean);
                    btn_cleaning.setText("一键清理" + MessageUtils.conver(allM));
                    mobileSpeedUpActivity1.setAllmemory(allM);
                }

                SpeedUpTools speedUpTools = mobileSpeedUpActivity1.getSpeedUpTools();
                speedUpTools.killProcess(appInfo2.pakcageName);

                int size = mobileSpeedUpActivity1.getSize();
                TextView tv_jincheng = (TextView) mobileSpeedUpActivity1.findViewById(R.id.tv_jincheng);
                tv_jincheng.setText("正在运行的进程（"+ (size) +"个）");

                dismiss();

                break;
        }
    }
}
