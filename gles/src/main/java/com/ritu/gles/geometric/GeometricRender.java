package com.ritu.gles.geometric;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author vurtne on 1-Jun-18
 * */
public class GeometricRender implements GLSurfaceView.Renderer {

    public enum GeometricEnum{
        Triangle
    }

    public GeometricEnum mEnum;

    private Triangle mTriangle;

    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mMVPMatrix=new float[16];


    public void setEnum(GeometricEnum eEnum){
        this.mEnum = eEnum;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f,0f,0f,1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1,3, 5);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mEnum == GeometricEnum.Triangle){
            if (mTriangle == null){
                mTriangle = new Triangle();
            }
            mTriangle.onDrawFrame(gl);
        }
    }
}
