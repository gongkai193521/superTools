package com.gkail.tools.sms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gkail.tools.R;
import com.gkail.tools.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Consumer;


/**
 * Created by gongkai on 16/11/2.
 */

public class SMSActivatity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_send;
    private EditText et_addressee, et_content;
    private TextView tv_sms;
    private SmsObserver mSmsObserver;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        SMS_Receiver.registeSMSReceiver(this);
        rxPermissions = new RxPermissions(this);
        initView();
        rxPermissions.request(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    ToastUtils.show(SMSActivatity.this, "已获取权限");
                    tv_sms.setText(getSmsInPhone());
                } else {
                }
            }
        });
    }

    private void initView() {
        bt_send = findViewById(R.id.bt_send);
        tv_sms = findViewById(R.id.tv_sms);
        et_addressee = findViewById(R.id.et_addressee);
        et_content = findViewById(R.id.et_content);
        bt_send.setOnClickListener(this);
        rxPermissions = new RxPermissions(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                sendSms();
                break;
            default:
                break;
        }
    }

    /**
     * 发送短信
     */
    private void sendSms() {
        String addressee = et_addressee.getText().toString();
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(addressee) || TextUtils.isEmpty(content)) {
            ToastUtils.show(SMSActivatity.this, "收信人和短信内容不能为空");
            return;
        }
        SMSCore smscore = new SMSCore();
        smscore.SendSMS2(addressee, content, this);
        mSmsObserver = new SmsObserver(new Handler(), this);
        getContentResolver().registerContentObserver(Uri.parse
                ("content://sms"), true, mSmsObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMS_Receiver.unRegisteSMSReceiver(this);
        if (mSmsObserver != null) {
            getContentResolver().unregisterContentObserver(mSmsObserver);
        }
    }

    //只要发过短信就可以获取到手机号
    private String getPhoneNum() {
        String address = null;
        Cursor myCursor = getContentResolver().query(Uri.parse("content://sms"),
                new String[]{"(select address from addr where type = 151) as address"},
                null, null, "date desc");
        if (myCursor != null) {
            myCursor.moveToFirst();
            address = myCursor.getString(myCursor.getColumnIndex("address"));
            Log.d("number", "number=" + address);
        }
        return address;
    }

    private int smsCount;

    @SuppressLint("LongLogTag")
    public String getSmsInPhone() {
        final String SMS_URI_ALL = "content://sms/"; // 所有短信
        final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
        final String SMS_URI_SEND = "content://sms/sent"; // 已发送
        final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
        final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
        final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
        final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type",};
            Cursor cur = getContentResolver().query(uri, projection, null,
                    null, "date desc"); // 获取手机内部短信
            // 获取短信中最新的未读短信
            // Cursor cur = getContentResolver().query(uri, projection,
            // "read = ?", new String[]{"0"}, "date desc");
            smsCount = cur.getColumnCount();
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);
                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else if (intType == 3) {
                        strType = "草稿";
                    } else if (intType == 4) {
                        strType = "发件箱";
                    } else if (intType == 5) {
                        strType = "发送失败";
                    } else if (intType == 6) {
                        strType = "待发送列表";
                    } else if (intType == 0) {
                        strType = "所以短信";
                    } else {
                        strType = "null";
                    }
                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());
                if (!cur.isClosed()) {
                    cur.close();
                }
            } else {
                smsBuilder.append("no result!");
            }
            smsBuilder.append("getSmsInPhone has executed!");
        } catch (SQLiteException ex) {
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        return smsBuilder.toString();
    }

}