package demo.ritu.com.app4.shape.triangle;

import android.opengl.GLES20;

import demo.ritu.com.app4.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * 普通三角形
 */
public class Triangle extends BaseGLSL {

    // 简单的顶点着色器
    public static final String vertexShaderCode =
            "attribute vec4 vPosition;\n" +
                    " void main() {\n" +
                    "     gl_Position   = vPosition;\n" +
                    " }";

    // 简单的片元着色器
    public static final String fragmentShaderCode =
            " precision mediump float;\n" +
                    " uniform vec4 vColor;\n" +
                    " void main() {\n" +
                    "     gl_FragColor = vColor;\n" +
                    " }";

    // 定义三角形的坐标
    public static float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f ,
            -0.3f,0.8f,0.0f,
            -0.5f,0.0f,0.0f,
            -0.7f,-0.3f,0.0f
    };

    // 定义三角形的颜色——白色
    public static float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    // 顶点个数
    public static final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

    FloatBuffer vertexBuffer;

    int mProgram;
    public Triangle() {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 申请底色空间

        ByteBuffer bb = ByteBuffer.allocateDirect(Triangle.triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(Triangle.triangleCoords);
        vertexBuffer.position(0);


        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
    }

    public void draw() {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //获取顶点着色器的vPosition成员句柄
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, Triangle.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, Triangle.vertexStride, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, Triangle.color, 0);




        //绘制三角形
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 0, Triangle.vertexCount,drawListBuffer);
         GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, Triangle.vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private void checkVertex(float[] verctex){
        int size = verctex.length;
        int x = size % 3;
        if ( x == 1){
            float[] v = new float[]{verctex[verctex.length - 2],verctex[verctex.length - 1]};
            System.arraycopy(v,0,verctex,verctex.length,2);
        }
    }
}
