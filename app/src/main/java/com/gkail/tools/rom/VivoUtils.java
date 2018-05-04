package com.gkail.tools.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2018-02-01
 */
public class VivoUtils {
    /**
     * 开启悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean openFloatWindow(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            openAutoStart(context);
        } else {
            try {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.MainActivity");
                intent.setComponent(cn);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 开启自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        Intent intent = new Intent();
        if (Build.MODEL.contains("X9")
                || Build.MODEL.contains("x9")) {
            try {
                ComponentName vivoX9 = new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.safeguard.PurviewTabActivity");
                intent.setComponent(vivoX9);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception ex) {
                return false;
            }
        } else {
            try {
                ComponentName vivo = new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.MainActivity");
                intent.setComponent(vivo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    ComponentName vivoY66 = new ComponentName("com.vivo.permissionmanager",
                            "com.vivo.permissionmanager.activity.PurviewTabActivity");
                    intent.setComponent(vivoY66);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 省电管理
     *
     * @param context
     * @return
     */
    public static boolean powerManager(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName vivoPower = new ComponentName("com.vivo.abe",
                    "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity");
            intent.setComponent(vivoPower);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 白名单管理
     *
     * @param context
     * @return
     */
    public static boolean whitelistManager(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName whitelist = new ComponentName("com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
            intent.setComponent(whitelist);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
