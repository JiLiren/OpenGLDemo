package com.ritu.line;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ORender extends BaseRenderer {
    /**
     * 存储模型矩阵。 该矩阵用于从对象空间移动模型（可以考虑每个模型）
     * 位于宇宙中心）到世界空间。
     */
    private float[] mModelMatrix = new float[16];

    /**
     * 存储视图矩阵。 这可以被认为是我们的相机。 该矩阵将世界空间转换为眼睛空间;
     * 它定位相对于我们眼睛的东西。
     */
    private float[] mViewMatrix = new float[16];

    /**
     * 存储投影矩阵。 这用于将场景投影到2D视口上。
     */
    private float[] mProjectionMatrix = new float[16];

    /**
     * 为最终组合矩阵分配存储空间。 这将被传递到着色器程序。
     */
    private float[] mMVPMatrix = new float[16];

    /**
     * 将我们的模型数据存储在浮点缓冲区中。
     */
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle2Vertices;
    private final FloatBuffer mTriangle3Vertices;

    /**
     * 这将用于传递变换矩阵。
     */
    private int mMVPMatrixHandle;

    /**
     * 这将用于传递模型位置信息。
     */
    private int mPositionHandle;

    /**
     * 这将用于传递模型颜色信息。
     */
    private int mColorHandle;

    /**
     * 每个浮点数多少字节。
     */
    private final int mBytesPerFloat = 4;

    /**
     * 每个顶点有多少个元素。
     */
    private final int mStrideBytes = 7 * mBytesPerFloat;

    /**
     * 偏移位置数据。
     */
    private final int mPositionOffset = 0;

    /**
     * 元素中位置数据的大小。
     */
    private final int mPositionDataSize = 3;

    /**
     * 偏移颜色数据。
     */
    private final int mColorOffset = 3;

    /**
     * 元素中颜色数据的大小。
     */
    private final int mColorDataSize = 4;

    /**
     * 初始化模型数据。
     */
    public ORender() {
        //定义等边三角形的点。

        //这个三角形是红色，绿色和蓝色。
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f};

        //这个三角形是黄色，青色和洋红色。
        final float[] triangle2VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 1.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 1.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                1.0f, 0.0f, 1.0f, 1.0f};

        //这个三角形是白色，灰色和黑色。
        final float[] triangle3VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f};

        //初始化缓冲区。
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mTriangle1Vertices.put(triangle1VerticesData).position(0);
        mTriangle2Vertices.put(triangle2VerticesData).position(0);
        mTriangle3Vertices.put(triangle3VerticesData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        //将背景清晰颜色设置为灰色。
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // 将眼睛放在原点后面。
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // 我们正望着远方
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // 设置我们的向量。 这是我们拿着相机的头部指向的地方。
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        //设置视图矩阵。 可以说该矩阵代表摄像机位置。
        //注意：在OpenGL 1中，使用ModelView矩阵，它是模型和模型的组合
        //查看矩阵 在OpenGL 2中，如果我们选择，我们可以单独跟踪这些矩阵。
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader =
                // 表示组合模型/视图/投影矩阵的常量。
                "uniform mat4 u_MVPMatrix;      \n"
                        //我们将传入的每顶点位置信息。
                        + "attribute vec4 a_Position;     \n"
                        //我们将传入的每顶点颜色信息。
                        + "attribute vec4 a_Color;        \n"
                        //这将传递给片段着色器。
                        + "varying vec4 v_Color;          \n"
                        //我们的顶点着色器的入口点。
                        + "void main()                    \n"
                        + "{                              \n"
                        //将颜色传递给片段着色器。
                        + "   v_Color = a_Color;          \n"
                        // 它将在三角形内插。 gl_Position是一个用于存储最终位置的特殊变量。
                        + "   gl_Position = u_MVPMatrix   \n"
                        //将矩阵乘以矩阵以得到最终点
                        + "               * a_Position;   \n"
                        //标准化的屏幕坐标。
                        + "}                              \n";

        final String fragmentShader =
                // 将默认精度设置为中等。 我们不需要那么高的
                "precision mediump float;       \n"
                        // 片段着色器中的精度。这是从顶点着色器插入的颜色
                        + "varying vec4 v_Color;          \n"
                        // 每片三角形。片段着色器的入口点。
                        + "void main()                    \n"
                        + "{                              \n"
                        //直接将颜色传递给管道。
                        + "   gl_FragColor = v_Color;     \n"
                        + "}                              \n";

        // 在顶点着色器中加载。
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0) {
            //传递着色器源。
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // 编译着色器。
            GLES20.glCompileShader(vertexShaderHandle);

            //获取编译状态。
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            //如果编译失败，请删除着色器。
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0) {
            throw new RuntimeException("创建顶点着色器时出错.");
        }

        // 在片段着色器着色器中加载。
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            // 传递着色器源。
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // 编译着色器。
            GLES20.glCompileShader(fragmentShaderHandle);

            // 获取编译状态。
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // 如果编译失败，请删除着色器。
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("创建片元色器时出错。");
        }

        // 创建一个程序对象并将句柄存储到它。
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // 将顶点着色器绑定到程序。
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // 绑定片元着色器到程序
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            //绑定属性
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

            // 将两个着色器链接到一个程序中。
            GLES20.glLinkProgram(programHandle);

            // 获取链接状态。
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            //如果链接失败，请删除该程序。
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        // 设置程序句柄。 这些将在以后用于将值传递给程序。
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // 告诉OpenGL在渲染时使用此程序。
        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        //将OpenGL视口设置为与曲面相同的大小。
        GLES20.glViewport(0, 0, width, height);

        //创建一个新的透视投影矩阵。 高度将保持不变
        //宽度将根据纵横比而变化。
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // 每10秒完成一次旋转。
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // 画三角形直面。
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle1Vertices);

        // 画一个向下平移并旋转到地面平坦。
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle2Vertices);

        //画一个向右平移并旋转到面向左侧。
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle3Vertices);
    }

    /**
     *从给定的顶点数据中绘制一个三角形。
     *
     * @param aTriangleBuffer 包含顶点数据的缓冲区。
     */
    private void drawTriangle(final FloatBuffer aTriangleBuffer) {
        // 传递位置信息
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 传递颜色信息
        aTriangleBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        //这将视图矩阵乘以模型矩阵，并将结果存储在MVP矩阵中
        //（目前包含模型*视图）。
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        //这将模型视图矩阵乘以投影矩阵，并将结果存储在MVP矩阵中
        //（现在包含模型*视图*投影）。
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
