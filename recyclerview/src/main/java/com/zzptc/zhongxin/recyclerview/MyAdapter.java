package com.zzptc.zhongxin.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhongxin on 2016/5/23.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> listitem;
    private LayoutInflater layoutInflater;

    public MyAdapter(List<String> listitem,Context context){
        this.listitem = listitem;
        layoutInflater = LayoutInflater.from(context);
    }

    //添加监听接口
    public interface OnItemClickListener{
        //给监听接口添加方法
        void onItemClickListener(View itemView,int position);
        void onItemLongClickListener(View itemView,int position);;
    }

    //声明监听
    private OnItemClickListener onItemClickListener;

    //通过set方法给类设置监听
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(R.layout.item_layout,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String data = listitem.get(position);
        holder.tv_item.setText(data);

        //调用监听接口中的方法
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(holder.itemView, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClickListener(holder.itemView,position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }

    public void addData(int position,String data){
        listitem.add(position,data+""+position);
        notifyItemInserted(position);
    }

    public void removeData(int position,String data){
        listitem.remove(position);
        notifyItemRemoved(position);
    }

}
