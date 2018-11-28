package com.ritu.item8;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ritu.golbal.BaseRenderer;
import com.ritu.util.BufferUtil;
import com.ritu.util.ProjectionMatrixHelper;
import com.ritu.util.TextureHelper;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TextRender extends BaseRenderer {


    private Context context;

    private String VERTEX_SHADER =  ""+
            "uniform mat4 u_Matrix;\n"+
            "attribute vec4 a_Position;\n"+
            // 纹理坐标：2个分量，S和T坐标
            "attribute vec2 a_TexCoord;\n"+
            "varying vec2 v_TexCoord;\n"+
            "void main() {\n"+
            "v_TexCoord = a_TexCoord;\n"+
            "gl_Position = u_Matrix * a_Position;\n"+
            "}";
    private String FRAGMENT_SHADER = ""+
            "precision mediump float;\n"+
            "varying vec2 v_TexCoord;\n"+
            // sampler2D：二维纹理数据的数组
            "uniform sampler2D u_TextureUnit;\n"+
            "void main() {\n"+
            "gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);\n"+
            "}";

    private int POSITION_COMPONENT_COUNT = 2;

    private float[] POINT_DATA = new float[]{-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f};

    /**
     * 纹理坐标
     */
    private float[] TEX_VERTEX = new float[]{0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f};

    /**
     * 纹理坐标中每个点占的向量个数
     */
    private int TEX_VERTEX_COMPONENT_COUNT = 2;

    private FloatBuffer mVertexData;

    private int uTextureUnitLocation;
    private FloatBuffer mTexVertexBuffer;

    /**
     * 纹理数据
     */
    private TextureHelper.TextureBean mTextureBean;
    private ProjectionMatrixHelper mProjectionMatrixHelper;

    public TextRender(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA);
        mTexVertexBuffer = BufferUtil.createFloatBuffer(TEX_VERTEX);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        int aPositionLocation = getAttrib("a_Position");
        mProjectionMatrixHelper = new ProjectionMatrixHelper(program, "u_Matrix");
        // 纹理坐标索引
        int aTexCoordLocation = getAttrib("a_TexCoord");
        uTextureUnitLocation = getUniform("u_TextureUnit");
        // 纹理数据
        mTextureBean = TextureHelper.createTextTexture(context, "牛逼呀");

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // 加载纹理坐标
        mTexVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoordLocation);

        GLES20.glClearColor(0f, 0f, 0f, 1f);
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

    }

    private void setPointData(TextureHelper.TextureBean bean){
        float ww = (bean.width /  outputWidth * 2 - 1.0f) * outputRatio;
        float hh = (bean.height /  outputHeight * 2 - 1.0f) * outputRatio;
        POINT_DATA[0] = 0.0f;
        POINT_DATA[1] = 0.0f;
        POINT_DATA[2] = ww;
        POINT_DATA[3] = 0.0f;
        POINT_DATA[4] = ww;
        POINT_DATA[5] = hh;
        POINT_DATA[6] = 0.0f;
        POINT_DATA[7] = hh;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        outputWidth = width;
        outputHeight = height;
        outputRatio = (float) width / height;
//        setPointData(mTextureBean);
        mProjectionMatrixHelper.enable(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // 纹理单元：在OpenGL中，纹理不是直接绘制到片段着色器上，而是通过纹理单元去保存纹理

        // 设置当前活动的纹理单元为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // 将纹理ID绑定到当前活动的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean.textureId);

        // 将纹理单元传递片段着色器的u_TextureUnit
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
    }
}
