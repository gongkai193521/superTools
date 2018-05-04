package com.gkail.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

/**
 * Created by gongkai on 18/2/27.
 */

public class MainApplication extends MultiDexApplication {
    private static Context mContext;
    public static PackageManager pm;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
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
