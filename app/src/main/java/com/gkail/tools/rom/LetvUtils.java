package com.gkail.tools.rom;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * 乐视
 */
public class LetvUtils {
    /**
     * 开启自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 开启受保护
     *
     * @param context
     * @return
     */
    public static boolean openProtected(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.BackgroundAppManageActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
