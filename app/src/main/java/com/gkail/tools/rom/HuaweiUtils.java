package com.gkail.tools.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

public class HuaweiUtils {
    /**
     * 获取华为手机的EMUI系统版本号
     *
     * @return
     */
    public static String getEMUI() {
        String buildVersion = null;
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            buildVersion = (String) getMethod.invoke(classType, new Object[]{"ro.build.version.emui"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("----", "EMUI_version==" + buildVersion);
        return buildVersion;
    }

    /**
     * 开启自启动
     *
     * @param context
     * @return
     */
    public static boolean openAutoStart(Context context) {
        Intent intent = new Intent();
        try {
            ComponentName cn = new ComponentName("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName cn = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
                intent.setComponent(cn);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e1) {
                try {
                    ComponentName cn = new ComponentName("com.huawei.systemmanager",
                            "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity");
                    intent.setComponent(cn);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e2) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 开启受保护
     */
    public static boolean openProtected(Context context) {
        Intent intent = new Intent();
        try {
            ComponentName cn = new ComponentName("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity");
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName cn = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.mainscreen.MainScreenActivity");
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


