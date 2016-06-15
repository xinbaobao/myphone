package com.zzptc.zhongxin.myphone.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.adapter.ContactAdapter;
import com.zzptc.zhongxin.myphone.adapter.SearchAdapter;
import com.zzptc.zhongxin.myphone.bean.Contact;
import com.zzptc.zhongxin.myphone.utils.ReadContactsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddContactsActivity extends AppCompatActivity implements View.OnClickListener {

    //所有联系人delistview
    private ListView lv_contacts;
    private ContactAdapter adapter;
    private SearchAdapter searchAdapter;

    private TextView tv_contacts;
    private TextView tv_queding;
    private EditText et_search;

    //所有联系人的list集合
    private List<Contact> contacts;
    //搜索结果的list集合
    private List<Contact> searchcontacts;
    //搜索结果的listview
    private ListView lv_search;
    //紧急联系人的数量
    private ArrayList<Contact> selectedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        et_search = (EditText) findViewById(R.id.et_search);
        tv_contacts = (TextView) findViewById(R.id.tv_contacts);
        tv_queding = (TextView) findViewById(R.id.tv_queding);
        tv_queding.setOnClickListener(this);

        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        lv_search = (ListView) findViewById(R.id.lv_search);

        contacts = ReadContactsUtils.getContacts();

        Intent intent = getIntent();
        selectedItems = (ArrayList<Contact>) intent.getSerializableExtra("selecterItems");

         adapter = new ContactAdapter(contacts,this,tv_contacts,tv_queding,selectedItems);
        lv_contacts.setAdapter(adapter);

        searchcontacts = new ArrayList<>();
        searchAdapter = new SearchAdapter(searchcontacts,this);
        lv_search.setAdapter(searchAdapter);


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    lv_contacts.setVisibility(View.GONE);
                    lv_search.setVisibility(View.VISIBLE);

                    search(s.toString());
                } else {
                    lv_search.setVisibility(View.GONE);
                    lv_contacts.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv_search.setVisibility(View.GONE);
                lv_contacts.setVisibility(View.VISIBLE);

                Contact contact = searchcontacts.get(position);
                int checkedPosition = getPosition(contact.getPhone());
                lv_contacts.smoothScrollToPosition(checkedPosition + 4);
                //问题出现的原因  用户选中条目的时候，没有修改相应的数字
                //当用户点击条目时，先判断该联系人是否已经存在，如果存在，则跳转到指定位置，并提示用户该联系人已经存在
                //如果不存在，则判断用户点击的号码是否是正确的手机号码并修改Toolbar上相应的数字
                if (!adapter.isExist(contact.getPhone())) {
                    if (!contact.getAttribute().equals("座机号码")) {
                        Map<Integer, Boolean> checkItems = adapter.getCheckItems();
                        checkItems.put(checkedPosition, true);

                        List<Contact> selectedItems = adapter.getSelectedItems();
                        selectedItems.add(contact);

                        adapter.notifyDataSetChanged();
                        tv_contacts.setText("选择联系人  (" + selectedItems.size() + ")");

                        tv_queding.setTextColor(Color.WHITE);
                        tv_queding.setClickable(true);
                    } else {
                        Toast.makeText(AddContactsActivity.this, "请选择正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddContactsActivity.this, "号码已存在", Toast.LENGTH_SHORT).show();
                }

                et_search.setText("");

            }
        });
        tv_contacts.setText("选择联系人  ("+selectedItems.size()+")");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_queding:
                //通过setResult把值返回到AddContantsFragment
                Intent data = getIntent();

                //复选框选中的数据保存在hasMap中，所以我们只要去遍历hasMap，取出其中为true的对象就可以了
                //创建一个新的集合存放取出的对象
                ArrayList<Contact> contactList = new ArrayList<>();
                //从适配器中得到hasMap
                Map<Integer,Boolean> checkItems = adapter.getCheckItems();
                //遍历hasMap  通过keySet方法
                for(Integer key : checkItems.keySet()){
                    boolean flag = checkItems.get(key);

                    if(flag){
                        //从集合中得到选中的联系人
                        Contact contact = contacts.get(key);
                        //把得到的对象添加到新的集合里
                        contactList.add(contact);
                    }
                }

                data.putExtra("data",contactList);
                setResult(1,data);
                finish();

                break;
        }
    }

    /**
     *根据用户所输入的内容查询
     * @param key
     */

    private void search(String key){
        //查询数据前先清除数据
        searchcontacts.clear();
        for(int i = 0;i<contacts.size();i++ ){
            Contact contact = contacts.get(i);
            if(contact.getName().contains(key) || contact.getPhone().contains(key)){
                searchcontacts.add(contact);

                searchAdapter.notifyDataSetChanged();
            }
        }

    }
                //所有联系人的位置
    private int getPosition(String phone){
        int position = 0;

        for(int i = 0; i < contacts.size(); i++){
            Contact contact = contacts.get(i);
            if(contact.getPhone().equals(phone)){
                position = i;
                break;
            }
        }


        return position;
    }

}
