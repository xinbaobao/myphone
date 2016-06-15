package com.zzptc.zhongxin.myphone.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by zhongxin on 2016/5/8.
 */

@Table(name = "dm_mobile")

public class Mobile {

    @Column(name = "ID",isId = true)
    private int id;
    @Column(name = "MobileNumber")
    private String mobileNumber;
    @Column(name = "MobileArea")
    private String mobileArea;
    @Column(name = "MobileType")
    private String mobileType;
    @Column(name = "AreaCode")
    private String areaCode;
    @Column(name = "PostCode")
    private String postCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileArea() {
        return mobileArea;
    }

    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
