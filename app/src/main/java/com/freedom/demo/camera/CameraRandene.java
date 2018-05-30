package com.freedom.demo.camera;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRandene implements GLSurfaceView.Renderer {

    private Context context;
    private int textures[] = new int[2];
    private Bitmap bitmap;
    private int bitmap_width,bitmap_height;
    private MyTextureOne myTextureOne;

    private EffectContext effectContext;
    private Effect effect;
    int view_port_width,view_port_height;

    static String appName = "My Application",imageName = "ForeGround";


    public void onte(){

    }
    public CameraRandene(Context context) {
        super();
        this.context = context;
        try {
            InputStream is = context.getResources().getAssets().open("map1.png");
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmapOptions.inScaled = true;
            bitmapOptions.inDensity = 4;
            bitmapOptions.inTargetDensity = (int)context.getResources().getDisplayMetrics().density;
            bitmap = BitmapFactory.decodeStream(is, null, bitmapOptions);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap_width = 512;
        bitmap_height = 512;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        view_port_width = width;
        view_port_height = height;
        GLES20.glViewport(0,0,width, height);
        GLES20.glClearColor(0,0,0,1);
        generateTexture();

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (effectContext == null) {
            effectContext = EffectContext.createWithCurrentGlContext();
        }
        if (effect != null) {
            effect.release();
        }
        documentaryEffect();
        myTextureOne.draw(textures[1]);

    }

    private void generateTexture() {
        GLES20.glGenTextures(2, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        myTextureOne = new MyTextureOne();

    }

    private void documentaryEffect() {
        EffectFactory effectFactory = effectContext.getFactory();
        effect = effectFactory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
        effect.apply(textures[0],bitmap_width,bitmap_height,textures[1]);
    }

}