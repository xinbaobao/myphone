package com.zzptc.zhongxin.myphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.AppInfo;
import com.zzptc.zhongxin.myphone.utils.MessageUtils;

import java.util.List;

/**
 * Created by zhongxin on 2016/5/26.
 */
public class SpeedUpAdapter extends RecyclerView.Adapter<SpeedUpAdapter.ViewHolder> {

    private List<AppInfo> appInfos;
    private LayoutInflater layoutInflater;

    public SpeedUpAdapter(List<AppInfo> appInfoList,Context context){
        this.appInfos = appInfoList;
        layoutInflater = LayoutInflater.from(context);
    }

    //定义监听
    public interface OnSpeedUpListener{
        void onItemListener(View itemView,int position);
        void onCheckedListener(View checkbox, int position);
    }
//声明监听
    private OnSpeedUpListener onSpeedUpListener;
//设置监听
    public void setOnSpeedUpListener(OnSpeedUpListener onSpeedUpListener){
        this.onSpeedUpListener = onSpeedUpListener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(R.layout.cleanmobile_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        AppInfo appInfo = appInfos.get(position);
        holder.iv_icon.setImageDrawable(appInfo.icon);
        holder.tv_appname.setText(appInfo.appName);
        holder.tv_size.setText(MessageUtils.conver(appInfo.memory));
        holder.cb_clean.setChecked(appInfo.isChecked);


        //安装监听器
        if(onSpeedUpListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSpeedUpListener.onItemListener(holder.itemView,position);
                }
            });

            holder.cb_clean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSpeedUpListener.onCheckedListener(holder.cb_clean,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_icon;
        TextView tv_appname;
        TextView tv_size;
        CheckBox cb_clean;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_appname = (TextView) itemView.findViewById(R.id.tv_appname);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            cb_clean = (CheckBox) itemView.findViewById(R.id.cb_clean);
        }
    }

    public void removeItem(int position){
        appInfos.remove(position);
        notifyItemRemoved(position);
    }

}
