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
 * Created by zhongxin on 2016/5/12.
 */
public class SearchAdapter extends BaseAdapter {

    public List<Contact> searchcontacts;
    public LayoutInflater layoutInflater;

    public SearchAdapter(List<Contact> searchcontacts,Context context){
        this.searchcontacts = searchcontacts;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchcontacts.size();
    }

    @Override
    public Object getItem(int position) {
        return searchcontacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.search_item,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tv_attri = (TextView) convertView.findViewById(R.id.tv_attri);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = searchcontacts.get(position);
        viewHolder.tv_name.setText(contact.getName());
        viewHolder.tv_phone.setText(contact.getPhone());
        viewHolder.tv_attri.setText(contact.getAttribute());

        return convertView;
    }


    class ViewHolder{
        TextView tv_name;
        TextView tv_phone;
        TextView tv_attri;
    }

}
