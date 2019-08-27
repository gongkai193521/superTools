package com.gkail.tools.lock;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by gongkai on 17/2/13.
 */

public class AdminManageReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.i("----", "激活设备管理器了");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.i("----", "取消激活设备管理器了");
    }

    @Override
    public CharSequence onDisableRequested(final Context context, Intent intent) {
        context.stopService(intent);// 是否可以停止
        return "确认取消激活设备管理器？"; // "这是一个可选的消息，警告有关禁止用户的请求";
    }
}
