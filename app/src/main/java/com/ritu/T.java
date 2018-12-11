package com.ritu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class T extends View {
    public T(Context context) {
        super(context);
    }

    public T(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public T(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(100,100,10,paint);
    }
}
