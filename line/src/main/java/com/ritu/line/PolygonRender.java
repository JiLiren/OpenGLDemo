package com.ritu.line;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;


import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PolygonRender extends BaseRenderer {

    private Context context;

    private List<Spirit> spirits = new ArrayList<>();

    public float[] mViewMatrix=new float[16];
    public float[] mProjectMatrix=new float[16];
    public float[] mMVPMatrix=new float[16];

    public PolygonRender(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        Spirit spirit;
        float[] array = new float[]{-1.0f,-0.95f,-0.9f,-0.85f,-0.8f,-0.75f,-0.7f,-0.65f,-0.6f,-0.55f,-0.5f,-0.45f,-0.4f,-0.35f,-0.3f,-0.25f,-0.2f,-0.15f,-0.1f,-0.05f,0.0f,0.05f,
                0.1f,0.15f,0.2f,0.25f,0.3f,0.35f,0.4f,0.45f,0.5f,0.55f,0.6f,0.65f,0.7f,0.75f,0.8f,0.85f,0.9f,0.95f,1.0f};
        Random random = new Random();
        for (int i=0;i <21 ;i++){
            spirit = new Spirit(this,array[i]);
            spirits.add(spirit);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        outputWidth = width;
        outputHeight = height;
        GLES20.glViewport(0, 0, width, height);
        float ratio=(float)width/height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 2, 2, 0.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

//        Matrix.scaleM(mProjectMatrix,2,0.0f,0.0f,0.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        for (Spirit spirit: spirits){
            spirit.onCreate();
            spirit.onDraw(this);
        }
    }

}
