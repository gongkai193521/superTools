package com.viapalm.schchildpre.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSCore {
    //get the phone number from sms
    public static String GetPhoneNumberFromSMSText(String sms) {
        List<String> list = GetNumberInString(sms);
        for (String str : list) {
            if (str.length() == 11)
                return str;
        }
        return "";
    }

    public static List<String> GetNumberInString(String str) {
        List<String> list = new ArrayList<String>();
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(str);

        while (m.find()) {
            if (!"".equals(m.group()))
                list.add(m.group());
        }
        return list;
    }

    public void SendSMS(String number, String text, Context context) {
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, context.getClass()), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, text, pi, null);

    }

    public void SendSMS2(String number, String text, Context context) {
        String SENT = "sms_sent";
        String DELIVERED = "sms_delivered";
        PendingIntent sentPI = PendingIntent.getActivity(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getActivity(context, 0, new Intent(DELIVERED), 0);
        SmsManager smsm = SmsManager.getDefault();
        smsm.sendTextMessage(number, null, text, sentPI, deliveredPI);
    }
}
