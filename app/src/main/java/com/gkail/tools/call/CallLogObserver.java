package com.gkail.tools.call;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

/**
 * 监听通话记录数据库改变
 */
public class CallLogObserver extends ContentObserver {

    public CallLogObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i("CallLogObserver", "onChange()");
    }
}
