package com.gkail.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.provider.CallLog;
import android.support.multidex.MultiDexApplication;
import android.view.WindowManager;

import com.gkail.tools.call.CallLogObserver;

/**
 * Created by gongkai on 18/2/27.
 */

public class MainApplication extends MultiDexApplication {
    private static Context mContext;
    public static PackageManager pm;
    Intent intent;
    int resultCode;
    private MediaProjectionManager mMediaProjectionManager;

    public MediaProjectionManager getmMediaProjectionManager() {
        return mMediaProjectionManager;
    }

    public void setmMediaProjectionManager(MediaProjectionManager mMediaProjectionManager) {
        this.mMediaProjectionManager = mMediaProjectionManager;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    private static WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,
                true, new CallLogObserver(new Handler()));
    }

    public static WindowManager.LayoutParams getLayoutParams() {
        return mParams;
    }

    public static PackageManager getPkgManager() {
        if (pm == null) {
            pm = mContext.getPackageManager();
        }
        return pm;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
