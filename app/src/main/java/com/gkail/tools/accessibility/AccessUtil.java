package com.gkail.tools.accessibility;

import android.content.Intent;

import com.gkail.tools.MainApplication;

public class AccessUtil {
    /**
     * 返回到桌面
     */
    public static void backToDesk() {
        if (MainApplication.getContext() == null) {
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainApplication.getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
