package com.zzptc.zhongxin.myphone.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.AppInfo;

import org.xutils.x;

import java.util.List;

/**
 * Created by zhongxin on 2016/6/8.
 */
public class DisturbAdapter extends RecyclerView.Adapter<DisturbAdapter.ViewHolder> {

    private List<AppInfo> appInfos;
    private LayoutInflater layoutInflater;
    private Context context;

    public DisturbAdapter(List<AppInfo> appInfos,Context context){
        this.appInfos = appInfos;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(R.layout.disturb_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.btn_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", viewHolder.appInfo.pakcageName, null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AppInfo appInfo = appInfos.get(position);
        holder.iv_con.setImageDrawable(appInfo.icon);
        holder.tv_name.setText(appInfo.appName);
        holder.appInfo = appInfo;

    }

    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_con;
        TextView tv_name;
        Button btn_manage;
        AppInfo appInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_con = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            btn_manage = (Button) itemView.findViewById(R.id.btn_manage);
        }
    }
}
