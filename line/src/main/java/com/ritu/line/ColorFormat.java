package com.ritu.line;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * @author 冀泽阳 on 3-Dec-18
 * */
@SuppressWarnings({"unused"})
class ColorFormat {

    /**
     * 将颜色16进制转换成float数组
     * @param color 颜色
     * */
    static float[] generateFloadArrayFromARGB(@ColorInt int color){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Color.alpha(color);
        return new float[]{
                (red / 255f),
                (green / 255f),
                (blue / 255f),
                (alpha / 255f)
        };
    }

    static float getRedFromARGB(@ColorInt int color){
        int red = Color.red(color);
        return red / 255f;
    }

    static float getGreenFromARGB(@ColorInt int color){
        int green = Color.green(color);
        return green / 255f;
    }

    static float getBlueFromARGB(@ColorInt int color){
        int blue = Color.blue(color);
        return blue / 255f;
    }

    static float getAlphaFromARGB(@ColorInt int color){
        int alpha = Color.alpha(color);
        return alpha / 255f;
    }
}
