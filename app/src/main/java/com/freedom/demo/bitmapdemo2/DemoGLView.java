package com.freedom.demo.bitmapdemo2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;

import java.io.IOException;

public class DemoGLView extends GLSurfaceView {

    private Context context;
    private DemoRandene mRanderer;

    public DemoGLView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        mRanderer = new DemoRandene(context);
        setRenderer(mRanderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        try {
            mRanderer.setBitmap(BitmapFactory.decodeStream(getResources().getAssets().open("cat.png")));
            requestRender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DemoRandene getRanderer() {
        return mRanderer;
    }
}
