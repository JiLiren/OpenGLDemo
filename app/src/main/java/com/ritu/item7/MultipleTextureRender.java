package com.ritu.item7;

import android.content.Context;
import android.opengl.GLES20;

import com.ritu.R;
import com.ritu.golbal.BaseRenderer;
import com.ritu.util.BufferUtil;
import com.ritu.util.ProjectionMatrixHelper;
import com.ritu.util.TextureHelper;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MultipleTextureRender extends BaseRenderer {

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

    private float[] POINT_DATA2 = new float[]{-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f};

    private float[] POINT_DATA = new float[]{2 * -0.5f, -0.5f * 2, 2 * -0.5f, 0.5f * 2, 2 * 0.5f, 0.5f * 2, 2 * 0.5f, -0.5f * 2};

    /**
     * 纹理坐标
     */
    private float[] TEX_VERTEX = new float[]{0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f};

    /**
     * 纹理坐标中每个点占的向量个数
     */
    private int TEX_VERTEX_COMPONENT_COUNT = 2;

    private FloatBuffer mVertexData;
    private FloatBuffer mVertexData2;

    private int uTextureUnitLocation;
    private FloatBuffer mTexVertexBuffer;
    /**
     * 纹理数据
     */
    private TextureHelper.TextureBean mTextureBean;
    private TextureHelper.TextureBean mTextureBean2;
    private ProjectionMatrixHelper mProjectionMatrixHelper;
    private int mAPositionLocation;

    public MultipleTextureRender(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA);
        mVertexData2 = BufferUtil.createFloatBuffer(POINT_DATA2);
        mTexVertexBuffer = BufferUtil.createFloatBuffer(TEX_VERTEX);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        mAPositionLocation = getAttrib("a_Position");
        mProjectionMatrixHelper = new ProjectionMatrixHelper(program, "u_Matrix");
        // 纹理坐标索引
        int aTexCoordLocation = getAttrib("a_TexCoord");
        uTextureUnitLocation = getUniform("u_TextureUnit");
        // 纹理数据
        mTextureBean = TextureHelper.loadTexture(context, R.drawable.pikachu);
        mTextureBean2 = TextureHelper.loadTexture(context, R.drawable.square);

        // 加载纹理坐标
        mTexVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoordLocation);

        GLES20.glClearColor(0f, 0f, 0f, 1f);
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mProjectionMatrixHelper.enable(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);
        drawPikachu();
        drawTuzki();
    }

    private void drawPikachu() {
        mVertexData.position(0);
        GLES20.glVertexAttribPointer(mAPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(mAPositionLocation);

        // 设置当前活动的纹理单元为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 将纹理ID绑定到当前活动的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean.textureId);
        GLES20.glUniform1i(uTextureUnitLocation, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
    }

    private void drawTuzki() {
        mVertexData2.position(0);
        GLES20.glVertexAttribPointer(mAPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData2);
        GLES20.glEnableVertexAttribArray(mAPositionLocation);

        // 绑定新的纹理ID到已激活的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean2.textureId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
    }
}
