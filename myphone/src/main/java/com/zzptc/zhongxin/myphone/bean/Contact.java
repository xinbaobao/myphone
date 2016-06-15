package com.zzptc.zhongxin.myphone.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by zhongxin on 2016/5/4.
 */

@Table(name = "urgentContacts")
public class Contact implements java.io.Serializable {

    @Column(name = "id",isId = true)
    private int contactId;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    private String attribute;
    @Column(name = "helpMessage")
    private String helpMessage;

    public String getHelpMessage() {
        return helpMessage;
    }

    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }

    private int headColor;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getHeadColor() {
        return headColor;
    }

    public void setHeadColor(int headColor) {
        this.headColor = headColor;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", attribute='" + attribute + '\'' +
                ", headColor=" + headColor +
                '}';
    }
}
