package com.ritu.golbal;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ritu.util.BufferUtil;
import com.ritu.util.LoggerConfig;
import com.ritu.util.ShaderHelper;

import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class BaseRenderer implements GLSurfaceView.Renderer {

    protected int program;
    public RendererCallback rendererCallback;
    public boolean isReadCurrentFrame;
    protected int outputWidth;
    protected int outputHeight;


    /**
     * 创建OpenGL程序对象
     *
     * @param vertexShader   顶点着色器代码
     * @param fragmentShader 片段着色器代码
     */
    protected void makeProgram(String vertexShader,String fragmentShader) {
        // 步骤1：编译顶点着色器
        int vertexShaderId = ShaderHelper.compileVertexShader(vertexShader);
        // 步骤2：编译片段着色器
        int fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShader);
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        program = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(program);
    }

    protected int getUniform(String name) {
        return GLES20.glGetUniformLocation(program, name);
    }

    protected int getAttrib(String name) {
        return GLES20.glGetAttribLocation(program, name);
    }

//    @Override
//    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        outputWidth = width;
//        outputHeight = height;
//    }

    /**
     * 获取当前画面帧,并回调接口
     */
    protected void onReadPixel(int x,int y,int  width,int height) {
        if (!isReadCurrentFrame) {
            return;
        }
        isReadCurrentFrame = false;
        ByteBuffer buffer = ByteBuffer.allocate(width * height * BufferUtil.BYTES_PER_FLOAT);
        GLES20.glReadPixels(x,
                y,
                width,
                height,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, buffer);
        if (rendererCallback != null) {
            rendererCallback.onRendererDone(buffer, width, height);
        }
    }

    protected Bitmap readPixel(int w,int  h){
        ByteBuffer buffer = ByteBuffer.allocate(w * h * 4);
        GLES20.glReadPixels(0,
                0,
                w,
                h,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, buffer);

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        return bitmap;
    }

}
