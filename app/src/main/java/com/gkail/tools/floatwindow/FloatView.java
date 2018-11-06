package com.gkail.tools.floatwindow;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.gkail.tools.MainApplication;
import com.gkail.tools.R;

/**
 * Created by gongkai on 2018/10/29.
 */

public class FloatView extends View {
    private float mTouchStartX;
    private float mTouchStartY;
    private float x, y;
    private WindowManager mManager = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams mParams = MainApplication.getLayoutParams();

    public FloatView(Context context) {
        super(context);
        inflate(MainApplication.getContext(), R.layout.floatview, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getRawX();
        y = event.getRawY() - 25;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX = mTouchStartY = 0;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updateViewPosition() {
        mParams.x = (int) (x - mTouchStartX);
        mParams.y = (int) (y - mTouchStartY);
        mManager.updateViewLayout(this, mParams);
    }
}
