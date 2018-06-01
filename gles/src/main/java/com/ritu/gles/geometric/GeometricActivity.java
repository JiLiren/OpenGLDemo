package com.ritu.gles.geometric;

import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.ritu.gles.R;

/**
 * @author vurtne on 1-Jun-18
 * */
public class GeometricActivity extends AppCompatActivity {

    private LinearLayout mParentLayout;
    private Toolbar mToolbar;
    private GLSurfaceView mSurfaceView;
    private GeometricRender mRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geometric);
        initView();
    }

    private void initView(){
        mParentLayout = findViewById(R.id.layout_parent);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("绘制三角形");
        mToolbar.setTitleTextColor(Color.WHITE);
        mSurfaceView = new GLSurfaceView(this);
        mParentLayout.addView(mSurfaceView);
        mSurfaceView. setEGLContextClientVersion(2);
        mSurfaceView.setRenderer( mRender = new GeometricRender());
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mRender.setEnum(GeometricRender.GeometricEnum.Triangle);
        mSurfaceView.requestRender();
    }


}
