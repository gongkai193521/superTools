package com.gkail.tools.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

public class MeizuUtils {
    public static String getFlyme() {
        String buildVersion = null;
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            buildVersion = (String) getMethod.invoke(classType, new Object[]{"ro.build.display.id"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("----", "Flyme_version==" + buildVersion);
        return buildVersion;
    }

    /**
     * 开启魅族悬浮窗权限
     */
    public static boolean openFloatWindow(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // 最新的魅族flyme 6.2.5 用上述方法获取权限失败, 不过又可以用下述方法获取权限了
            if (!CommonUtils.openFloatWindow(context)) {
                return CommonUtils.openApplicationDetails(context);
            }
        }
        return true;
    }

    /**
     * 开启魅族自启动
     */
    public static boolean openAutoStart(Context context) {
        Intent intent = new Intent();
        try {
            ComponentName cn = new ComponentName("com.meizu.safe",
                    "com.meizu.safe.permission.SmartBGActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName cn = new ComponentName("com.meizu.safe",
                        "com.meizu.safe.permission.PermissionMainActivity");
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
