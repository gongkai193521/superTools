package com.viapalm.schchildpre.util;

import android.content.Context;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.viapalm.schchildpre.MainApplication;

import java.util.List;

/**
 * Created by gongkai on 18/4/17.
 */

public class InputUtils {
    //获取当前输入法
    public static String getCurrentInput() {
        String string = Settings.Secure.getString(MainApplication.getContext().getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD);
        String input = string.split("/")[0];
        return input;
    }

    //获取所有输入法名字+包名
    public static String getInput() {
        InputMethodManager imm = (InputMethodManager) MainApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> methodList = imm.getInputMethodList();
        StringBuilder builder = new StringBuilder();
        for (InputMethodInfo mi : methodList) {
            CharSequence name = mi.loadLabel(MainApplication.getContext().getPackageManager());
            String pkg = mi.getPackageName();
            builder.append(name + "/" + pkg + "\n");
        }
        return builder.toString();
    }
}
