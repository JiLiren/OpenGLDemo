package com.freedom.demo.bitmapdemo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DemoRandene implements GLSurfaceView.Renderer {

    private Context context;
    private Filter filter;
    private Bitmap bitmap1,bitmap2;
    private FloatBuffer bPos1;
    private FloatBuffer bPos2;
    private FloatBuffer bPos3;
    private FloatBuffer bPos4;
    private FloatBuffer bPos5;
    private FloatBuffer bPos6;
    private FloatBuffer bPos7;
    private FloatBuffer bPos8;
    private FloatBuffer bPos9;
    private FloatBuffer bPos10;
    private FloatBuffer bPos11;
    private FloatBuffer bPos12;
    private FloatBuffer bPos13;
    private FloatBuffer bPos14;
    private FloatBuffer bPos15;
    private FloatBuffer bPos16;
    private FloatBuffer bCoord;
    private String vertex;
    private String fragment;
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int glHMatrix;
    private int hIsHalf;
    private int glHUxy;
    private int textureId;
    private float uXY;
    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mMVPMatrix=new float[16];



    private int hChangeType;
    private int hChangeColor;


//    /** 定点坐标  */
//    private final float[] sPos3={
//            -1.0f,1.0f,
//            -1.0f,-1.0f,
//            1.0f,1.0f,
//            1.0f,-1.0f
//    };

    /** 定点坐标  */
    private final float[] sPos1={
            -1.2f,1.2f,
            -1.2f,0.7f,
            -0.7f,1.2f,
            -0.7f,0.7f
    };

    /** 定点坐标  */
    private final float[] sPos2={
            -0.5f,1.0f,
            -0.5f,0.5f,
            0.0f,1.0f,
            0.0f,0.5f
    };

    /** 定点坐标  */
    private final float[] sPos3={
            0.0f,1.0f,
            0.0f,0.5f,
            0.5f,1.0f,
            0.5f,0.5f
    };

    /** 定点坐标  */
    private final float[] sPos4={
            0.5f,1.0f,
            0.5f,0.5f,
            1.0f,1.0f,
            1.0f,0.5f
    };

    /** 定点坐标  */
    private final float[] sPos5={
            -1.0f,0.5f,
            -1.0f,0.0f,
            -0.5f,0.5f,
            -0.5f,0.0f
    };

    /** 定点坐标  */
    private final float[] sPos6={
            -0.5f,0.5f,
            -0.5f,0.0f,
            0.0f,0.5f,
            0.0f,0.0f
    };

    /** 定点坐标  */
    private final float[] sPos7={
            0.0f,0.5f,
            0.0f,0.0f,
            0.5f,0.5f,
            0.5f,0.0f
    };

    /** 定点坐标  */
    private final float[] sPos8={
            0.5f,0.5f,
            0.5f,0.0f,
            1.0f,0.5f,
            1.0f,0.0f
    };

    /** 定点坐标  */
    private final float[] sPos9={
            -1.0f,0.0f,
            -1.0f,-0.5f,
            -0.5f,0.0f,
            -0.5f,-0.5f
    };

    /** 定点坐标  */
    private final float[] sPos10={
            -0.5f,0.0f,
            -0.5f,-0.5f,
            0.0f,0.0f,
            0.0f,-0.5f
    };

    /** 定点坐标  */
    private final float[] sPos11={
            0.0f,0.0f,
            0.0f,-0.5f,
            0.5f,0.0f,
            0.5f,-0.5f
    };

    /** 定点坐标  */
    private final float[] sPos12={
            0.5f,0.0f,
            0.5f,-0.5f,
            1.0f,0.0f,
            1.0f,-0.5f
    };

    /** 定点坐标  */
    private final float[] sPos13={
            -1.0f,-0.5f,
            -1.0f,-1.0f,
            -0.5f,-0.5f,
            -0.5f,-1.0f
    };

    /** 定点坐标  */
    private final float[] sPos14={
            -0.5f,-0.5f,
            -0.5f,-1.0f,
            0.5f,-0.5f,
            0.5f,-1.0f
    };

    /** 定点坐标  */
    private final float[] sPos15={
            0.0f,-0.5f,
            0.0f,-1.0f,
            0.5f,-0.5f,
            1.0f,-1.0f
    };

    /** 定点坐标  */
    private final float[] sPos16={
            0.5f,-0.5f,
            1.0f,-1.0f,
            1.0f,-0.5f,
            1.0f,-1.0f
    };

    /** 纹理坐标  */
    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };

    public DemoRandene(Context context) {
        this.context = context;
        this.filter = Filter.NONE;

        this.vertex="half_color_vertex.sh";
        this.fragment="half_color_fragment.sh";
        ByteBuffer bb=ByteBuffer.allocateDirect(sPos1.length*4);
        bb.order(ByteOrder.nativeOrder());
        bPos1=bb.asFloatBuffer();
        bPos1.put(sPos1);
        bPos1.position(0);


        ByteBuffer bb2=ByteBuffer.allocateDirect(sPos2.length*4);
        bb2.order(ByteOrder.nativeOrder());
        bPos2=bb2.asFloatBuffer();
        bPos2.put(sPos2);
        bPos2.position(0);

        ByteBuffer bb3=ByteBuffer.allocateDirect(sPos3.length*4);
        bb3.order(ByteOrder.nativeOrder());
        bPos3=bb3.asFloatBuffer();
        bPos3.put(sPos3);
        bPos3.position(0);

        ByteBuffer bb4=ByteBuffer.allocateDirect(sPos4.length*4);
        bb4.order(ByteOrder.nativeOrder());
        bPos4=bb4.asFloatBuffer();
        bPos4.put(sPos4);
        bPos4.position(0);



        ByteBuffer bb5=ByteBuffer.allocateDirect(sPos5.length*4);
        bb5.order(ByteOrder.nativeOrder());
        bPos5=bb5.asFloatBuffer();
        bPos5.put(sPos5);
        bPos5.position(0);


        ByteBuffer bb6=ByteBuffer.allocateDirect(sPos6.length*4);
        bb6.order(ByteOrder.nativeOrder());
        bPos6=bb6.asFloatBuffer();
        bPos6.put(sPos6);
        bPos6.position(0);

        ByteBuffer bb7=ByteBuffer.allocateDirect(sPos7.length*4);
        bb7.order(ByteOrder.nativeOrder());
        bPos7=bb7.asFloatBuffer();
        bPos7.put(sPos7);
        bPos7.position(0);

        ByteBuffer bb8=ByteBuffer.allocateDirect(sPos8.length*4);
        bb8.order(ByteOrder.nativeOrder());
        bPos8=bb8.asFloatBuffer();
        bPos8.put(sPos8);
        bPos8.position(0);

        ByteBuffer bb9=ByteBuffer.allocateDirect(sPos9.length*4);
        bb9.order(ByteOrder.nativeOrder());
        bPos9=bb9.asFloatBuffer();
        bPos9.put(sPos9);
        bPos9.position(0);


        ByteBuffer bb10=ByteBuffer.allocateDirect(sPos10.length*4);
        bb10.order(ByteOrder.nativeOrder());
        bPos10=bb10.asFloatBuffer();
        bPos10.put(sPos10);
        bPos10.position(0);


        ByteBuffer bb11=ByteBuffer.allocateDirect(sPos11.length*4);
        bb11.order(ByteOrder.nativeOrder());
        bPos11=bb11.asFloatBuffer();
        bPos11.put(sPos11);
        bPos11.position(0);


        ByteBuffer bb12=ByteBuffer.allocateDirect(sPos12.length*4);
        bb12.order(ByteOrder.nativeOrder());
        bPos12=bb12.asFloatBuffer();
        bPos12.put(sPos12);
        bPos12.position(0);


        ByteBuffer bb13=ByteBuffer.allocateDirect(sPos13.length*4);
        bb13.order(ByteOrder.nativeOrder());
        bPos13=bb13.asFloatBuffer();
        bPos13.put(sPos13);
        bPos13.position(0);


        ByteBuffer bb14=ByteBuffer.allocateDirect(sPos14.length*4);
        bb14.order(ByteOrder.nativeOrder());
        bPos14=bb14.asFloatBuffer();
        bPos14.put(sPos14);
        bPos14.position(0);


        ByteBuffer bb15=ByteBuffer.allocateDirect(sPos15.length*4);
        bb15.order(ByteOrder.nativeOrder());
        bPos15=bb15.asFloatBuffer();
        bPos15.put(sPos15);
        bPos15.position(0);


        ByteBuffer bb16=ByteBuffer.allocateDirect(sPos16.length*4);
        bb16.order(ByteOrder.nativeOrder());
        bPos16=bb16.asFloatBuffer();
        bPos16.put(sPos16);
        bPos16.position(0);


        ByteBuffer cc=ByteBuffer.allocateDirect(sCoord.length*4);
        cc.order(ByteOrder.nativeOrder());
        bCoord=cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }



    public void setBitmap(Bitmap bitmap1,Bitmap bitmap2) {
        this.bitmap1 = bitmap1;
        this.bitmap2 = bitmap2;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        mProgram=ShaderUtils.createProgram(context.getResources(),vertex,fragment);
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        glHMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        hIsHalf=GLES20.glGetUniformLocation(mProgram,"vIsHalf");
        glHUxy=GLES20.glGetUniformLocation(mProgram,"uXY");
        onDrawCreatedSet(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        int aw=bitmap1.getWidth();
        int ah=bitmap1.getHeight();
        float aWH=aw/(float)ah;
        float aWidthHeight=width/(float)height;
        uXY=aWidthHeight;
        if(width>height){
            if(aWH>aWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -aWidthHeight*aWH,aWidthHeight*aWH, -1.4f,1.4f, 3, 5);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -aWidthHeight/aWH,aWidthHeight/aWH, -1.4f,1.4f, 3, 5);
            }
        }else{
//            if(aWH>aWidthHeight){
//                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/aWidthHeight*aWH, 1/aWidthHeight*aWH,3, 5);
//            }else{
//                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -aWH/aWidthHeight, aWH/aWidthHeight,3, 5);
//            }
            if(aWH>aWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1,3, 5);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1,3, 5);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        onDrawSet();
        GLES20.glUniform1i(hIsHalf,0);
        //是否是一半
        GLES20.glUniform1f(glHUxy,uXY);
        GLES20.glUniformMatrix4fv(glHMatrix,1,false,mMVPMatrix,0);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture, 0);
        textureId=createTexture1();
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos1);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos2);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos3);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos4);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos5);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos6);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos7);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos8);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos9);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos10);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos11);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos12);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);


