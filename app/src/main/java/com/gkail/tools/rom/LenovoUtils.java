package com.gkail.tools.rom;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * 联想
 */
public class LenovoUtils {
    /**
     * 开启自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.lenovo.security",
                    "com.lenovo.security.homepage.HomePageActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
