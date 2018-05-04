package com.gkail.tools.sms;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

//一个继承自ContentObserver的监听器类
class SmsObserver extends ContentObserver {
    private Context context;

    public SmsObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        //查询发送箱中的短信
        Cursor cursor = context.getContentResolver().query(Uri.parse(
                "content://sms/outbox"), new String[]{"body"}, null, null, null);
        if (null == cursor)
            return;
        //遍历查询结果获取用户正在发送的短信
        while (cursor.moveToNext()) {
            StringBuffer sb = new StringBuffer();
            //获取短信的发送地址
            sb.append("发送地址：" + cursor.getString(cursor.getColumnIndex("address")));
            //获取短信的标题
            sb.append("\n标题：" + cursor.getString(cursor.getColumnIndex("subject")));
            //获取短信的内容
            sb.append("\n内容：" + cursor.getString(cursor.getColumnIndex("body")));
            //获取短信的发件人
            sb.append("\n发件人：" + cursor.getString(cursor.getColumnIndex("person")));
            //获取短信的发送时间
            Date date = new Date(cursor.getLong(cursor.getColumnIndex("date")));
            //格式化以秒为单位的日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒");
            sb.append("\n时间：" + sdf.format(date));
            Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
            MessageTools.ShowDialog(context, sb.toString());
        }
        super.onChange(selfChange);
    }
}
