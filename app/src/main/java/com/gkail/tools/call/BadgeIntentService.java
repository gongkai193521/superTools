package com.gkail.tools.call;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.gkail.tools.BuildConfig;
import com.gkail.tools.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BadgeIntentService extends IntentService {
    private static final String NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID;
    private int notificationId = 0;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int badgeCount = intent.getIntExtra("badgeCount", 0);
            mNotificationManager.cancel(notificationId);
            notificationId++;
            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText("显示角标通知" + badgeCount)
                    .setSmallIcon(R.drawable.icon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupNotificationChannel();
                builder.setChannelId(NOTIFICATION_CHANNEL);
            }
            Notification notification = builder.build();
            applyNotification(notification, badgeCount);
            mNotificationManager.notify(notificationId, notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT);
        mNotificationManager.createNotificationChannel(channel);
    }

    /**
     * @param notification
     * @param badgeCount
     */
    public static void applyNotification(Notification notification, int badgeCount) {
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            try {
                Field field = notification.getClass().getDeclaredField("extraNotification");
                Object extraNotification = field.get(notification);
                Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
                method.invoke(extraNotification, badgeCount);
            } catch (Exception e) {
                Log.d("Unable to execute badge", e.getMessage());
            }
        }
    }
}
