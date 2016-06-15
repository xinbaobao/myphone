package com.zzptc.zhongxin.myphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.Contact;

import java.util.List;

/**
 * Created by zhongxin on 2016/5/10.
 */
public class HelpAdapter extends BaseAdapter {

    public List<Contact> helpcontacts;
    public LayoutInflater layoutInflater;

    private LinearLayout ll_center;

    public HelpAdapter(List<Contact> helpcontacts,Context context,LinearLayout ll_center){
        this.helpcontacts = helpcontacts;
        this.ll_center = ll_center;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return helpcontacts.size();
    }

    @Override
    public Object getItem(int position) {
        return helpcontacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.add_contacts_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.iv_false = (ImageView) convertView.findViewById(R.id.iv_false);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = helpcontacts.get(position);
        viewHolder.tv_phone.setText(contact.getPhone());
        viewHolder.tv_name.setText(contact.getName());

        //当点击删除时，删除所点击位置的联系人并通知适配器发生改变
        viewHolder.iv_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpcontacts.remove(position);
                notifyDataSetChanged();
//当helpcontacts集合中的数量小于3时，显示编辑框
                if(helpcontacts.size() < 3){
                    ll_center.setVisibility(View.VISIBLE);
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView tv_phone;
        TextView tv_name;
        ImageView iv_false;
    }
}
