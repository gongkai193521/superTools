package com.viapalm.schchildpre.sms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SMS_Receiver extends BroadcastReceiver {
    private static final String ACTION_SMS_SEND = "lab.sodino.sms.send";
    private static final String ACTION_SMS_DELIVERY = "lab.sodino.sms.delivery";
    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";
    final String GetNumberAddress = "10086";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.parse("content://sms/inbox");
            ContentResolver smsResolver = context.getContentResolver();
            // 获取sms表中_id最大值，即最新收到短信的_id值
            Cursor idCursor = smsResolver.query(uri, new String[]{"count(_id)"}, null,
                    null, null);
            idCursor.moveToNext();
            int _id = idCursor.getInt(0);
            idCursor.close();
            Cursor smsCursor = smsResolver.query(uri, new String[]{"address",
                    "body"}, "_id = ?", new String[]{"" + _id}, null);
            if (smsCursor != null) {
                smsCursor.moveToNext();
                String address = smsCursor.getString(0);
                String body = smsCursor.getString(1);

                Log.i("----", " == " + _id + "," + address + ":" + body);
            }


            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //不知道为什么明明只有一条消息，传过来的却是数组，也许是为了处理同时同分同秒同毫秒收到多条短信
            //但这个概率有点小
            SmsMessage[] message = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            String address = "";
            for (int i = 0; i < pdus.length; i++) {
                Log.i("----", " message== " + message[i]);
                //虽然是循环，其实pdus长度一般都是1
                message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append("接收到短信来自:\n");
                address = message[i].getDisplayOriginatingAddress();
                sb.append(address + "\n");
                sb.append("内容:" + message[i].getDisplayMessageBody());
                sb.append("\n短信服务中心号码为：" + message[i].getServiceCenterAddress());
            }
            String phoneNumber = null;
            if (address.equals(GetNumberAddress)) {
                phoneNumber = SMSCore.GetPhoneNumberFromSMSText(sb.toString());
                Log.i("----", "SMSCore.PhoneNumber == " + phoneNumber);
            }
            MessageTools.ShowDialog(context, phoneNumber);
        }
    }

    public static SMS_Receiver smsReceiver;

    public static void registeSMSReceiver(Context context) {
        smsReceiver = new SMS_Receiver();
        IntentFilter receiverFilter = new IntentFilter(ACTION_SMS_RECEIVER);
        context.registerReceiver(smsReceiver, receiverFilter);
    }

    public static void unRegisteSMSReceiver(Context context) {
        if (smsReceiver != null) {
            context.unregisterReceiver(smsReceiver);
        }
    }

    private Uri SMS_INBOX = Uri.parse("content://sms/");

    public void getSmsFromPhone(Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
        String where = " address = '1066321332' AND date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            //这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
            Matcher matcher = pattern.matcher(body);
            Log.i("----", " == " + number + name + body);
            if (matcher.find()) {
                String res = matcher.group().substring(1, 11);
            }
        }
    }

}
