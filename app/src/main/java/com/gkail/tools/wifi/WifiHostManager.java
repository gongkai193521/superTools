package com.gkail.tools.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.gkail.tools.util.ToastUtils;

import java.lang.reflect.Method;

/**
 * Created by gongkai on 18/5/4.
 */

public class WifiHostManager {
    private final String TAG = "WifiHostManager";
    private WifiManager wifiManager;
    private String WIFI_HOST_SSID = "AndroidAP";
    private String WIFI_HOST_PRESHARED_KEY = "12345678";// 密码必须大于8位数
    private Context mContext;

    public WifiHostManager(Context context) {
        super();
        this.mContext = context;
        //获取wifi管理服务
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 判断热点开启状态
     */
    public boolean isWifiApEnabled() {
        return getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
    }

    private WIFI_AP_STATE getWifiApState() {
        int tmp;
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            tmp = ((Integer) method.invoke(wifiManager));
            // Fix for Android 4
            if (tmp > 10) {
                tmp = tmp - 10;
            }
            return WIFI_AP_STATE.class.getEnumConstants()[tmp];
        } catch (Exception e) {
            e.printStackTrace();
            return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
        }
    }

    public enum WIFI_AP_STATE {
        WIFI_AP_STATE_DISABLING,
        WIFI_AP_STATE_DISABLED,
        WIFI_AP_STATE_ENABLING,
        WIFI_AP_STATE_ENABLED,
        WIFI_AP_STATE_FAILED
    }

    /**
     * wifi热点开关
     *
     * @param enabled true：打开  false：关闭
     * @return true：成功  false：失败
     */
    public boolean setWifiApEnabled(boolean enabled) {
        if (enabled) { // disable WiFi in any case
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = WIFI_HOST_SSID;
            //配置热点的密码
            apConfig.preSharedKey = WIFI_HOST_PRESHARED_KEY;
            //安全：WPA2_PSK
            apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            return (Boolean) method.invoke(wifiManager, apConfig, enabled);
        } catch (Exception e) {
            ToastUtils.showSingleToast(mContext, "开启热点失败");
            return false;
        }
    }

    public void startWifiAp() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        Method method = null;
        try {
            method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method.setAccessible(true);
            WifiConfiguration netConfig = new WifiConfiguration();
            netConfig.SSID = WIFI_HOST_SSID;
            netConfig.preSharedKey = WIFI_HOST_PRESHARED_KEY;
            netConfig.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);

            method.invoke(wifiManager, netConfig, true);
        } catch (Exception e) {
            ToastUtils.showSingleToast(mContext, "开启热点失败");
            Log.i(TAG, "startWifiAp: " + e.getMessage());
        }
    }

    /**
     * check whether wifi hotspot on or off
     */
    public boolean isApOn() {
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (Throwable ignored) {
        }
        return false;
    }
}