//        GLES20.glUniform1i(hIsHalf,0);
//        //是否是一半
//        GLES20.glUniform1f(glHUxy,uXY);
//        GLES20.glUniformMatrix4fv(glHMatrix,1,false,mMVPMatrix,0);
//        GLES20.glEnableVertexAttribArray(glHPosition);
//        GLES20.glEnableVertexAttribArray(glHCoordinate);
//        GLES20.glUniform1i(glHTexture, 0);
        textureId=createTexture2();
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos13);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos14);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos15);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos16);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

    }

    private int createTexture1(){
        int[] texture=new int[1];
        if(bitmap1!=null&&!bitmap1.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap1, 0);
            return texture[0];
        }
        return 0;
    }

    private int createTexture2(){
        int[] texture=new int[1];
        if(bitmap2!=null&&!bitmap2.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap2, 0);
            return texture[0];
        }
        return 0;
    }

    private void onDrawSet() {
        GLES20.glUniform1i(hChangeType,filter.getType());
        GLES20.glUniform3fv(hChangeColor,1,filter.data(),0);
    }

    private void onDrawCreatedSet(int mProgram) {
        hChangeType=GLES20.glGetUniformLocation(mProgram,"vChangeType");
        hChangeColor=GLES20.glGetUniformLocation(mProgram,"vChangeColor");
    }



}