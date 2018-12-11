package com.ritu.line;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;
import java.util.Random;

public class Spirit {

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

    private PolygonRender mRender;
    private  int co;
    private int[] ca = new int[]{Color.BLACK,Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GRAY,Color.GREEN,Color.LTGRAY,Color.MAGENTA,Color.RED,Color.YELLOW};
    private String[] a = new String[]{"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    private boolean ready;

    public Spirit(PolygonRender render ,float radius) {
        this.mRender = render;
        this.RADIUS = radius;
        initColor();
    }

    private void initColor(){
        Random random = new Random();
        String c = "#"+ a[random.nextInt(16)] +a[random.nextInt(16)] +a[random.nextInt(16)]
                +a[random.nextInt(16)] +a[random.nextInt(16)] +a[random.nextInt(16)];
        co = Color.parseColor(c);
    }

    public void onCreate(){
        if (ready){
            return;
        }
        ready = true;
        mRender.makeProgram(vertexShader, FRAGMENT_SHADER);

        uColorLocation = mRender.getUniform("u_Color");
        aPositionLocation = mRender.getAttrib("a_Position");

        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    public void onDraw(){
        updateVertexData();
        drawLine();
        drawShape();
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

    private void drawLine() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 1, mPolygonVertexCount);
    }

    private void drawShape() {
        GLES20.glUniform4f(uColorLocation, ColorFormat.getRedFromARGB(co),  ColorFormat.getGreenFromARGB(co),  ColorFormat.getBlueFromARGB(co), 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mPolygonVertexCount + 2);
    }
    /**
     * 更新多边形的边数
     */
    private void updatePolygonVertexCount() {
        mPolygonVertexCount++;
        mPolygonVertexCount =(mPolygonVertexCount > 64) ? 3 : mPolygonVertexCount;
        if (mPolygonVertexCount == 3){
            initColor();
        }
    }
}
