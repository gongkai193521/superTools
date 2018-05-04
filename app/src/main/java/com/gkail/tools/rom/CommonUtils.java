package com.gkail.tools.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;

import static android.content.Context.APP_OPS_SERVICE;

/**
 * Created by gongkai on 18/3/6.
 */

public class CommonUtils {
    /**
     * 检测悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Log.i("悬浮窗权限", checkAppOps(context, 24) + "");
            Log.i("悬浮窗权限", checkAppOps(context) + "");
            return checkAppOps(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        }
        return true;
    }

    /**
     * 跳转到应用详情页面
     *
     * @param context
     */
    public static boolean openApplicationDetails(Context context) {
        try {
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.i("----", "权限跳转失败！");
            return false;
        }
        return true;
    }

    /**
     * 开启悬浮窗权限
     *
     * @param context
     */
    public static boolean openFloatWindow(Context context) {
        try {
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageURI);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.i("----", "权限跳转失败！");
            return false;
        }
        return true;
    }

    /**
     * 开启允许查看应用权限
     *
     * @param context
     */
    public static boolean openUsageSetting(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkAppOps(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean checkAppOps(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            try {
                Object object = context.getSystemService(APP_OPS_SERVICE);
                if (object == null) {
                    return false;
                }
                Class localClass = object.getClass();
                Class[] arrayOfClass = new Class[3];
                arrayOfClass[0] = Integer.TYPE;
                arrayOfClass[1] = Integer.TYPE;
                arrayOfClass[2] = String.class;
                Method method = localClass.getMethod("checkOp", arrayOfClass);
                if (method == null) {
                    return false;
                }
                Object[] arrayOfObject1 = new Object[3];
                arrayOfObject1[0] = Integer.valueOf(24);
                arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
                arrayOfObject1[2] = context.getPackageName();
                int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
                return m == AppOpsManager.MODE_ALLOWED;
            } catch (Exception ex) {
                return true;
            }
        }
        return false;
    }

}
