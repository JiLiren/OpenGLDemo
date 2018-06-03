package com.ritu.game.demo.triangle;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ritu.game.tools.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author vurtne on 3-Jun-18
 * */
public class Triangle {

    /** 4 x 4 的投影矩阵 */
    public static float[] mProjMatrix = new float[16];
    /** 摄像机位置朝向的参数举证 */
    public static float[] mVMatrix = new float[16];
    /** 总变换矩阵 */
    public static float[] mMVPMatrix;
    /** 自定义渲染管线着色器程序ID */
    int mProgram;
    /** 总变换矩阵引用 */
    int muMVPMAtrixHandle;
    /** 顶点位置属性引用 */
    int maPositionHandle;
    /** 顶点颜色着色器引用 */
    int maColorHandle;
    /** 顶点着色器代码脚本 */
    String mVertexShader;
    /** 片元着色器代码脚本 */
    String mFragmentShader;

    /** 具有物体的3D变换矩阵，包括旋转、平移、缩放 */
    static float[] mMMatrix = new float[16];
    /** 顶点坐标数据缓冲 */
    FloatBuffer mVertexBuffer;
    /** 顶点颜色坐标数据缓冲 */
    FloatBuffer mColorBuffer;
    /** 顶点数量 */
    int vCount = 0;
    /** 角度 */
    float xAngle = 0;

    public Triangle(TDView view) {
        initVertexData();
        initShader(view);
    }

    private void initVertexData(){
        vCount = 3;
        //单位长度
        final float UNIT_SIZT = 0.2f;
        float vertices[] = new float[]{
                -4*UNIT_SIZT,0,0,0,-4*UNIT_SIZT,0,4*UNIT_SIZT,0,0
        };
        ByteBuffer vBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        //设置字节顺序为本地操作系统顺序
        vBuffer.order(ByteOrder.nativeOrder());
        //转换为浮点类型缓冲
        mVertexBuffer = vBuffer.asFloatBuffer();
        //在缓冲区内写入数据
        mVertexBuffer.put(vertices);
        //设置缓冲区其实位置
        mVertexBuffer.position(0);

        float colors[] = new float[]{
                1,1,1,0,0,0,1,0,0,1,0,0
        };
        ByteBuffer cBuffer = ByteBuffer.allocateDirect(colors.length * 4);
        cBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = cBuffer.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    /**
     * 生成最终变换矩阵的方法
     * */
    public static float[] getFinalMatrix(float[] spec){
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix,0,mMMatrix,0,spec,0);
        Matrix.multiplyMM(mMVPMatrix,0,mProjMatrix,0,mMVPMatrix,0);
        return mMVPMatrix;
    }

    /**
     * 初始化着色器
     * */
    private void initShader(TDView view){
        mVertexShader = ShaderUtils.loadFromAssetsFile("triangle/vertex.sh",view.getResources());
        mFragmentShader = ShaderUtils.loadFromAssetsFile("triangle/frag.sh",view.getResources());
        mProgram = ShaderUtils.createProgram(mVertexShader,mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram,"aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");
        muMVPMAtrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
    }

    public void drawSelf(){
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix,0,0,0,1,0);
        Matrix.translateM(mMMatrix,0,0,0,1);
        Matrix.rotateM(mMMatrix,0,xAngle,1,0,0);
        GLES20.glUniformMatrix4fv(muMVPMAtrixHandle,1,false,Triangle.getFinalMatrix(mMMatrix),0);
        //将顶点位置数据传送进渲染管线
        GLES20.glVertexAttribPointer(maPositionHandle,3,GLES20.GL_FLOAT,false,3*4,mVertexBuffer);
        //将顶点颜色数据传送进渲染管线
        GLES20.glVertexAttribPointer(maPositionHandle,4,GLES20.GL_FLOAT,false,4*4,mColorBuffer);
        //启用顶点位置数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        //启用顶点着色器数据
        GLES20.glEnableVertexAttribArray(maColorHandle);
        //执行绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vCount);
    }
}
