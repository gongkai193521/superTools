package com.gkail.tools.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.gkail.tools.util.LogUtils;


public class HomeWatcherReceiver extends BroadcastReceiver {
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (TextUtils.isEmpty(reason)) return;
            LogUtils.f("HomeWatcherReceiver--reason == ", reason);
            switch (reason) {
                case SYSTEM_DIALOG_REASON_HOME_KEY://短按Home键
                case SYSTEM_DIALOG_REASON_ASSIST://长按Home键
                case SYSTEM_DIALOG_REASON_RECENT_APPS://最近任务
                    context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                    context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                    context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                    break;
                case SYSTEM_DIALOG_REASON_LOCK://锁屏
                    break;
                default:
                    break;
            }
        }
    }

    private static HomeWatcherReceiver mHomeReceiver = null;

    //home键广播注册
    public static void registerHomeReceiver(Context context) {
        if (mHomeReceiver == null) {
            mHomeReceiver = new HomeWatcherReceiver();
            IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.registerReceiver(mHomeReceiver, homeFilter);
        }
    }

    //home键广播注销
    public static void unregisterHomeReceiver(Context context) {
        if (null != mHomeReceiver) {
            context.unregisterReceiver(mHomeReceiver);
            mHomeReceiver = null;
        }
    }
}