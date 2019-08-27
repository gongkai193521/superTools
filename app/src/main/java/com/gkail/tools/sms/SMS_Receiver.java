package com.gkail.tools.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;

import com.viapalm.kidcares.wxphone.util.LogUtils;


public class SMS_Receiver extends BroadcastReceiver {
    private static final String ACTION_SMS_SEND = "lab.sodino.sms.send";
    private static final String ACTION_SMS_DELIVERY = "lab.sodino.sms.delivery";
    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.i("收到短信action", action);
        if (action.equals(ACTION_SMS_RECEIVER)) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //不知道为什么明明只有一条消息，传过来的却是数组，也许是为了处理同时同分同秒同毫秒收到多条短信
            //但这个概率有点小
            SmsMessage[] message = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            String lastAddress = "";
            for (int i = 0; i < pdus.length; i++) {
                //虽然是循环，其实pdus长度一般都是1
                message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String address = message[i].getDisplayOriginatingAddress();
                if (lastAddress.equals(address)) {
                    sb.append(message[i].getDisplayMessageBody());
                } else {
                    lastAddress = address;
                    sb.append("接收到短信来自:" + address + "\n");
                    sb.append("短信服务中心号码为：" + message[i].getServiceCenterAddress() + "\n");
                    sb.append("内容:" + message[i].getDisplayMessageBody());
                }
            }
            LogUtils.i("sms-receiver=", sb.toString());
        }
    }

    public static SMS_Receiver smsReceiver;

    public static void registeSMSReceiver(Context context) {
        if (smsReceiver == null) {
            smsReceiver = new SMS_Receiver();
            IntentFilter receiverFilter = new IntentFilter(ACTION_SMS_RECEIVER);
            receiverFilter.addAction(ACTION_SMS_RECEIVER);
            receiverFilter.addAction(ACTION_SMS_SEND);
            receiverFilter.addAction(ACTION_SMS_DELIVERY);
            context.registerReceiver(smsReceiver, receiverFilter);
        }
    }

    public static void unRegisteSMSReceiver(Context context) {
        if (smsReceiver != null) {
            context.unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }
}
