package com.ritu.item8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLUtils;

import com.ritu.golbal.BaseRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TextRender2 extends BaseRenderer {

    private Context context;

    public TextRender2(Context context) {
        this.context = context;
    }

    /** 位图 */
    private Bitmap bitmap;
    /** 四边形的顶点坐标系 */
    private float[] vertex = new float[]{
            -2.5f,-2.5f,0,
            2.5f,-2.5f,0,
            -2.5f,2.5f,0,
            2.5f,2.5f,0
    };
    /** 纹理坐标系 */
    private float[] coord = new float[]{
            0,1.0f,
            1.0f,1.0f,
            0,0,
            1.0f,0
    };
    /** 纹理存储定义，一般用来存名称   */
    private int[] textures = new int[1];
    /** 顶点、纹理缓冲 */
    FloatBuffer vertexBuffer;
    FloatBuffer coordBuffer;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initFontBitmap();

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        // 黑色背景色
        gl.glClearColorx(0, 0, 0, 0);
        // 启用阴影平滑
        gl.glShadeModel(GL10.GL_SMOOTH);
        // 启用深度测试
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // 深度测试类型
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // 设置深度缓存
        gl.glClearDepthf(1.0f);

        // 启用纹理
        gl.glEnable(GL10.GL_TEXTURE_2D);
        // 创建纹理
        gl.glGenTextures(1, textures, 0);
        // 绑定纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        //生成纹理
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        //线性滤波
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);//放大时
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);//缩小时
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置场景大小
        gl.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        //投影矩阵
        gl.glMatrixMode(GL10.GL_PROJECTION);
        //重置视图
        gl.glLoadIdentity();
        //设置视图的大小
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 15);
        // 设置观察模型
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        //准备顶点缓冲
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);
        //准备纹理缓冲
        ByteBuffer coordbb = ByteBuffer.allocateDirect(coord.length * 4);
        coordbb.order(ByteOrder.nativeOrder());
        coordBuffer = coordbb.asFloatBuffer();
        coordBuffer.put(coord);
        coordBuffer.position(0);
        //开启顶点和纹理缓冲
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //往里面进去一点
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        //设置顶点和纹理的位置、类型
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
        //绘图
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        //取消缓冲
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //结束绘图
        gl.glFinish();
    }

    /**
     * android中绘制字体，使用画布canvas
     */
    public void initFontBitmap(){
        String font = "需要渲染的文字测试！";
        bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //背景颜色
        canvas.drawColor(Color.LTGRAY);
        Paint p = new Paint();
        //字体设置
        String fontType = "宋体";
        Typeface typeface = Typeface.create(fontType, Typeface.BOLD);
        //消除锯齿
        p.setAntiAlias(true);
        //字体为红色
        p.setColor(Color.RED);
        p.setTypeface(typeface);
        p.setTextSize(28);
        //绘制字体
        canvas.drawText(font, 0, 100, p);
    }
}
