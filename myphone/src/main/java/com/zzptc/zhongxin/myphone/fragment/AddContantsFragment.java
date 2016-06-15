package com.zzptc.zhongxin.myphone.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.activity.AddContactsActivity;
import com.zzptc.zhongxin.myphone.activity.HelpActivity;
import com.zzptc.zhongxin.myphone.adapter.HelpAdapter;
import com.zzptc.zhongxin.myphone.bean.Contact;
import com.zzptc.zhongxin.myphone.utils.MessageUtils;
import com.zzptc.zhongxin.myphone.utils.ReadContactsUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContantsFragment extends Fragment implements View.OnClickListener {

    public HelpAdapter helpAdapter;
    public ListView lv_add;

    private ArrayList<Contact> helpcontacts;

    private LinearLayout ll_center;
    private EditText et_phone;
    private Button btn_add;
    private ImageView iv_add;

    private TextView et_help;
    private TextView tv_text0;

    private CheckBox cb_check;
    private Button btn_com;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contants, container, false);

        ll_center = (LinearLayout) view.findViewById(R.id.ll_center);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        btn_add = (Button) view.findViewById(R.id.btn_add);

        cb_check = (CheckBox) view.findViewById(R.id.cb_check);
        btn_com = (Button) view.findViewById(R.id.btn_com);

        et_help = (TextView) view.findViewById(R.id.et_help);
        tv_text0 = (TextView) view.findViewById(R.id.tv_text0);

        lv_add = (ListView) view.findViewById(R.id.lv_add);
        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_com.setOnClickListener(this);

        helpcontacts = new ArrayList<>();
        //一进来就设置适配器
        helpAdapter = new HelpAdapter(helpcontacts,getActivity(),ll_center);
        lv_add.setAdapter(helpAdapter);

        //当编辑框输入数字时，添加图片不显示，确定按钮显示。没有输入数字时，显示添加图片，不显示确定按钮
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    iv_add.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);
                }else {
                    iv_add.setVisibility(View.VISIBLE);
                    btn_add.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//对编辑信息的输入框进行监听，判断所写信息的数字
        et_help.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_text0.setText(s.length() + "/40");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add:
//启动有返回值的activity需要用startActivityForResult
                Intent intent = new Intent(getActivity(),AddContactsActivity.class);
                intent.putExtra("selecterItems",helpcontacts);
                startActivityForResult(intent,0);//0表示请求码

                break;

            case R.id.btn_add:
//判断是否为手机号码，当编辑框里输入的手机号码在所有联系人中存在时，显示所有信息，
// 当编辑框里输入的手机号码在所有联系人中不存在时，显示手机号码
                String input = et_phone.getText().toString();
                if(input.matches("^1[34578]\\d{9}$")){

                    boolean isExist = isExist(input);
                    if(!isExist){
                        //把所有联系人放在allContacts集合里
                        List<Contact> allContacts = ReadContactsUtils.getContacts();
                        boolean isContain = false;//默认为不包含
                        //当输入的手机号码包含在所有联系人中就把它的所有信息拿出来现世在helpcontacts集合中，并通知适配器发生改变
                        for(Contact contact : allContacts){
                            if(contact.getPhone().equals(input)){
                                helpcontacts.add(contact);
                                helpAdapter.notifyDataSetChanged();
                                isContain = true;
                                break;
                            }
                        }

                        //当输入的手机号码不包含在所有联系人中就new一个对象存放它，然后从对象中拿出来添加到helpcontacts集合中
                        //并通知适配器发生改变
                        if(!isContain){
                            Contact contact = new Contact();
                            contact.setPhone(input);
                            helpcontacts.add(contact);
                            helpAdapter.notifyDataSetChanged();
                        }
                        //当helpcontacts集合中的数量等于3时，不显示编辑框
                        if(helpcontacts.size() == 3){
                            ll_center.setVisibility(View.GONE);
                        }
                    }else {
                        Toast.makeText(getActivity(), "您输入的手机号码已存在，请输入新的号码", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                et_phone.setText("");
                break;

            case R.id.btn_com:

                if(helpcontacts != null || helpcontacts.size()>0){
                    String info = et_help.getText().toString();
                    if(cb_check.isChecked()){
                        for(Contact contact : helpcontacts){
//                            MessageUtils.sendMessage(contact.getPhone(),info);
                        }
                    }

                    //保存求救短信,下次遇到危险还是发送给紧急联系人
                    for(int i = 0; i< helpcontacts.size(); i++){
                        Contact c = helpcontacts.get(i);
                        c.setHelpMessage(info);

                        helpcontacts.set(i,c);
                    }

                    //将紧急联系人保存到数据库
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
//清除数据
                    try {
                        //定义一个集合存放数据List<Contact> urgent
                        List<Contact> urgent = dbManager.selector(Contact.class).findAll();
                        if(urgent != null && urgent.size() > 0){
                            dbManager.delete(urgent);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    //保存数据
                    for(Contact c:helpcontacts){
                        try {
                            dbManager.save(c);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }


                    HelpActivity helpActivity = (HelpActivity) getActivity();
                    helpActivity.Urgentreplace();

                    SharedPreferences sp = getActivity().getSharedPreferences("urgent", Context.MODE_PRIVATE);
                    sp.edit().putBoolean("HasUrgent",true).commit();

                }else {
                    Toast.makeText(getActivity(), "请选择紧急联系人", Toast.LENGTH_SHORT).show();
                }


                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //接收返回的数据
        if(requestCode == 0){
            switch (resultCode){
                case 1:
                    ArrayList<Contact> contacts = (ArrayList<Contact>) data.getSerializableExtra("data");
//                    for(Contact contact : contacts){
//                        System.out.println(contact);
//                    }

                    helpcontacts.addAll(contacts);
                    helpAdapter.notifyDataSetChanged();

                    if(contacts != null){
                        for(Contact c : contacts){
                            boolean isExist = isExist(c.getPhone());
                            if(!isExist){
                                this.helpcontacts.add(c);
                                helpAdapter.notifyDataSetChanged();
                            }
                        }

                    }

                    //当helpcontacts集合中的数量等于3时，不显示编辑框
                    if(helpcontacts.size() == 3){
                        ll_center.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 判断手机号码是否存在
     * @param phone
     * @return
     */

    private boolean isExist(String phone){
        boolean flag = false;
//遍历集合helpcontacts
        for(int i = 0; i < helpcontacts.size(); i++){
            Contact contact = helpcontacts.get(i);
            if(phone.equals(contact.getPhone())){
                flag = true;
                break;
            }
        }

        return flag;
    }

}
