package com.freedom.demo.demo3;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class Demo3Activity extends AppCompatActivity {

    private  GLSurfaceView view;
    private Demo3Renderer mRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GLSurfaceView(this);
        // Create an OpenGL ES 2.0 context.
        view.setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new Demo3Renderer(this);
        view.setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
//        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(view);
    }


    @Override
    public void onPause() {
        super.onPause();
        mRenderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderer.onResume();
    }
}
