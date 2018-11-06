package com.gkail.tools.accessibility;

import android.os.Build;

import com.gkail.tools.MainApplication;
import com.gkail.tools.R;
import com.gkail.tools.accessibility.accessibility.bean.AccessibilityBean;
import com.gkail.tools.accessibility.accessibility.bean.ClickViewBean;
import com.gkail.tools.util.DeviceTypeUtils;

import java.util.ArrayList;

/**
 * Created by gongkai on 18/6/28.
 */

public class AccessibilityData {
    public static final String button = "android.widget.Button";
    public static final String imageView = "android.widget.ImageView";
    public static final String amigoButton = "amigo.widget.AmigoButton";
    public static final String checkBox = "android.widget.CheckBox";
    public static final String leCheckBox = "com.letv.leui.widget.LeCheckBox";
    public static final String LeSwitch = "com.letv.leui.widget.LeSwitch";
    public static final String textView = "android.widget.TextView";
    public static final String checkedTextView = "android.widget.CheckedTextView";
    public static final String mSwitch = "android.widget.Switch";
    public static final String mView = "android.view.View";
    private final static String appName = MainApplication.getContext().getResources().getString(R.string.app_name);
    public static ArrayList<AccessibilityBean> datas;

    static {
        if (datas == null) {
            datas = new ArrayList<>();
            if (DeviceTypeUtils.isHuaweiDevice()) {
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$UsageAccessSettingsActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(appName, null, textView, true, false)));
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.SubSettings",
                        appName,
                        null,
                        5,
                        new ClickViewBean(null, "android:id/switchWidget||android:id/switch_widget",
                                mSwitch, true, true)));
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$AppDrawOverlaySettingsActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(null, "android:id/switchWidget||android:id/switch_widget",
                                mSwitch, true, true)));
                datas.add(new AccessibilityBean(null,
                        "android.app.Dialog",
                        "应用",
                        null,//com.android.wallpaper.livepicker:id/apply_button
                        5,
                        new ClickViewBean("应用", null,
                                button, true, true)));
            } else if (DeviceTypeUtils.isXiaomiDevice()) {
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$UsageAccessSettingsActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(appName, null, textView, true, false)));
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.SubSettings",
                        "允许访问使用记录",
                        null,
                        5,
                        new ClickViewBean(null, "android:id/checkbox", checkBox, true, true)));
                datas.add(new AccessibilityBean(null,
                        "com.miui.permcenter.permissions.PermissionsEditorActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean("显示悬浮窗", null, textView, true, false)));
                datas.add(new AccessibilityBean(null,
                        "miui.app.AlertDialog",
                        "显示悬浮窗",
                        null,
                        5,
                        new ClickViewBean("允许", null, checkedTextView, true, true)));
                datas.add(new AccessibilityBean(null,
                        "android.app.Dialog",
                        "设置壁纸",
                        null,
                        5,
                        new ClickViewBean("设置壁纸", null, button, true, true)));
            } else if (DeviceTypeUtils.isLetvDevice()) {
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$UsageAccessSettingsActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(appName, null, textView, true, false)));
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.SubSettings",
                        appName,
                        null,
                        5,
                        new ClickViewBean(null, "android:id/switchWidget", LeSwitch, true, true)));
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$AppDrawOverlaySettingsActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(null, "android:id/switchWidget", LeSwitch, true, true)));
                datas.add(new AccessibilityBean(null,
                        "com.android.wallpaper.livepicker.LiveWallpaperPreview",
                        "设为壁纸",
                        null,
                        5,
                        new ClickViewBean("设为壁纸", null, button, true, false)));

            } else if (DeviceTypeUtils.isOppoDevice()) {
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$UsageAccessSettingsActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(appName, null, textView, true, false)));
                datas.add(new AccessibilityBean(null,
                        "android.app.AlertDialog",
                        appName,
                        null,
                        5,
                        new ClickViewBean("允许", "android:id/button1", button, true, true)));
                datas.add(new AccessibilityBean(null,
                        "android.app.AlertDialog",
                        appName,
                        null,
                        5,
                        new ClickViewBean("确定", "android:id/button1", button, true, true)));
                datas.add(new AccessibilityBean(null,
                        "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(appName, null, textView, true, true)));
                datas.add(new AccessibilityBean(null,
                        "com.android.wallpaper.livepicker.LiveWallpaperPreview",
                        "应用",
                        null,
                        5,
                        new ClickViewBean("应用", null, button, true, true)));
            } else if (DeviceTypeUtils.isZTEDevice()) {
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$UsageAccessSettingsActivity",
                        appName,
                        null,
                        5,
                        new ClickViewBean(appName, null, textView, true, false)));
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.SubSettings",
                        "允许访问使用记录",
                        null,
                        5,
                        new ClickViewBean(null, "com.android.settings:id/switchWidget", mSwitch, true, true)));
                datas.add(new AccessibilityBean(null,
                        "com.android.settings.Settings$AppDrawOverlaySettingsActivity",
                        "允许在其他应用的上层显示",
                        null,
                        5,
                        new ClickViewBean(null, "com.android.settings:id/switchWidget", mSwitch, true, true)));
            } else if (DeviceTypeUtils.isVivoDevice()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    datas.add(new AccessibilityBean(null,
                            "com.android.settings.Settings$UsageAccessSettingsActivity",
                            appName,
                            null,
                            5,
                            new ClickViewBean(appName, null, textView, true, false)));
                    datas.add(new AccessibilityBean(null,
                            "com.vivo.settings.VivoSubSettings",
                            appName,
                            null,
                            5,
                            new ClickViewBean(null, "android:id/checkbox", mView, true, true)));
                    datas.add(new AccessibilityBean(null,
                            "com.vivo.permissionmanager.activity.PurviewTabActivity",
                            appName,
                            null,
                            5,
                            new ClickViewBean(appName, null, textView, true, false)));
                    datas.add(new AccessibilityBean(null,
                            "com.android.packageinstaller.permission.ui.ManagePermissionsActivity",
                            appName,
                            null,
                            5,
                            new ClickViewBean("单项权限设置", null, textView, true, false)));
                    datas.add(new AccessibilityBean(null,
                            "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity",
                            appName,
                            null,
                            5,
                            new ClickViewBean(null, "com.vivo.permissionmanager:id/move_btn", mView, true, true)));
                    datas.add(new AccessibilityBean(null,
                            "android.app.Dialog",
                            "动态壁纸",
                            null,
                            5,
                            new ClickViewBean("应用", null, mView, true, false)));
                    datas.add(new AccessibilityBean(null,
                            "com.bbk.theme.os.app.VivoContextListDialog",
                            "设定至桌面",
                            null,
                            5,
                            new ClickViewBean("设定至桌面", null, textView, true, true)));
                } else {
                    datas.add(new AccessibilityBean(null,
                            "com.iqoo.secure.MainActivity",
                            "软件管理",
                            null,
                            5,
                            new ClickViewBean("软件管理", null, textView, true, false)));
                    datas.add(new AccessibilityBean(null,
                            "com.iqoo.secure.ui.phoneoptimize.SoftwareManagerActivity",
                            "悬浮窗管理",
                            null,
                            5,
                            new ClickViewBean("悬浮窗管理", null, textView, true, false)));
                    datas.add(new AccessibilityBean(null,
                            "com.bbk.theme.livewallpaper.LocalLiveWallpaperPreview",
                            "动态壁纸",
                            null,
                            5,
                            new ClickViewBean("应用", null, textView, true, false)));
                }
            }
            datas.add(new AccessibilityBean(null,
                    "com.android.wallpaper.livepicker.LiveWallpaperPreview",
                    "设置壁纸",
                    null,
                    5,
                    new ClickViewBean("设置壁纸", null, button, true, true)));
            datas.add(new AccessibilityBean(null,
                    "com.android.wallpaper.livepicker.LiveWallpaperPreview",
                    "设置壁纸",
                    null,
                    5,
                    new ClickViewBean("设置壁纸", null, textView, true, true)));
        }
    }
}
