package com.gkail.tools.accessibility;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.gkail.tools.MainApplication;
import com.gkail.tools.accessibility.accessibility.bean.AccessibilityBean;
import com.gkail.tools.util.DeviceTypeUtils;
import com.gkail.tools.util.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * Created by g on 17/7/28.
 */

public class AccessibilityManager {
    private static final String ACCESSIBILITY_DATAS = "accessibility_datas";
    private static final String ACCESSIBILITY_STATE = "ACCESSIBILITY_STATE";
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
     * 0表示没有赋值，1表示辅助功能开始工作，2表示辅助功能停止工作。
     */
    private int state = 0;

    public void start() {
        state = 1;
        SharedPreferencesUtils.putValue(ACCESSIBILITY_STATE, state);
    }

    public void stop() {
        state = 2;
        SharedPreferencesUtils.putValue(ACCESSIBILITY_STATE, state);
    }

    public int getState() {
        if (state == 0) {
            state = SharedPreferencesUtils.getValue(ACCESSIBILITY_STATE, 2, Integer.class);
        }
        return state;
    }

    private static int isAccessibilityOpen = 0;//是否开启辅助功能,0表示没有赋值，1表示开启，2表示没开启

    /**
     * 更新辅助功能开启状态
     *
     * @param flag
     */
    public void setAccessibility(int flag) {
        isAccessibilityOpen = flag;
    }

    /**
     * 获取辅助功能是否开启
     *
     * @return
     */
    public boolean getAccessibility() {
        if (isAccessibilityOpen == 0) {
            if (isOpen()) {
                isAccessibilityOpen = 1;
            } else {
                isAccessibilityOpen = 2;
            }
        }
        return isAccessibilityOpen == 1;
    }

    /**
     * 所有辅助功能页面
     */
    private ArrayList<AccessibilityBean> datas;
    /**
     * 根据包名类名匹配的数据
     */
    private ArrayList<AccessibilityBean> tempList;

    public void setData(ArrayList<AccessibilityBean> datas) {
        synchronized (this) {
            this.datas = datas;
            SharedPreferencesUtils.putValue(ACCESSIBILITY_DATAS, datas);
        }
    }

    public ArrayList<AccessibilityBean> getDatas() {
//        if (datas == null) {
//            datas = SharedPreferencesUtils.getValues(ACCESSIBILITY_DATAS, new ArrayList<AccessibilityBean>(), new TypeToken<ArrayList<AccessibilityBean>>() {
//            }.getType());
//        }
        return datas;
    }

    public ArrayList<AccessibilityBean> getContainList(String pkgName, String clsName) {
        if (tempList == null) {
            tempList = new ArrayList<>();
        } else {
            tempList.clear();
        }
        for (AccessibilityBean bean : getDatas()) {
            if (!TextUtils.isEmpty(bean.getPkgName()) && !TextUtils.isEmpty(bean.getClassName())) {
                if (bean.getPkgName().equals(pkgName) && bean.getClassName().equals(clsName)) {
                    tempList.add(bean);
                }
            } else if (!TextUtils.isEmpty(bean.getPkgName()) && bean.getPkgName().equals(pkgName)) {
                tempList.add(bean);
            } else if (!TextUtils.isEmpty(bean.getClassName()) && bean.getClassName().equals(clsName)) {
                tempList.add(bean);
            }
        }


        return tempList;
    }


    /**
     * 判断辅助功能是否选上
     */
    public boolean isOpen() {
        int accessibilityEnabled = 0;
        Context mContext = MainApplication.getContext();
        final String service = mContext.getPackageName() + "/" + com.gkail.tools.accessibility.accessibility.MyAccessibilityService.class.getCanonicalName();
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
        if (accessContext != null) {
            AccessibilityNodeInfo rootInActiveWindow = accessContext.getRootInActiveWindow();
            if (rootInActiveWindow != null) {
                return true;
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

    public void clear() {
        datas = null;
        tempList = null;
        SharedPreferencesUtils.putValue(ACCESSIBILITY_DATAS, null);
    }


    com.gkail.tools.accessibility.accessibility.MyAccessibilityService accessContext;

    public void setContext(com.gkail.tools.accessibility.accessibility.MyAccessibilityService accessContext) {
        this.accessContext = accessContext;
    }
}
