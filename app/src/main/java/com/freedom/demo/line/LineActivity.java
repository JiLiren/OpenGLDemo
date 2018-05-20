package com.freedom.demo.line;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.freedom.demo.Util;
import com.freedom.demo.point.PointRenderer;

public class LineActivity extends AppCompatActivity {
    private GLSurfaceView mSurfaceView;
    private LineRenderer mRederer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Util.isSupported(this)) {
            mSurfaceView = new GLSurfaceView(this);
            mSurfaceView.setRenderer(mRederer = new LineRenderer());
            setContentView(mSurfaceView);
        }else {
            Toast.makeText(this,"手机不支持",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
    }
}
