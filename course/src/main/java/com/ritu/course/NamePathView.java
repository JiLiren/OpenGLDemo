package com.ritu.course;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class NamePathView extends View {
    public NamePathView(Context context) {
        super(context);
    }

    public NamePathView(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NamePathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Path mPath;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPath == null){
            mPath = new Path();
        }
        mPath.reset();
        mPath.moveTo(100, 100);
        mPath.lineTo(200, 200);
        mPath.lineTo(200, 300);
        mPath.lineTo(500, 300);

        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath,paint);
        paint.setTextSize(50);

        String text = "新 华 北 环 五 路";
        Rect tr = new Rect();
        paint.getTextBounds(text, 0, text.length(), tr);
        int textWidth = tr.width();
        int textHeight = tr.height();

        canvas.drawTextOnPath(text,mPath,0f,textHeight / 2,paint);
    }
}
