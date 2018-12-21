package com.ritu.app2.lesson1;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class LessonOneActivity extends Activity {
    /**
     * Hold a reference to our GLSurfaceView
     */
    private GLSurfaceView mGLSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

        // 检查系统是否支持OpenGL ES 2.0。
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // 请求OpenGL ES 2.0兼容的上下文。
            mGLSurfaceView.setEGLContextClientVersion(2);

            // 将渲染器设置为我们的演示渲染器，定义如下。
            mGLSurfaceView.setRenderer(new LessonOneRenderer());
        } else {
            //这是您可以创建OpenGL ES 1.x兼容的地方
            //渲染器，如果您想同时支持ES 1和ES 2。
            return;
        }

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        //活动必须在活动onResume（）上调用GL表面视图的onResume（）。
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        //活动必须在活动onPause（）上调用GL表面视图的onPause（）。
        super.onPause();
        mGLSurfaceView.onPause();
    }
}