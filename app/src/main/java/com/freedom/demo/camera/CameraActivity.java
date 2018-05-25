package com.freedom.demo.camera;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.freedom.demo.R;

public class CameraActivity extends AppCompatActivity {

    private GLSurfaceView mSurfaceView;
    private CameraRandene mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new GLSurfaceView(this);
        mSurfaceView.setEGLContextClientVersion(2);
        mRenderer = new CameraRandene(this);
        mSurfaceView.setRenderer(mRenderer);
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mRenderer.onte();
                mSurfaceView.requestRender();
            }
        });
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //RENDERMODE_CONTINUOUSLY
        setContentView(mSurfaceView);


    }
}
