package com.zzptc.zhongxin.myphone.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.AppInfo;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhongxin on 2016/6/13.
 */
public class DisturbTools {

    private Context context;
    private final ActivityManager activityManager;
    private final PackageManager packageManager;
    private List<AppInfo> appInfosdisturb;

    public PackageManager getPackageManager() {
        return packageManager;
    }

    public interface OnScanListener{
        void onStartScanListener(Context context);
        void onProcessScanListener(Context context,int current,int total);
        void onCompleteScanListener(Context context,List<AppInfo> appInfoList);

    }

    private OnScanListener onScanListener;

    public void setOnScanListener(OnScanListener onScanListener){
        this.onScanListener = onScanListener;
    }

    public DisturbTools(){
        context = x.app().getApplicationContext();
        activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        packageManager = context.getPackageManager();

    }

    public void ScanInstall(){
        new ScanInstall().execute();
    }



    class ScanInstall extends AsyncTask<Void,Integer,List<AppInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(onScanListener != null){
                onScanListener.onStartScanListener(context);
            }
        }

        @Override
        protected List<AppInfo> doInBackground(Void... params) {

            ArrayList<AppInfo> list = new ArrayList<>();
            list.clear();
            list.addAll(Get());
            return list;
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            if(onScanListener != null){
                onScanListener.onCompleteScanListener(context,appInfos);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(onScanListener != null){
                onScanListener.onProcessScanListener(context, values[0], values[1]);
            }
        }

        private ArrayList<AppInfo> Get(){
            ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
            List<PackageInfo> packageInfos=getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                publishProgress(i+1, packageInfos.size());
                PackageInfo pInfo=packageInfos.get(i);
                AppInfo allAppInfo=new AppInfo();
                allAppInfo.setAppName(pInfo.applicationInfo.loadLabel(getPackageManager()).toString());//应用程序的名称
                allAppInfo.setPakcageName(pInfo.packageName);//应用程序的包
                if((pInfo.applicationInfo.flags & pInfo.applicationInfo.FLAG_SYSTEM) !=0){
                    allAppInfo.isSystem = true;
                }else{
                    allAppInfo.isSystem = false;
                }
                allAppInfo.setIcon(pInfo.applicationInfo.loadIcon(getPackageManager()));//图标
                appList.add(allAppInfo);
            }
            return appList;
        }
    }

}
