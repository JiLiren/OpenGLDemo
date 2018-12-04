package com.ritu.item3;

import android.content.Context;
import android.opengl.GLES20;

import com.ritu.golbal.BaseRenderer;
import com.ritu.util.BufferUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PolygonRender extends BaseRenderer {

    /**
     * 顶点着色器：之后定义的每个都会传1次给顶点着色器
     */
    private String VERTEX_SHADER = "" +
            "attribute vec4 a_Position;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = a_Position;\n" +
            "    gl_PointSize = 10.0;\n" +
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
    private int POSITION_COMPONENT_COUNT = 2;
    /**
     * 多边形顶点与中心点的距离
     */
    private float RADIUS = 0.5f;
    /**
     * 起始点的弧度
     */
    private float START_POINT_RADIAN = (float) (2 * Math.PI / 4);

    private FloatBuffer mVertexData;
    private int uColorLocation;
    private int aPositionLocation;
    /**
     * 多边形的顶点数，即边数
     */
    private int mPolygonVertexCount = 3;
        /**
         * 绘制所需要的顶点数
         */
        private float[] mPointData;

    String vertexShader = VERTEX_SHADER;

    private Context context;

    public PolygonRender(Context context) {
        this.context = context;
        init();
    }

    private void init(){

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        makeProgram(vertexShader, FRAGMENT_SHADER);

        uColorLocation = getUniform("u_Color");
        aPositionLocation = getAttrib("a_Position");

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
        updateVertexData();
        drawShape();
        drawLine();
        drawPoint();
        updatePolygonVertexCount();
    }

    private void updateVertexData() {
        // 边数+中心点+闭合点；一个点包含x、y两个向量
        mPointData = new float[(mPolygonVertexCount + 2) * 2];

        // 组成多边形的每个三角形的中心点角的弧度
        float radian = (float) (2 * Math.PI / mPolygonVertexCount);
        // 中心点
        mPointData[0] = 0f;
        mPointData[1] = 0f;
        // 多边形的顶点数据
        for (int i =0;i <mPolygonVertexCount;i++){
            mPointData[2 * i + 2] = (float) (RADIUS * Math.cos((radian * i + START_POINT_RADIAN)));
            mPointData[2 * i + 1 + 2] = (float) (RADIUS * Math.sin((radian * i + START_POINT_RADIAN)));
        }
        // 闭合点：与多边形的第一个顶点重叠
        mPointData[mPolygonVertexCount * 2 + 2] = (float) (RADIUS * Math.cos(START_POINT_RADIAN));
        mPointData[mPolygonVertexCount * 2 + 3] = (float) (RADIUS * Math.sin(START_POINT_RADIAN));

        mVertexData = BufferUtil.createFloatBuffer(mPointData);
        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData);
    }


    private void drawShape() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mPolygonVertexCount + 2);
    }

    private void drawPoint() {
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 1, mPolygonVertexCount + 1);
    }

    private void drawLine() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 1, mPolygonVertexCount);
    }

    /**
     * 更新多边形的边数
     */
    private void updatePolygonVertexCount() {
        mPolygonVertexCount++;
        mPolygonVertexCount =(mPolygonVertexCount > 32) ? 3 : mPolygonVertexCount;
    }
}
