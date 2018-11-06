package com.gkail.tools.accessibility.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.gkail.tools.util.LogUtils;


/**
 * Created by Administrator on 2015/5/12 0012.
 */
public class PolicyAdminReceiver extends DeviceAdminReceiver {

    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        LogUtils.d("激活设备管理器了");
    }

    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        LogUtils.d("取消激活设备管理器了");
    }

    @Override
    public CharSequence onDisableRequested(final Context context, Intent intent) {
        context.stopService(intent);// 是否可以停止
        return "确认取消激活设备管理器？"; // "这是一个可选的消息，警告有关禁止用户的请求";
    }
}
