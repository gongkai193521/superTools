package com.gkail.tools.rom;


import android.content.Context;
import android.content.Intent;

public class QikuUtils {
    /**
     * 开启悬浮窗权限
     */
    public static boolean openFloatWindow(Context context) {
        Intent intent = new Intent();
        try {
            intent.setClassName("com.android.settings",
                    "com.android.settings.Settings$OverlaySettingsActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return CommonUtils.openApplicationDetails(context);
        }
        return true;
    }
}
