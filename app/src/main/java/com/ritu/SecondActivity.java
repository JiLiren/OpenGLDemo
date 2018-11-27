package com.ritu;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ritu.golbal.BaseRenderer;
import com.ritu.item1.PointRender;
import com.ritu.item2.BasisRender;

public class SecondActivity extends AppCompatActivity {
    private String key = "key";

    private GLSurfaceView mSurfaceView;
    private BaseRenderer mRender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        initData();
    }


    private void initView(){
        mSurfaceView = findViewById(R.id.surface);
        mSurfaceView.setEGLContextClientVersion(2);
        mSurfaceView.setEGLConfigChooser(false);
    }

    private void initData(){
        Intent intent = getIntent();
        if (intent != null){
            int type = intent.getIntExtra(key,0);

            switch (type){
                case 0:
                    mRender = new PointRender();
                    break;
                case 1:
                    mRender = new BasisRender(SecondActivity.this);
                default:break;
            }
        }

        if (mRender != null){
            mSurfaceView.setRenderer(mRender);
            mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            mSurfaceView.setOnClickListener(v -> mSurfaceView.requestRender());
        }
    }
}
