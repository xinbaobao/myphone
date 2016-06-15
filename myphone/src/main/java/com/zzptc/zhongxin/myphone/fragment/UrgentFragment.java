package com.zzptc.zhongxin.myphone.fragment;


import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.adapter.UrgentAdapter;
import com.zzptc.zhongxin.myphone.bean.Contact;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UrgentFragment extends Fragment {

    private ListView lv_urgent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_urgent, container, false);

        ImageView iv_open = (ImageView) view.findViewById(R.id.iv_open);
        iv_open.setImageResource(R.drawable.urgent_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_open.getDrawable();
        animationDrawable.start();


        lv_urgent = (ListView) view.findViewById(R.id.lv_urgent);

        //因为点击完成按钮时把紧急联系人保存在了数据库里，所以现在通过Xutils3读取数据
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("urgentContacts.db")
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
        DbManager dbManager = x.getDb(daoConfig);

        try {
            List<Contact> urgentContact = dbManager.selector(Contact.class).findAll();

            UrgentAdapter urgentAdapter = new UrgentAdapter(urgentContact,getActivity());
            lv_urgent.setAdapter(urgentAdapter);

        } catch (DbException e) {
            e.printStackTrace();
        }


        return view;
    }

}
