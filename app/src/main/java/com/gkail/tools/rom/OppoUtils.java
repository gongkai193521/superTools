package com.gkail.tools.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2018-02-01
 */
public class OppoUtils {
    /**
     * 开启悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean openFloatWindow(Context context) {
        Intent intent = new Intent();
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.coloros.safecenter",
                    "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName comp = new ComponentName("com.coloros.safecenter",
                        "com.coloros.privacypermissionsentry.PermissionTopActivity");
                intent.setComponent(comp);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 开启oppp自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        Intent intent = new Intent();
        try {
            ComponentName componentName = new ComponentName("com.coloros.safecenter",
                    "com.coloros.safecenter.startupapp.StartupAppListActivity");
            intent.setComponent(componentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName componentName = new ComponentName("com.coloros.safecenter",
                        "com.coloros.privacypermissionsentry.PermissionTopActivity");
                intent.setComponent(componentName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception ex) {
                try {
                    ComponentName componentName = new ComponentName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    intent.setComponent(componentName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e1) {
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
            ComponentName oppoPower = new ComponentName("com.coloros.oppoguardelf",
                    "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity");
            intent.setComponent(oppoPower);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 桌面加速球
     *
     * @param context
     * @return
     */
    public static boolean closeBall(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName oppoPower = new ComponentName("com.coloros.safecenter",
                    "com.coloros.safecenter.settings.SecureSafeMainSettingsActivity");
            intent.setComponent(oppoPower);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
