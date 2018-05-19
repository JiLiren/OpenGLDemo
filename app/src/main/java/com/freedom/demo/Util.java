package com.freedom.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author vurtne on 19-May-18.
 */

public class Util {

    public static boolean isSupported(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x2000;
    }

    /**
     * @param vertexs int数组
     * @return 获取整形缓冲数据
     */
    public static IntBuffer getIntBuffer(int[] vertexs) {
        IntBuffer buffer;
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertexs.length * 4);
        qbb.order(ByteOrder.nativeOrder());
        buffer = qbb.asIntBuffer();
        buffer.put(vertexs);
        buffer.position(0);
        return buffer;
    }
    /**
     * @param vertexs float 数组
     * @return 获取浮点形缓冲数据
     */
    public static FloatBuffer getFloatBuffer(float[] vertexs) {
        FloatBuffer buffer;
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertexs.length * 4);
        qbb.order(ByteOrder.nativeOrder());
        buffer = qbb.asFloatBuffer();
        buffer.put(vertexs);
        buffer.position(0);
        return buffer;
    }

    /**
     * @param vertexs Byte 数组
     * @return 获取字节型缓冲数据
     */
    public static ByteBuffer getByteBuffer(byte[] vertexs) {
        ByteBuffer buffer = null;
        buffer = ByteBuffer.allocateDirect(vertexs.length);
        buffer.put(vertexs);
        buffer.position(0);
        return buffer;
    }

}
