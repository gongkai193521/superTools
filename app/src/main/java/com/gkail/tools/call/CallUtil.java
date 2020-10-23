package com.gkail.tools.call;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.gkail.tools.util.LogUtils;
import com.gkail.tools.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gongkai on 16/11/18.
 */

public class CallUtil {
    /**
     * 利用系统CallLog获取通话历史记录
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCallHistoryList(Context context, ContentResolver cr) {
        Cursor cs;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        cs = cr.query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls._ID,
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION   //通话时长
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        String callHistoryListStr = "";
        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            for (cs.moveToFirst(); !cs.isAfterLast() & i < 50; cs.moveToNext()) {
                String id = cs.getString(cs.getColumnIndex(CallLog.Calls._ID));
                String callName = cs.getString(cs.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String callNumber = cs.getString(cs.getColumnIndex(CallLog.Calls.NUMBER));
                //通话类型
                int callType = Integer.parseInt(cs.getString(cs.getColumnIndex(CallLog.Calls.TYPE)));
                String callTypeStr = "";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "未接";
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate = new Date(Long.parseLong(cs.getString((cs.getColumnIndex(CallLog.Calls.DATE)))));
                String callDateStr = sdf.format(callDate);
                //通话时长
                int callDuration = Integer.parseInt(cs.getString((cs.getColumnIndex(CallLog.Calls.DURATION))));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = min + "分" + sec + "秒";
                String callOne = "ID：" + id + "；类型：" + callTypeStr + "；称呼：" + callName + "；号码："
                        + callNumber + "；通话时长：" + callDurationStr + "；时间:" + callDateStr
                        + "\n---------------------\n";

                callHistoryListStr += callOne;
                i++;
            }
        }
        return callHistoryListStr;
    }

    /**
     * 判断是否开启电话权限
     *
     * @return
     */
    public static boolean checkCallPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showLong(context, "请开启拨打电话权限");
            return false;
        }
        return true;
    }

    //获取所有电话联系人
    public static String getCallContact(Context context) {
        Cursor cursor = null;
        StringBuilder builder = new StringBuilder();
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                    null, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " ASC");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                builder.append("contact_id：" + id + "----");
                builder.append("name：" + name + "----");
                builder.append("num：" + num + "\n");
            }
        } catch (Exception e) {
            LogUtils.d("getCallContact", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return builder.toString();
    }

    /**
     * 利用系统CallLog获取通话历史记录
     *
     * @return
     */
    public static String getCallRecord(Context context) {
        StringBuilder builder = new StringBuilder();
        Cursor cs = null;
        try {
            cs = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                    new String[]{CallLog.Calls._ID,
                            CallLog.Calls.CACHED_NAME,  //姓名
                            CallLog.Calls.NUMBER,    //号码
                            CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                            CallLog.Calls.DATE,  //拨打时间
                            CallLog.Calls.DURATION   //通话时长
                    }, null, null, CallLog.Calls._ID + " ASC");
            while (cs.moveToNext()) {
                int id = cs.getInt(cs.getColumnIndex(CallLog.Calls._ID));
                String name = cs.getString(cs.getColumnIndex(CallLog.Calls.CACHED_NAME));//称呼
                String number = cs.getString(cs.getColumnIndex(CallLog.Calls.NUMBER));//号码
                long date = Long.parseLong(cs.getString(cs.getColumnIndex(CallLog.Calls.DATE)));//拨打时间
                int duration = Integer.parseInt(cs.getString(cs.getColumnIndex(CallLog.Calls.DURATION)));//通话时长
                int type = Integer.parseInt(cs.getString(cs.getColumnIndex(CallLog.Calls.TYPE)));//通话类型
                builder.append("call_id：" + id + "\n");
                builder.append("称呼：" + name + "\n");
                builder.append("号码：" + number + "\n");
                builder.append("拨打时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "\n");
                builder.append("通话时长：" + duration + "\n");
                //1呼入/2呼出/3未接
                switch (type) {
                    case 1:
                        builder.append("通话类型：呼入" + "\n");
                        break;
                    case 2:
                        builder.append("通话类型：呼出" + "\n");
                        break;
                    case 3:
                        builder.append("通话类型：未接" + "\n");
                        break;
                    default:
                        break;
                }
                builder.append("--------------------------------------");
            }
        } catch (Exception e) {
            LogUtils.d("getCallRecord", e.getMessage());
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return builder.toString();
    }

    /*保存电话*/
    //根据电话号码查询姓名（在一个电话打过来时，如果此电话在通讯录中，则显示姓名）
    public static void savePhone(Context context, String name, String phone) {
        Uri uri = Uri.parse(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI + "/" + phone);
        ContentResolver resolver = context.getContentResolver();
        // ContactsContract.Data.DISPLAY_NAME 查询 该电话的客户姓名
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.HAS_PHONE_NUMBER}, null, null, null);
        //从raw_contact表中返回display_name
        int count = cursor.getCount();
        if (count > 0) {
            if (cursor.moveToFirst()) {
                String hasPhone = cursor.getString(0);//查询该电话有没有人
                if (TextUtils.isEmpty(hasPhone)) {//没有该电话
                    insertPhone(context, name, phone);
                } else if ("0".equals(hasPhone)) {//没有该电话
                    insertPhone(context, name, phone);
                } else {
                    ToastUtils.show(context, "该电话号码已存在!");
                }

            }
        } else {
            insertPhone(context, name, phone);
        }
        cursor.close();
    }

    public static void insertPhone(Context context, String name, String phone) {
        if (TextUtils.isEmpty(name)) {
            name = phone;
        }
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        //获取id
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        //往data表入姓名数据
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();
        //添加id
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        //添加内容类型（MIMETYPE）
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //添加名字，添加到first name位置
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        //往data表入电话数据
        context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
    }

    /*
     * 根据电话号码取得联系人姓名
     */
    public static String getContactName(Context context, String mNumber) {
        String name = "";
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,    // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + mNumber + "'", // WHERE clause.
                null,  // WHERE clause value substitution
                null);   // Sort order.
        if (cursor == null) {
            return name;
        }
        while (cursor.moveToNext()) {
            // 取得联系人名字
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            Log.i("Contacts", "" + name); // 这里提示 force close
        }
        cursor.close();
        return name;
    }
}
