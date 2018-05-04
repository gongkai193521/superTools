package com.gkail.tools.base;

import android.text.TextUtils;

import java.util.Stack;

public class ActivityStackManager {
    private static Stack<BaseActivity> activityList = new Stack<>();

    public static void addActivity(BaseActivity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(BaseActivity activity) {
        activityList.remove(activity);
    }

    public static BaseActivity getLastBaseActvity() {
        return activityList.peek();
    }

    public static boolean isEmpty() {
        return activityList.size() == 0;
    }

    public static void closeActivitys(String clazz) {
        synchronized (BaseActivity.class) {
            del(activityList, clazz);
        }
    }

    private synchronized static void del(Stack<BaseActivity> list, String clazz) {
        synchronized (list) {
            for (BaseActivity mActivity : list) {
                if (TextUtils.isEmpty(clazz) || !clazz.equals(mActivity.getClass().getName())) {
                    mActivity.finish();
                }
            }
        }
    }
}
