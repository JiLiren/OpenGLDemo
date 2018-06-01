package com.ritu.gles.geometric;

import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.ritu.gles.R;
import com.ritu.gles.geometric.GeometricRender.GeometricEnum;

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
        initEvent();
    }

    private void initView(){
        mParentLayout = findViewById(R.id.layout_parent);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("绘制三角形");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mSurfaceView = new GLSurfaceView(this);
        mParentLayout.addView(mSurfaceView);
        mSurfaceView. setEGLContextClientVersion(2);
        mSurfaceView.setRenderer( mRender = new GeometricRender());
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mRender.setEnum(GeometricEnum.Triangle);
        mSurfaceView.requestRender();
    }

    private void initEvent(){
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String tile = "绘制"+ item.getTitle();
                GeometricEnum e = GeometricEnum.Triangle;
                switch (item.getItemId()){
                    case R.id.triangle:
                        e = GeometricEnum.Triangle;
                        break;
                    case R.id.square:
                        e = GeometricEnum.Square;
                        break;
                    default:break;
                }
                mToolbar.setTitle(tile);
                mRender.setEnum(e);
                mSurfaceView.requestRender();
                return false;
            }
        });
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return super.onGenericMotionEvent(event);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_geometric, menu);
        return true;
    }
}
