package com.gkail.tools.rom;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class SamsungUtils {
    /**
     * 开启自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        Intent intent = new Intent();
        try {
            ComponentName cn = new ComponentName("com.samsung.android.sm_cn",
                    "com.samsung.android.sm.ui.ram.AutoRunActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName cn = new ComponentName("com.samsung.android.sm",
                        "com.samsung.android.sm.ui.dashboard.SmartManagerDashBoardActivity");
                intent.setComponent(cn);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e1) {
                return false;
            }
        }
        return true;
    }
}
