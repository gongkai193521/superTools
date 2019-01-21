package com.gkail.tools.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.gkail.tools.MainApplication;

/**
 * Created by gongkai on 2019/1/7.
 */

public class MGridView extends View {

    public MGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    Path path;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path = new Path();
        //准备画笔
        Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStrokeWidth(2);
        mRedPaint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        mRedPaint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        //绘制
        WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Path path = gridPath(50, size);
        //绘制弧：arcTo(矩形范围，起点，终点，)
        RectF rectF = new RectF(100, 500, 300, 200);
        path.moveTo(0, 0);
        path.arcTo(rectF, 0, 45, true);
        canvas.drawPath(path, mRedPaint);
    }

    public Path gridPath(int step, Point winSiz) {
        //每间隔step,将笔点移到(0, step * i)，然后画线到(winSize.x, step * i)
        for (int i = 0; i < winSiz.x / step + 1; i++) {
            path.moveTo(step * i, 0);
            path.lineTo(step * i, winSiz.y);
        }
        for (int i = 0; i < winSiz.y / step + 1; i++) {
            path.moveTo(0, step * i);
            path.lineTo(winSiz.x, step * i);
        }
        return path;
    }
}
