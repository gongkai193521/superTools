package com.gkail.tools.util;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.gkail.tools.MainApplication;


/**
 * Created by gackor on 16/4/14.
 */
public class SoundUtils {
    static AudioManager audio = (AudioManager) MainApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
    public static boolean isSilent = false;
    public static boolean isNormal = false;
    static int streamVolume;

    /**
     * 设置为静音模式
     */
    public static void setSilentModel() {
        if (isSilent) {
            return;
        }
        //静音模式
        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        ContentResolver resolver = MainApplication.getContext().getContentResolver();
        int ringerModeStreamTypes = Settings.System.getInt(resolver, Settings.System.MODE_RINGER_STREAMS_AFFECTED, 0);
        // 静音模式下不闹铃，也就是Alarm受RingerMode设置的影响
        ringerModeStreamTypes |= (1 << AudioManager.STREAM_ALARM);
        audio.setMode(ringerModeStreamTypes);
        streamVolume = audio.getStreamVolume(AudioManager.STREAM_ALARM);
        //设置音量大小
        audio.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        isSilent = true;
        isNormal = false;
    }

    public static void setNormalModel() {
        if (isNormal) {
            return;
        }
        int v = audio.getStreamVolume(AudioManager.STREAM_ALARM);
        if (v <= 0) {
            audio.setStreamVolume(AudioManager.STREAM_ALARM, streamVolume, 0);
        }
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        isNormal = true;
        isSilent = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkNotiPermission() {
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationManager notificationManager = (NotificationManager) MainApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                MainApplication.getContext().startActivity(intent);
                return false;
            }
        }
        return true;
    }
}
