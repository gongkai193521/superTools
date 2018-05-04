package com.gkail.tools.rom;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * 金立
 */
public class GioneeUtils {
    /**
     * 开启自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.gionee.softmanager",
                    "com.gionee.softmanager.MainActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
