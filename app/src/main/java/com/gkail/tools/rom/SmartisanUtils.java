package com.gkail.tools.rom;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Smartisan
 */
public class SmartisanUtils {
    /**
     * 开启自启动(开启悬浮窗也调用此方法)
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.smartisanos.security",
                    "com.smartisanos.security.MainActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
