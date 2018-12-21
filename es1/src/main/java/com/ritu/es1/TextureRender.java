package com.ritu.es1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TextureRender implements Renderer {

    private static Context context;
    private int bitmap_width;
    private int bitmap_height;
    private int textures[] = new int[2];
    private Bitmap bitmap;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.tom_and_jerry);
        bitmap_width = bitmap.getWidth();
        bitmap_height = bitmap.getHeight();

        //设置清屏颜色
        gl.glClearColor(0.1f, 0.8f, 1.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口
        gl.glViewport(0, 0, width, height);
        generateTexture();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //清屏
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }



    private void generateTexture(GL10 gl) {
        gl.glGenTextures(2, textures, 0);
        gl.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        gl.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        gl.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        gl.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }
}
