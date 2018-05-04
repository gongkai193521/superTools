package com.gkail.tools.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.gkail.tools.MainApplication;

import java.util.List;

import static com.gkail.tools.MainApplication.getPkgManager;

/**
 * 获取应用相关信息类
 */
public class AppUtils {

    /**
     * 根据包名获取应用的版本号
     *
     * @param packageName
     * @return int
     */
    public static int getVersionForPackage(String packageName) {
        try {
            PackageInfo info = getPkgManager().getPackageInfo(packageName, 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 判断是否为系统应用
     *
     * @param packageName
     * @return boolean
     */
    public static boolean isSystemApp(String packageName) {
        try {
            PackageInfo packageInfo = getPkgManager().getPackageInfo(packageName, 0);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过packagename判断应用是否安装
     *
     * @param packagename
     * @return 跳转的应用主activity Intent
     */

    public static Intent isInstall(String packagename) {
        Intent intent = null;
        try {
            intent = getPkgManager().getLaunchIntentForPackage(packagename);
        } catch (Exception e) {
            ToastUtils.showShort(MainApplication.getContext(), "没有检测到安装该APP");
        }
        return intent;
    }

    public static void installApp(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(path), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    public static boolean isActivityTop(String actName) {
        ActivityManager manager = (ActivityManager) MainApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String topActivityName = null;

        if (null != runningTaskInfos) {
            ComponentName componentName = (runningTaskInfos.get(0).topActivity);
            topActivityName = componentName.getClassName();
        }
        return actName.equals(topActivityName);
    }
}
