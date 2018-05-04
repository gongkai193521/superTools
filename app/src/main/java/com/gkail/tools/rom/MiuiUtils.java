package com.gkail.tools.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gkail.tools.util.AppUtils;

import java.lang.reflect.Method;

public class MiuiUtils {
    /**
     * 开启小米自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        Intent intent = new Intent();
        try {
            ComponentName cn = new ComponentName("com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName cn = new ComponentName("com.android.settings",
                        "com.android.settings.MiuiSettings");
                intent.setComponent(cn);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 小米开启悬浮窗权限
     */
    public static boolean openFloatWindow(Context context) {
        String miui = getMIUI();
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        if ("v8".equalsIgnoreCase(miui)) {// MIUI 8
            try {
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    intent.setPackage("com.miui.securitycenter");
                    intent.putExtra("extra_pkgname", context.getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e1) {
                    context.startActivity(AppUtils.isInstall("com.miui.securitycenter"));
                }
            }
        } else {
            try {// MIUI 5/6/7
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e1) {
                return CommonUtils.openApplicationDetails(context);
            }
        }
        return true;
    }

    /**
     * 关闭小米分身
     *
     * @param context
     * @return
     */
    public static boolean closeSecondSpace(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.miui.securitycore",
                    "com.miui.securityspace.ui.activity.PrivateSpaceMainActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 关闭小米长按杀进程
     *
     * @param context
     * @return
     */
    public static boolean closeKillProcess(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.android.settings",
                    "com.android.settings.MiuiSettings");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 关闭小米神隐模式
     *
     * @param context
     * @return
     */
    public static boolean closePowerHideMode(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.miui.powerkeeper",
                    "com.miui.powerkeeper.ui.PowerHideModeActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent("miui.intent.action.POWER_HIDE_MODE_APP_LIST");
                intent.setClassName("com.miui.powerkeeper",
                        "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取小米手机的MIUI系统版本号
     *
     * @return
     */
    public static String getMIUI() {
        String property = null;
        try {
            Class<?> spClazz = Class.forName("android.os.SystemProperties");
            Method method = spClazz.getDeclaredMethod("get", String.class, String.class);
            property = (String) method.invoke(spClazz, "ro.miui.ui.version.name", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("----", "MIUI_version==" + property);
        return property;
    }
}
