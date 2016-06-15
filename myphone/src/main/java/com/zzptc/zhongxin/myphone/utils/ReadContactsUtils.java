package com.zzptc.zhongxin.myphone.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.github.lzyzsd.randomcolor.RandomColor;
import com.zzptc.zhongxin.myphone.bean.Contact;
import com.zzptc.zhongxin.myphone.bean.Mobile;

import org.apache.commons.io.FileUtils;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongxin on 2016/5/4.
 */
public class ReadContactsUtils {

    public static List<Contact> contacts = new ArrayList<>();

    public static List<Contact> getContacts() {
        return contacts;
    }

    public static void setContacts(List<Contact> contacts) {
        ReadContactsUtils.contacts = contacts;
    }

    /**读取手机通讯录
     * 1、将数据库文件导入到项目的 assets 目录下，然后 将数据库拷贝到系统数据库路径 data/data/packagename/databases/
     * 2、首先读取系统通讯录
     * 3、根据系统通讯录中的电话号码，查询外部数据库，得到手机归属地
     * 4、添加集合当中
     */


//文件夹路径
    public static String databasePath = "data/data/%s/databases";
    //创建存放数据库的文件夹
    public static String getDatabasePath(Context context){
        return String.format(databasePath,context.getPackageName());
    }
//创建存放数据库的文件
    public static String getDatabaseFile(Context context,String dbFile){
        return getDatabasePath(context) + "/" + dbFile;
    }


    public static void copyDatabase(Context context){
        //判断文件夹是否存在，当文件夹不存在时，重新创建文件夹
        File fileDir = new File(getDatabasePath(context));
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
//判断文件是否存在，当文件不存在时，重新创建文件
        File dbFile = new File(getDatabaseFile(context,"mobile.db"));
        if(!dbFile.exists()){
            try {
                dbFile.createNewFile();
//拷贝数据
                URL url = context.getClass().getClassLoader().getResource("assets/mobile.db");
                FileUtils.copyURLToFile(url,dbFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //读取系统通讯录   系统通讯录以ContentProvider()对外提供数据，所以我们使用ContentResolver()读取数据
    //首先创建Contact类
    public static List<Contact> queryAllContacts(Context context){
        List<Contact> contacts = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();


        //首先查raw_contact  id  display_name  has_phone
        //要据上一张表，首先要判断是否有电话号码，只有在有电话号码的情况下，再去查询 ContactsContract.CommonDataKinds.Phone
        //查询 ContactsContract.CommonDataKinds.Phone同时，根据电话号码 ，查询手机归属地的表


        //查询完成，添加到集合                           查询的url                       查询的字段落  条件  条件参数  排序
        Cursor raw_cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        while (raw_cursor != null && raw_cursor.moveToNext()){
            Contact contact = new Contact();
//查询id
            int id = raw_cursor.getInt(raw_cursor.getColumnIndex(ContactsContract.Contacts._ID));
            contact.setContactId(id);
//查询姓名
            String name = raw_cursor.getString(raw_cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setName(name);
//查询电话号码
            int has_phone_number = raw_cursor.getInt(raw_cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//判断是否有电话号码
            String phone = getPhone(id,has_phone_number,contentResolver);
            if(phone != null){
                contact.setPhone(phone);

                //根据手机号码查询归属地  xutils3
                String phoneAttribute = getPhoneAttribute(phone);
                contact.setAttribute(phoneAttribute);

                RandomColor randomColor = new RandomColor();
                int color = randomColor.randomColor();

                contact.setHeadColor(color);

            }
            contacts.add(contact);
        }
        raw_cursor.close();

        return contacts;
    }

//判断是否有电话号码   有电话号码就查询ContactsContract.CommonDataKinds.Phone
    public static String getPhone(int id,int has_phone_number,ContentResolver contentResolver){
        if(has_phone_number > 0){
            Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",new String[]{id+""},null);
//if   moveToFirst()  默认情况下查询联系人的第一个电话号码
            //如果需要查询所有号码就要将contact中得到String phone 改为  List<String> phone;  while  moveToNext()
            if(cursor != null && cursor.moveToFirst()){
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //去除掉手机号码中的空格和横杠  比如把 1 378-916-7406 变成 13789167406
                return phone.replace(" ","").replace("-","");
            }
            cursor.close();
        }
        return null;
    }

    private static String getPhoneAttribute(String phone){

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("mobile.db")
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
        DbManager dbManager = x.getDb(daoConfig);

        //运用正则表达式判断是否为手机号码
        if(phone.matches("^1[34578]\\d{9}$")){
            try {
                Mobile mobile = dbManager.selector(Mobile.class).where("MobileNumber","=",phone.substring(0,7)).findFirst();

                if(mobile != null){
                    return mobile.getMobileArea() + "\n" + mobile.getMobileType();
                }else {
                    return "未知号码";
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        return "座机号码";
    }

}
