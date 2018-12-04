package com.ritu.app3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

/**
 * 一个二维正方形，用作OpenGL ES 2.0中的绘制对象。
 */
public class Square {

    private final String vertexShaderCode =
            //这个矩阵成员变量提供了一个操纵钩子
            //使用此顶点着色器的对象的坐标
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            //矩阵必须作为gl_Position的修饰符包含在内。
            //请注意，uMVPMatrix因子*必须先按顺序排列*
            //使矩阵乘法乘积正确。
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    //此数组中每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            // top left
            -0.5f,  0.5f, 0.0f,
            // bottom left
            -0.5f, -0.5f, 0.0f,
            // bottom right
             0.5f, -0.5f, 0.0f,
            // top right
             0.5f,  0.5f, 0.0f };

    //以绘制顶点
    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    //每个顶点4个字节
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    /**
     * 设置要在OpenGL ES上下文中使用的绘图对象数据。
     */
    public Square() {
        // 为形状坐标初始化顶点字节缓冲区
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // （坐标值数*每个浮点数4个字节）
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // 初始化绘图列表的字节缓冲区
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // （坐标值的数量*每个短的2个字节）
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // 准备着色器和OpenGL程序
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        //创建空的OpenGL程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器添加到程序中
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片段着色器添加到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //创建OpenGL程序可执行文件
        GLES20.glLinkProgram(mProgram);
    }

    /**
     * 封装用于绘制此形状的OpenGL ES指令。
     *
     * @param mvpMatrix  - 要绘制的模型视图项目矩阵
     * 这个形状。
     */
    public void draw(float[] mvpMatrix) {
        // 将程序添加到OpenGL环境
        GLES20.glUseProgram(mProgram);

        // 获取顶点着色器位置成员的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 准备三角坐标数据
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // 获取片段着色器Color成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // 设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // 获取形状的变换矩阵
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // 应用投影和视图转换
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // 画正方形
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        //禁用顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}