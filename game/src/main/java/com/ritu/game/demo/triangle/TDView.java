package com.ritu.game.demo.triangle;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author vurtne on 3-Jun-18
 * */
public class TDView extends GLSurfaceView{

    /** 旋转的角度 */
    final float ANGLE_SPAN = 0.375f;
    SceneRenderer mRender;
    RotateThread mThread;

    public TDView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        this.mRender = new SceneRenderer();
        this.setRenderer(mRender);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer{

        Triangle tle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0,0,0,1f);
            tle = new Triangle(TDView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            mThread = new RotateThread();
            mThread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0,0,width,height);
            float ratio = (float)width / height;
            Matrix.frustumM(Triangle.mProjMatrix,0,-ratio,ratio, -1,1,1,10);
            Matrix.setLookAtM(Triangle.mVMatrix,0,0,0,3,0f,0f,0f,0f,1f,0f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClearColor(0,0,0,1f);
            tle.drawSelf();
        }
    }

    private class RotateThread extends Thread{

        public boolean flog = true;

        @Override
        public void run(){
            while (flog){
                mRender.tle.xAngle = mRender.tle.xAngle + ANGLE_SPAN;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
