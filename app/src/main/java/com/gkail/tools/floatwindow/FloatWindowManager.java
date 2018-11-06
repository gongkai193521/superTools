package com.gkail.tools.floatwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.gkail.tools.MainApplication;
import com.gkail.tools.R;

/**
 * Created by gongkai on 18/10/24.
 */

public class FloatWindowManager {
    static WindowManager mManager = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
    static FloatView floatView;

    /**
     * 利用系统弹窗实现悬浮窗
     */
    public static void showFloatView() {
        if (floatView == null) {
            floatView = new FloatView(MainApplication.getContext());
            floatView.setBackgroundResource(R.drawable.icon);
//            Button bt = (Button) floatView.findViewById(R.id.capture);
//            bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtils.showSingleToast(MainApplication.getContext(), "点击了");
//                    MainApplication.getContext().startService(new Intent(MainApplication.getContext(), ScreenService.class));
//                }
//            });
            WindowManager.LayoutParams params = MainApplication.getLayoutParams();
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SCALED
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

            //需要权限
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.format = 1;
            params.x = 0;
            params.y = -25;
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.width = 200;
            params.height = 200;
            mManager.addView(floatView, params);
        }
    }

    private void removeFloatView() {
        if (mManager != null && floatView != null) {
            mManager.removeViewImmediate(floatView);
        }
    }
}
