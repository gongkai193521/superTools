package com.gkail.tools.util;

import android.os.Build;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;

public class DeviceTypeUtils {
    private static final String LGE = "lge";
    private static final String SAMSUNG = "samsung";
    private static final String EBEST = "ebest";
    public static final String XIAOMI = "xiaomi";
    public static final String MEIZU = "meizu";
    public static final String HUAWEI = "huawei";
    public static final String HONOR = "honor";
    public static final String NUBIA = "nubia";
    public static final String LENOVO = "lenovo";
    public static final String LETV = "letv";
    public static final String ZTE = "zte";
    public static final String COOLPAD = "coolpad";
    public static final String YULONG = "yulong";
    public static final String M360 = "360";
    public static final String QIKU = "qiku";
    public static final String GIONEE = "gionee";
    public static final String VIVO = "vivo";
    public static final String OPPO = "oppo";
    public static final String SMARTISAN = "smartisan";
    public static final String LEECO = "leeco";
    //台电
    public static final String TECLAST = "teclast";

    private static final HashMap<String, DeviceType> sDeviceMap = new HashMap();
    private static DeviceType sDeviceType;

    public static DeviceType getDeviceType() {
        sDeviceType = DeviceType.TYPE_UNKOWN;
        String var0 = Build.BRAND;
        if (!TextUtils.isEmpty(var0)) {
            String var1 = var0.toLowerCase();
            Iterator var2 = sDeviceMap.keySet().iterator();
            while (var2.hasNext()) {
                String var3 = (String) var2.next();
                if (var1.contains(var3)) {
                    sDeviceType = sDeviceMap.get(var3);
                    break;
                }
            }
        }
        return sDeviceType;
    }

    public static boolean isCoolpadDevice() {
        return getDeviceType() == DeviceType.TYPE_COOLPAD;
    }

    public static boolean isYuLongDevice() {
        return getDeviceType() == DeviceType.TYPE_YULONG;
    }

    public static boolean isOppoDevice() {
        return getDeviceType() == DeviceType.TYPE_OPPO;
    }

    public static boolean isVivoDevice() {
        return getDeviceType() == DeviceType.TYPE_VIVO;
    }

    public static boolean isXiaomiDevice() {
        return getDeviceType() == DeviceType.TYPE_XIAOMI;
    }

    public static boolean isLetvDevice() {
        return getDeviceType() == DeviceType.TYPE_LETV || getDeviceType() == DeviceType.TYPE_LEECO;
    }

    public static boolean isNubiaNX511jDevice() {
        return getDeviceType() == DeviceType.TYPE_NUBIA && "NX511J".equalsIgnoreCase(Build.MODEL);
    }

    public static boolean isMeizuDevice() {
        return getDeviceType() == DeviceType.TYPE_MEIZU;
    }

    public static boolean isZTEDevice() {
        return getDeviceType() == DeviceType.TYPE_ZTE;
    }

    public static boolean isXiaomiPadDevice() {
        return getDeviceType() == DeviceType.TYPE_XIAOMI ? Build.MODEL.equals("MI PAD") : false;
    }

    public static boolean isEBestQ7() {
        return getDeviceType() == DeviceType.TYPE_EBEST ? Build.MODEL.equalsIgnoreCase("Q7") : false;
    }

    public static boolean isHuaweiDevice() {
        return getDeviceType() == DeviceType.TYPE_HUAWEI || getDeviceType() == DeviceType.TYPE_HONOR;
    }

    public static boolean isLenovoDevice() {
        return getDeviceType() == DeviceType.TYPE_LENOVO;
    }

    public static boolean isSamsungDevice() {
        return getDeviceType() == DeviceType.TYPE_SAMSUNG;
    }

    public static boolean isSmartisanDevice() {
        return getDeviceType() == DeviceType.TYPE_SMARTISAN;
    }

    public static boolean isGioneeDevice() {
        return getDeviceType() == DeviceType.TYPE_GIONEE;
    }

    public static boolean isTeclastDevice() {
        return getDeviceType() == DeviceType.TYPE_TECLAST;
    }

    public static boolean isM360Device() {
        return getDeviceType() == DeviceType.TYPE_M360;
    }

    public static boolean isLeEcoDevice() {
        return getDeviceType() == DeviceType.TYPE_LEECO;
    }

    public static boolean isVivoY20T() {
        if ("vivo Y20T".equalsIgnoreCase(Build.MODEL.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isSMN9008S() {
        if ("SM-N9008S".equalsIgnoreCase(Build.MODEL.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isOppoA31() {
        if ("A31".equalsIgnoreCase(Build.MODEL.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isSamsungN9002() {
        if ("SM-N9002".equalsIgnoreCase(Build.MODEL.trim())) {
            return true;
        }
        return false;
    }

    static {
        sDeviceMap.put(XIAOMI, DeviceType.TYPE_XIAOMI);
        sDeviceMap.put(OPPO, DeviceType.TYPE_OPPO);
        sDeviceMap.put(VIVO, DeviceType.TYPE_VIVO);
        sDeviceMap.put(COOLPAD, DeviceType.TYPE_COOLPAD);
        sDeviceMap.put(YULONG, DeviceType.TYPE_YULONG);
        sDeviceMap.put(MEIZU, DeviceType.TYPE_MEIZU);
        sDeviceMap.put(ZTE, DeviceType.TYPE_ZTE);
        sDeviceMap.put(SAMSUNG, DeviceType.TYPE_SAMSUNG);
        sDeviceMap.put(HUAWEI, DeviceType.TYPE_HUAWEI);
        sDeviceMap.put(LGE, DeviceType.TYPE_LG);
        sDeviceMap.put(EBEST, DeviceType.TYPE_EBEST);
        sDeviceMap.put(LETV, DeviceType.TYPE_LETV);
        sDeviceMap.put(NUBIA, DeviceType.TYPE_NUBIA);
        sDeviceMap.put(GIONEE, DeviceType.TYPE_GIONEE);
        sDeviceMap.put(LENOVO, DeviceType.TYPE_LENOVO);
        sDeviceMap.put(HONOR, DeviceType.TYPE_HONOR);
        sDeviceMap.put(M360, DeviceType.TYPE_M360);
        sDeviceMap.put(QIKU, DeviceType.TYPE_QiKU);
        sDeviceMap.put(SMARTISAN, DeviceType.TYPE_SMARTISAN);
        sDeviceMap.put(LEECO, DeviceType.TYPE_LEECO);
        sDeviceMap.put(TECLAST, DeviceType.TYPE_TECLAST);
    }

    public enum DeviceType {
        TYPE_UNKOWN,
        TYPE_COOLPAD,
        TYPE_HUAWEI,
        TYPE_LG,
        TYPE_MEIZU,
        TYPE_OPPO,
        TYPE_VIVO,
        TYPE_SAMSUNG,
        TYPE_XIAOMI,
        TYPE_ZTE,
        TYPE_EBEST,
        TYPE_LETV,
        TYPE_NUBIA,
        TYPE_GIONEE,
        TYPE_LENOVO,
        TYPE_HONOR,
        TYPE_QiKU,
        TYPE_M360,
        TYPE_SMARTISAN,
        TYPE_YULONG,
        TYPE_LEECO,
        TYPE_TECLAST
    }
}