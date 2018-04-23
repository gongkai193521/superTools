package com.viapalm.schchildpre.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ActionMenuView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 自定义土司工具
 */
public class ToastUtils {
    /**
     * 自定义位置的可设长短时间显示的Toast
     *
     * @param context
     * @param tip
     * @param isLongTime
     * @param gravity
     */
    public static void show(Context context, String tip, boolean isLongTime, int gravity, int id) {
        if (Looper.getMainLooper() != Looper.myLooper() || context == null) {
            return;
        }
        Toast toast = new Toast(context);
        if (isLongTime) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setGravity(gravity, 0, 100);// 设置Toast的位置
        RelativeLayout rl = new RelativeLayout(context);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(ActionMenuView.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextView textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setText(tip);
        textView.setBackgroundResource(id);// 自定义的图片
        textView.setGravity(Gravity.CENTER);
        textView.getBackground().setAlpha(150);
        rl.addView(textView);

        toast.setView(rl);
        toast.show();
    }

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    /**
     * 单例toast
     *
     * @param context
     * @param s
     */
    public static void showSingleToast(Context context, String s) {
        if (Looper.getMainLooper() != Looper.myLooper() || context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (!TextUtils.isEmpty(oldMsg) && s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    /**
     * tip为空时内容为自定义msg
     *
     * @param context
     * @param tip
     * @param msg
     */
    public static void show(Context context, String tip, String msg) {
        if (Looper.getMainLooper() != Looper.myLooper() || context == null) {
            return;
        }
        if (TextUtils.isEmpty(tip)) {
            tip = msg;
        } else if (tip.contains("Unable to resolve host")) {
            return;
        }
        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
    }

    /**
     * 默认长时间显示的Toast
     *
     * @param context
     * @param tip
     */
    public static void showLong(Context context, String tip) {
        if (Looper.getMainLooper() == Looper.myLooper() && context != null) {
            Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 默认短时间显示的Toast
     *
     * @param context
     * @param tip
     */
    public static void showShort(Context context, String tip) {
        if (Looper.getMainLooper() == Looper.myLooper() && context != null) {
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
        }
    }
}