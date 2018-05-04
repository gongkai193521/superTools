package com.gkail.tools.rom;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.gkail.tools.util.DeviceTypeUtils;


/**
 * 中兴
 */
public class ZTEUtils {
    /**
     * 开启自启动
     */
    public static boolean openAutoStart(Context context) {
        Intent intent = new Intent();
        if (DeviceTypeUtils.isYuLongDevice()) {
            try {
                ComponentName cn = new ComponentName("com.zte.heartyservice",
                        "com.zte.heartyservice.speedup.BackgroundAutorunAppActivity");
                intent.setComponent(cn);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                ComponentName cn = new ComponentName("com.zte.smartpower",
                        "com.zte.smartpower.SelfStartActivity");
                intent.setComponent(cn);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    ComponentName cn = new ComponentName("com.zte.heartyservice",
                            "com.zte.heartyservice.autorun.AppAutoRunManager");
                    intent.setComponent(cn);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    return false;
                }
            }
        }
        return true;
    }
}
