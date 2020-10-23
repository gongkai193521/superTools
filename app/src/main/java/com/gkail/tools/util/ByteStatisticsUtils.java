package com.gkail.tools.util;

import android.Manifest;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.gkail.tools.MainApplication;

import java.text.NumberFormat;

/**
 * Created by gongkai on 2020-02-04
 */
public class ByteStatisticsUtils {
    /**
     * 获取所有应用指定时间段的蜂窝网络的流量
     *
     * @param context
     * @param subscriberId
     * @param startTime
     * @param endTime
     * @return
     */
    public static long getAllMobileBytes(Context context, String subscriberId, long startTime, long endTime) {
        NetworkStats.Bucket bucket;
        try {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
            bucket = networkStatsManager.querySummaryForDevice(NetworkCapabilities.TRANSPORT_CELLULAR,
                    subscriberId,
                    startTime,
                    endTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes() + bucket.getRxBytes();
    }

    /**
     * 获取所有应用指定时间段的wifi网络的流量
     *
     * @param context
     * @param startTime
     * @param endTime
     * @return
     */
    public static long getAllWifiBytes(Context context, long startTime, long endTime) {
        NetworkStats.Bucket bucket = null;
        try {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
            bucket = networkStatsManager.querySummaryForDevice(NetworkCapabilities.TRANSPORT_WIFI,
                    "",
                    startTime,
                    endTime);
        } catch (RemoteException e) {
        }
        return bucket.getRxBytes() + bucket.getTxBytes();
    }

    /**
     * 根据uid获取指定时间段wifi网络的流量
     *
     * @param context
     * @param startTime
     * @param endTime
     * @param uid
     * @return
     */
    public static long getWifiBytesForUid(Context context, long startTime, long endTime, int uid) {
        long summaryTotal = 0;
        NetworkStats.Bucket summaryBucket = new NetworkStats.Bucket();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            NetworkStats summaryStats = networkStatsManager.querySummary(NetworkCapabilities.TRANSPORT_WIFI,
                    "", startTime, endTime);
            do {
                summaryStats.getNextBucket(summaryBucket);
                if (uid == summaryBucket.getUid()) {
                    summaryTotal += summaryBucket.getRxBytes() + summaryBucket.getTxBytes();
                }
            } while (summaryStats.hasNextBucket());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return summaryTotal;
    }

    /**
     * 根据uid获取指定时间段蜂窝网络的流量
     *
     * @param context
     * @param subscriberId
     * @param startTime
     * @param endTime
     * @param uid
     * @return
     */
    public static long getMobileBytesForUid(Context context, String subscriberId, long startTime, long endTime, int uid) {
        long summaryTotal = 0;
        NetworkStats.Bucket summaryBucket = new NetworkStats.Bucket();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            NetworkStats summaryStats = networkStatsManager.querySummary(NetworkCapabilities.TRANSPORT_CELLULAR,
                    subscriberId, startTime, endTime);
            do {
                summaryStats.getNextBucket(summaryBucket);
                if (uid == summaryBucket.getUid()) {
                    summaryTotal += summaryBucket.getRxBytes() + summaryBucket.getTxBytes();
                }
            } while (summaryStats.hasNextBucket());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return summaryTotal;
    }

    public static int getUidByPkg(String packageName) {
        int uid = -1;
        try {
            PackageInfo packageInfo = MainApplication.getPkgManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return uid;
    }

    private static String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return tm.getSubscriberId();
            }
        }
        return "";
    }

    public static String formatBytes(long bytes) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);//不足两位不补0，超过两位部分的自动舍去
        String string = "";
        if (bytes < 1024) {
            string = bytes + "B";
        } else if (bytes < (1024 * 1024)) {
            string = format.format((double) bytes / 1024) + "KB";
        } else if (bytes < (1024 * 1024 * 1024)) {
            string = format.format((double) bytes / 1024 / 1024) + "MB";
        } else {
            string = format.format((double) bytes / 1024 / 1024 / 1024) + "GB";
        }
        return string;
    }
}
