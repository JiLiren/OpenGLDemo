package com.ritu.item1;

import android.graphics.Point;
import android.opengl.GLES20;

import com.ritu.golbal.BaseRenderer;
import com.ritu.util.BufferUtil;

import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PointRender extends BaseRenderer {

    private String VERTEX_SHADER = "" +
            "attribute vec4 a_Position;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = a_Position;\n" +
            "    gl_PointSize = 40.0;\n" +
            "}";
    private String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "uniform vec4 u_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";
    private float[] POINT_DATA = new float[]{1f, 1.0f};
    private int POSITION_COMPONENT_COUNT = 2;

    private FloatBuffer vertexData;
    private int uColorLocation;


    public PointRender() {


    }

    private void init(){
        Point point = new Point(100,100);

//        float aa = 3;
//        float bb = 5;
//        float av = aa / bb;
////        NumberFormat numberFormat = NumberFormat.getInstance();
////        numberFormat.setMaximumFractionDigits(10);
////
////        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        float f1 = (((float)point.x) / ((float)outputWidth) - 1.0f) * outputRatio;

        //(param / mRender.mWidth * 2 - 1.0f) * mRender.mRatio;


        float x = ((float) point.x) * 2 / outputWidth - 1.0f;
        float y = 1.0f - ((float) point.y) * 2 / outputHeight ;
        POINT_DATA = new float[]{x,y};
//        POINT_DATA = new float[]{-1,0};
        vertexData = BufferUtil.createFloatBuffer(POINT_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width,int height) {
        outputWidth = width;
        outputHeight = height;
        outputRatio = (float) width / height;
        GLES20.glViewport(0, 0, width, height);
        init();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        init();
        int aPositionLocation = getAttrib("a_Position");
        uColorLocation = getUniform("u_Color");


        GLES20.glEnableVertexAttribArray(aPositionLocation);
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, vertexData);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUniform4f(uColorLocation, 1.0f, 0f, 0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }
}
