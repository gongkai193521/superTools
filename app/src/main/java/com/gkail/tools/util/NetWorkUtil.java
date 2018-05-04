package com.gkail.tools.util;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.gkail.tools.MainApplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络工具类
 */
public class NetWorkUtil {
    public enum ConnectTpye {
        WIFI, G4, G3, G2, NOCONNECT
    }

    /**
     * 检查网络连接状态
     *
     * @return boolean true连接 false未连接
     */
    public static boolean isConnNet() {
        ConnectivityManager cm = (ConnectivityManager) MainApplication.getContext().getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * 检查网络使用的类型 wifi/4G/3G/2G
     */
    public static ConnectTpye checkConnectType() {
        ConnectivityManager connectMgr = (ConnectivityManager) MainApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null) {
            return ConnectTpye.NOCONNECT;
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return ConnectTpye.WIFI;
        }
        switch (info.getSubtype()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return ConnectTpye.G2;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return ConnectTpye.G3;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return ConnectTpye.G4;
        }
        return ConnectTpye.G3;
    }

    /**
     * 判断wifi是否打开
     *
     * @return boolean
     */
    public static boolean isWifiOpen() {
        ConnectivityManager cm = (ConnectivityManager) MainApplication.getContext().getSystemService(Service.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equalsIgnoreCase("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查网络是否可以请求到服务器
     *
     * @return boolean
     */
    public static boolean hasActiveInternetConnection() {
        if (isConnNet()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) new URL("http://www.baidu.com").openConnection();
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(15000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

}
