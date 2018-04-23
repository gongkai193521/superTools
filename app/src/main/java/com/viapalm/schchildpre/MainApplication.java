package com.viapalm.schchildpre;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by gongkai on 18/2/27.
 */

public class MainApplication extends MultiDexApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
