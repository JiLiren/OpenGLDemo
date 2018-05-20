package com.freedom.demo.line;

import android.opengl.GLSurfaceView;

import com.freedom.demo.Util;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author vurtne on 19-May-18.
 */
public class LineRenderer implements GLSurfaceView.Renderer {

    /**
     * 顶点数组
     */

    private float[] mArray = {
            -0.6f , 0.6f , 0f,

            -0.2f , 0f , 0f ,

            0.2f , 0.6f , 0f ,

            0.6f , 0f , 0f

    };

    /**
     * 缓冲区
     */
    private FloatBuffer mBuffer = Util.getFloatBuffer(mArray);

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1f, 0f, 0f, 0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height );

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除屏幕
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // 允许设置顶点 // GL10.GL_VERTEX_ARRAY顶点数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // 设置顶点
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mBuffer);
        //设置点的颜色为绿色
        gl.glColor4f(0f, 1f, 0f, 0f);
        //设置点的大小
        gl.glPointSize(80f);
        // 绘制点
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
        // 取消顶点设置
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
