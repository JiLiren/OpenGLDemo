package com.ritu.item2;

import android.content.Context;
import android.opengl.GLES20;

import com.ritu.golbal.BaseRenderer;
import com.ritu.util.BufferUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BasisRender extends BaseRenderer {

    /**
     * 顶点着色器：之后定义的每个都会传1次给顶点着色器
     */
    private String VERTEX_SHADER = "" +
            "attribute vec4 a_Position;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = a_Position;\n" +
            "    gl_PointSize = 30.0;\n" +
            "}";
    /**
     * 片段着色器
     */
    private String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "uniform vec4 u_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";
    /**
     * 顶点数据数组
     */
    private float[] POINT_DATA = new float[]{
//            // 两个点的x,y坐标（x，y各占1个分量）
            0f, 0f, 0f, 0.5f, -0.5f, 0f, 0f, 0f - 0.5f, 0.5f, 0f - 0.5f, 0.5f, 0.5f - 0.5f,-0.5f,0.5f};

//    private float[] POINT_DATA = new float[]{
//            // 两个点的x,y坐标（x，y各占1个分量）
//             0f, - 0.5f, 0.5f, - 0.5f, 0.5f, 0f,-0.5f,0.5f};
////    private float[] POINT_DATA = new float[]{
////             两个点的x,y坐标（x，y各占1个分量）
////            0f, 0f, 0f, 0.5f, -0.5f, 0f, 0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f - 0.5f};

    private int POSITION_COMPONENT_COUNT = 2;
    private int DRAW_COUNT = POINT_DATA.length / POSITION_COMPONENT_COUNT;

    private FloatBuffer vertexData;
    private int uColorLocation;
    private int aPositionLocation;
    private int drawIndex;
    private Context context;

    public BasisRender(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        vertexData = BufferUtil.createFloatBuffer(POINT_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        uColorLocation = getUniform("u_Color");
        aPositionLocation = getAttrib("a_Position");

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        outputWidth = width;
        outputHeight = height;
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawIndex++;
        // 几何图形相关定义：http://wiki.jikexueyuan.com/project/opengl-es-guide/basic-geometry-definition.html
        drawTriangle();
        drawLine();
        drawPoint();
        if (drawIndex >= DRAW_COUNT) {
            drawIndex = 0;
        }
    }


    private void drawPoint() {
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, drawIndex);
    }

    private void drawLine() {
        // GL_LINES：每2个点构成一条线段
        // GL_LINE_LOOP：按顺序将所有的点连接起来，包括首位相连
        // GL_LINE_STRIP：按顺序将所有的点连接起来，不包括首位相连
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);

        //GL_LINE_LOOP
        //GL_LINE_STRIP
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, drawIndex);
    }

    /**
     * 1.GL_TRIANGLES：每三个顶之间绘制三角形，之间不连接
     *
     * 2.GL_TRIANGLE_FAN：以V0V1V2,V0V2V3,V0V3V4，……的形式绘制三角形
     *
     * 3.GL_TRIANGLE_STRIP：顺序在每三个顶点之间均绘制三角形。这个方法可以保证从相同的方向上所有三角形均被绘制。以V0V1V2,V1V2V3,V2V3V4……的形式绘制三角形
     * */
    private void drawTriangle() {
        // GL_TRIANGLES：每3个点构成一个三角形
        // GL_TRIANGLE_STRIP：相邻3个点构成一个三角形,不包括首位两个点
        // GL_TRIANGLE_FAN：第一个点和之后所有相邻的2个点构成一个三角形
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, drawIndex);
    }
}
