package com.ritu;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ritu.golbal.BaseRenderer;
import com.ritu.item1.PointRender;
import com.ritu.item2.BasisRender;
import com.ritu.item3.PolygonRender;
import com.ritu.item4.ColorfulRender;
import com.ritu.item5.DynamicRender;
import com.ritu.item6.TextureRender;
import com.ritu.item7.MultipleTextureRender;
import com.ritu.item8.TextRender;

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
                    break;
                case 2:
                    mRender = new PolygonRender(SecondActivity.this);
                    break;
                case 3:
                    mRender = new ColorfulRender(SecondActivity.this);
                    break;
                case 4:
                    mRender = new DynamicRender(SecondActivity.this);
                    break;
                case 5:
                    mRender = new TextureRender(SecondActivity.this);
                    break;
                case 6:
                    mRender = new MultipleTextureRender(SecondActivity.this);
                    break;
                case 7:
                    mRender = new TextRender(SecondActivity.this);
                    break;
                default:break;
            }
        }

        if (mRender != null){
            mSurfaceView.setRenderer(mRender);
            mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//            mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            mSurfaceView.setOnClickListener(v -> mSurfaceView.requestRender());
        }
    }
}
