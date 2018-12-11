/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ritu.app3;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * 提供GLSurfaceView对象的绘图说明。 这个类
 *   *必须覆盖OpenGL ES绘图生命周期方法：
 * <ul>
 *   <li>{@link GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Triangle mTriangle;
    private Square   mSquare;
    private T t;

    //mMVPMatrix是“模型视图投影矩阵”的缩写
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];


    private final float[] scaleMatrix = new float[16];

    private float mAngle;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // 设置背景框架颜色
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle = new Triangle();
        mSquare   = new Square();
        t = new T();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // 画背景颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // 设置摄像机位置（视图矩阵）
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //计算投影和视图转换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //画正方形
//        mSquare.draw(mMVPMatrix);

        //为三角形创建一个旋转

        //使用以下代码生成常量旋转。
        //使用TouchEvents时保留此代码。
        // long time = SystemClock.uptimeMillis（）％4000L;
        // float angle = 0.090f *（（int）time）;
        Log.e("11111",mAngle +"");
        GLES20.;
        SwapBuffer
        float[] a = new float[16];
        Matrix.scaleM(scaleMatrix, 0, 1f, 1f, 1f);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);




//        (float[] m, int mOffset, float x, float y, float




        //将旋转矩阵与投影和摄像机视图组合在一起
        //请注意，mMVPMatrix因子*必须先按顺序排列*
        //使矩阵乘法乘积正确。



//        Matrix.multiplyMM(a, 0, mMVPMatrix, 0, mRotationMatrix, 0);
//        Matrix.multiplyMM(scratch, 0, a, 0, scaleMatrix, 0);
        // 画三角形
        mTriangle.draw(scratch);
//        t.onDrawFrame();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        //根据几何变化调整视口，
        //如屏幕旋转
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        //此投影矩阵应用于对象坐标
        //在onDrawFrame（）方法中
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * 用于编译OpenGL着色器的实用方法。
     *
     * <p> <strong>注意：</ strong>开发着色器时，请使用checkGlError（）调试着色器编码错误的方法。</ p>
     * @param type  - 顶点或片段着色器类型。
     * @param shaderCode  - 包含着色器代码的String。
     * @return  - 返回着色器的id。
     */
    public static int loadShader(int type, String shaderCode){

        //创建顶点着色器类型（GLES20.GL_VERTEX_SHADER）
        //或片段着色器类型（GLES20.GL_FRAGMENT_SHADER）
        int shader = GLES20.glCreateShader(type);

        // 将源代码添加到着色器并进行编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * 用于调试OpenGL调用的实用方法。 提供呼叫的名称
    * 制作完成后：
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation（mProgram，“vColor”）;
    * MyGLRenderer.checkGlError（“glGetUniformLocation”）; </ pre>
    *
    *如果操作不成功，则检查会引发错误。
    * @param glOperation  - 要检查的OpenGL调用的名称。
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * 返回三角形的旋转角度（mTriangle）。
     *
     * @return - 表示旋转角度的浮点数。
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * 设置三角形的旋转角度（mTriangle）。
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

}