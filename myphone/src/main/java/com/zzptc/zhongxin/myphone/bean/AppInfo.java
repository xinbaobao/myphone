package com.zzptc.zhongxin.myphone.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by zhongxin on 2016/5/25.
 */

@Table(name = "AppInfo")
public class AppInfo implements Parcelable {
    //id
    @Column(name = "_id",isId = true)
    public Integer processId;
    //图标
    public Drawable icon;
    //应用程序名称
    @Column(name = "appName")
    public String appName;
    //包名
    @Column(name = "pakcageName")
    public String pakcageName;
    //内存
    public long memory;
    //系统进程
    public boolean isSystem;
    //当前进程
    public boolean isCurrent = false;
    //复选框状态
    @Column(name = "isChecked")
    public boolean isChecked = true;

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPakcageName() {
        return pakcageName;
    }

    public void setPakcageName(String pakcageName) {
        this.pakcageName = pakcageName;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public AppInfo(){

    }

    protected AppInfo(Parcel in) {
        appName = in.readString();
        pakcageName = in.readString();
        memory = in.readLong();
        isSystem = in.readByte() != 0;
        isCurrent = in.readByte() != 0;
        isChecked = in.readByte() != 0;
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(pakcageName);
        dest.writeLong(memory);
        dest.writeByte((byte) (isSystem ? 1 : 0));
        dest.writeByte((byte) (isCurrent ? 1 : 0));
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
