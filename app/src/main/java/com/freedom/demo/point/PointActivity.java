package com.freedom.demo.point;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.freedom.demo.Util;


/**
 * @author vurtne on 19-May-18.
 */
public class PointActivity extends AppCompatActivity {

    private GLSurfaceView mSurfaceView;
    private PointRenderer mRederer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Util.isSupported(this)) {
            mSurfaceView = new GLSurfaceView(this);
            mSurfaceView.setRenderer(mRederer = new PointRenderer());
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
