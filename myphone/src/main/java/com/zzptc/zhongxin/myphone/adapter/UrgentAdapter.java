package com.zzptc.zhongxin.myphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.Contact;

import java.util.List;

/**
 * Created by zhongxin on 2016/5/16.
 */
public class UrgentAdapter extends BaseAdapter {
    private List<Contact> urgentcontacts;
    private LayoutInflater layoutInflater;

    public UrgentAdapter(List<Contact> urgentcontacts,Context context){
        this.urgentcontacts = urgentcontacts;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return urgentcontacts.size();
    }

    @Override
    public Object getItem(int position) {
        return urgentcontacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.urgent_item,parent,false);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

        Contact contact = urgentcontacts.get(position);
        tv_name.setText(contact.getName());
        tv_phone.setText(contact.getPhone());

        return view;
    }
}
