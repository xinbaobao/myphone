package com.zzptc.zhongxin.myphone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkcool.circletextimageview.CircleTextImageView;
import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongxin on 2016/5/8.
 */
public class ContactAdapter extends BaseAdapter {

    private List<Contact> contacts;
    private LayoutInflater inflater;

    //保存复选框的状态
    private Map<Integer,Boolean> checkItems;

    //定义int变量记录选中的数量
    private int counter = 0;

    private Context context;

    //toolbar上的变化所需的控件
    private TextView tv_contacts;
    private TextView tv_queding;

    private ArrayList<Contact> selectedItems;

    public ContactAdapter(List<Contact> contacts,Context context,TextView tv_contacts,TextView tv_queding,ArrayList<Contact> selectedItems){
        this.contacts = contacts;
        this.context = context;
        this.tv_contacts = tv_contacts;
        this.tv_queding = tv_queding;
        this.selectedItems = selectedItems;
        inflater = LayoutInflater.from(context);

        //通过hasmap保存复选框的状态  初始化复选框状态   默认为不选中false
        checkItems = new HashMap<>();
        for(int position = 0 ;position < contacts.size(); position++){
            checkItems.put(position,false);
        }
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            //将convertView上的控件转变为viewHolder
            convertView = inflater.inflate(R.layout.contacts_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.ctiv_head = (CircleTextImageView) convertView.findViewById(R.id.ctiv_head);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tv_attri = (TextView) convertView.findViewById(R.id.tv_attri);
            viewHolder.cb_checked = (CheckBox) convertView.findViewById(R.id.cb_checked);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        final Contact contact = contacts.get(position);
        String name = contact.getName();

        viewHolder.ctiv_head.setFillColor(contact.getHeadColor());
        viewHolder.ctiv_head.setText(name.substring(name.length() - 1));

        viewHolder.tv_name.setText(name);
        viewHolder.tv_phone.setText(contact.getPhone());
        viewHolder.tv_attri.setText(contact.getAttribute());

        //给checkbox一个默认状态，  false  不选中
        //当checkbox选中时是true，不选中为false  从checkItems中获取复选框状态
        viewHolder.cb_checked.setChecked(checkItems.get(position));

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.cb_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当号码不是座机号码时可以点击复选框进行选择
                if(!contact.getAttribute().equals("座机号码")){
                    //当联系人没有被选中时，在用户选择时才判断联系人是否存在，如果已经被选中，则点击复选框是取消选中
                    if(!isExist(contact.getPhone())){
                        if(finalViewHolder.cb_checked.isChecked()){
                            boolean isContent = false;
                            for(Contact c : selectedItems){
                                if(contact.getPhone().equals(c.getPhone())){
                                    isContent = true;
                                }
                            }

                            if(!isContent) {
                                //当选中的数量小于3时可以继续点击选中，否则就不能点击选中
                                if (selectedItems.size() < 3) {//counter < 3 - selectedItems.size()
                                    checkItems.put(position, true);
                                    selectedItems.add(contact);
                                    //选中时数量增加
//                                counter++;
                                } else {
                                    Toast.makeText(context, "您已经选中了三个求救号码", Toast.LENGTH_SHORT).show();
                                }
                            }
                    }
                    else{
                            Toast.makeText(context, "号码已被选中", Toast.LENGTH_SHORT).show();
                        }

                        if(!finalViewHolder.cb_checked.isChecked()){
                            checkItems.put(position,false);
                            selectedItems.remove(contact);
                        }
                    }
                    /**
                     * else{
                     checkItems.put(position,false);
                     selectedItems.remove(contact);
                     取消时数量减少
                     counter--;
                     }
                     */
                }else {
                    Toast.makeText(context, "请选择正确的手机号码", Toast.LENGTH_SHORT).show();
                }


//选择联系人随着所选复选框数量的变化而变化
                tv_contacts.setText("选择联系人  ("+selectedItems.size()+")");
                //当数量大于0时  确定按钮变为白色，可点击，否则确定按钮变为灰色，不可点击
                if(selectedItems.size() > 0){
                    tv_queding.setTextColor(Color.WHITE);
                    tv_queding.setClickable(true);
                }else {
                    tv_queding.setTextColor(Color.GRAY);
                    tv_queding.setClickable(false);
                }

                //通知适配器发生改变
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder{
        CircleTextImageView ctiv_head;
        TextView tv_name;
        TextView tv_phone;
        TextView tv_attri;
        CheckBox cb_checked;
    }

    public Map<Integer, Boolean> getCheckItems() {
        return checkItems;
    }

    public ArrayList<Contact> getSelectedItems() {
        return selectedItems;
    }

    /**
     * 判断电话号码是否存在
     * @param phone
     * @return
     */
    public boolean isExist(String phone){
        boolean flag = false;

        for(Contact c:selectedItems){
            if(c.getPhone().equals(phone)){
                flag = true;

                break;
            }
        }

        return flag;
    }
}
