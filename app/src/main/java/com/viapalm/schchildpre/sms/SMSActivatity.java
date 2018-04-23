package com.viapalm.schchildpre.sms;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viapalm.schchildpre.R;


/**
 * Created by gongkai on 16/11/2.
 */

public class SMSActivatity extends Activity implements View.OnClickListener {
    private Button sms_body, sms_num;
    SmsObserver mSmsObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        SMS_Receiver.registeSMSReceiver(this);
        initView();
    }

    private void initView() {
        sms_body = (Button) findViewById(R.id.sms_body);
        sms_num = (Button) findViewById(R.id.sms_num);
        sms_body.setOnClickListener(this);
        sms_num.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sms_body:
                sendSms();
                break;
            case R.id.sms_num:
                Toast.makeText(this, getPhoneNum(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void sendSms() {
        //发送短信
        SMSCore smscore = new SMSCore();
        smscore.SendSMS2("10086", "501", this);
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
                new String[]{/*"msg_id", "contact_id", */
                        "(select address from addr where type = 151) as address"},
                null, null, "date desc");
        if (myCursor != null) {
            myCursor.moveToFirst();
            address = myCursor.getString(myCursor.getColumnIndex("address"));
            Log.d("number", "number=" + address);
        }
        return address;
    }
}