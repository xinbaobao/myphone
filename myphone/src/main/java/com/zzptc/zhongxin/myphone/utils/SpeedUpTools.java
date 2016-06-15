package com.zzptc.zhongxin.myphone.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.AppInfo;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongxin on 2016/5/25.
 */
public class SpeedUpTools {

    private List<AppInfo> appInfos;
    private ActivityManager activityManager;
    private final Context context;
    private final PackageManager packageManager;

    public void startClean(List<AppInfo> appInfoList) {
        new CleanAsyncTask().execute(appInfos);
    }


    public interface ScanListener{
        void ScanStartListener(Context context);
        void ScanProcessListener(Context context,int current,int total);
        void ScanCompleteListener(Context context,List<AppInfo> appInfos);

        void CleanstartListener(Context context);
        void CleancompleteListener( Context context,long cleanmemory);
    }

    private ScanListener scanListener;

    public void setScanListener(ScanListener scanListener){
        this.scanListener = scanListener;
    }


    public SpeedUpTools(){
        context = x.app().getApplicationContext();
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        packageManager = context.getPackageManager();
    }

    public void scanMobile(){
        new ScanMobile().execute();
    }

    class ScanMobile extends AsyncTask<Void,Integer,List<AppInfo>>{

        @Override
        protected List<AppInfo> doInBackground(Void... params) {

            appInfos = new ArrayList<>();

            int process = 0;

            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            for(ActivityManager.RunningAppProcessInfo info:runningAppProcesses){

                publishProgress(++process,runningAppProcesses.size());

                AppInfo appInfo = new AppInfo();
                //得到进程名
                appInfo.pakcageName = info.processName;
                /**
                 * 因为runningAppProcesses只能够得到正在运行的进程的processName  pid  uid,
                 * 所以要通过processName去获取ApplicationInfo,再通过ApplicationInfo获取其他信息
                 */

                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(info.processName, 0);
                    //图标
                    appInfo.icon = applicationInfo.loadIcon(packageManager);
                    //应用程序名称
                    appInfo.appName = applicationInfo.loadLabel(packageManager).toString();

                    //系统进程
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) !=0){
                        appInfo.isSystem = true;
                    }else{
                        appInfo.isSystem = false;
                    }
                    //当前进程
                    if(info.processName.equals(context.getPackageName())){
                        appInfo.isCurrent = true;
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    //当系统进程没有应用名称，远程服务
                    if(info.processName.contains(":")){
                        ApplicationInfo applicationInfo = getApplication(info.processName.split(":")[0]);
                        if(applicationInfo != null){
                            appInfo.icon = applicationInfo.loadIcon(packageManager);
                        }else {
                            appInfo.icon = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
                        }
                    }else {
                        appInfo.icon = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
                    }
                    appInfo.isSystem = true;
                    appInfo.appName = info.processName;
                }

                //内存
                appInfo.memory = activityManager.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty() * 1024;
                appInfos.add(appInfo);
            }

            return appInfos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(scanListener != null){
                scanListener.ScanStartListener(context);
            }
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            for(AppInfo appInfo:appInfos){
                if(!appInfo.isSystem && !appInfo.isCurrent){
                    Log.e("ceshi",appInfo.appName + "@@@@@@@@@@@" + appInfo.pakcageName);
                }
            }
            if(scanListener != null){
                scanListener.ScanCompleteListener(context, appInfos);
            }
            super.onPostExecute(appInfos);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(scanListener != null){
                scanListener.ScanProcessListener(context,values[0],values[1]);
            }
        }
    }

    private ApplicationInfo getApplication(String processName){

        if(processName == null){
            return null;
        }

        List<ApplicationInfo> applicationInfos = packageManager.
                getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(ApplicationInfo applicationInfo:applicationInfos){
            if(processName.equals(applicationInfo.processName)){
                return applicationInfo;
            }
        }

        return null;
    }

    /**
     * 杀掉后台进程
     */
    public void killProcess(String packsgeName){
        String process = packsgeName;
        if(packsgeName.contains(":")){
            process = packsgeName.split(":")[0];
        }
        activityManager.killBackgroundProcesses(process);
    }

    public class CleanAsyncTask extends AsyncTask<List<AppInfo>,Void,Long>{

        private long availMemory;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(scanListener != null){
                scanListener.CleanstartListener(context);
            }
        }

        @Override
        protected Long doInBackground(List<AppInfo>... params) {

            List<AppInfo> appInfos = params[0];
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
            long beforeMemory = getAvailMemory();
            if(runningAppProcessInfos != null){
                for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos){
                    for(AppInfo appInfo : appInfos){
                        if(appInfo.isChecked && appInfo.pakcageName.equals(runningAppProcessInfo.processName)){
                            killProcess(appInfo.pakcageName);
                        }
                    }
                }
            }
            long afterMemory = getAvailMemory();
            return afterMemory - beforeMemory;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if(scanListener != null){
                scanListener.CleancompleteListener(context,aLong);
            }
        }



        public long getAvailMemory() {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            return memoryInfo.availMem;
        }
    }


}
