package com.freedom.demo.bitmapdemo2;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.freedom.demo.camera.CameraRandene;

public class DemoTwoActivity extends AppCompatActivity {

    private DemoGLView mSurfaceView;
    private DemoRandene mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new DemoGLView(this);
        setContentView(mSurfaceView);
    }
}
