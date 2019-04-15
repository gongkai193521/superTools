package com.gkail.tools.accessibility;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

import com.gkail.tools.MainApplication;
import com.gkail.tools.util.DeviceTypeUtils;

/**
 * Created by g on 17/7/28.
 */

public class AccessibilityManager {
    private static AccessibilityManager mInstance = null;

    public static AccessibilityManager getInstance() {
        if (mInstance == null) {
            synchronized (AccessibilityManager.class) {
                if (mInstance == null) {
                    mInstance = new AccessibilityManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 判断辅助功能是否开启
     */
    public boolean isOpen() {
        int accessibilityEnabled = 0;
        Context mContext = MainApplication.getContext();
        final String service = mContext.getPackageName() + "/" + MyAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 跳转到开启辅助功能页面
     */
    public void toSet() {
        try {
            Intent intent = new Intent();
            if (DeviceTypeUtils.isXiaomiPadDevice()) {
                intent.setClassName("com.android.settings",
                        "com.android.settings.MiuiSettings");
            } else {
                intent.setClassName("com.android.settings",
                        "com.android.settings.Settings$AccessibilitySettingsActivity");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainApplication.getContext().startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainApplication.getContext().startActivity(intent);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }
}
