package com.ritu.d2_alert;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.ritu.base.BaseActivity;

/**
 * @author Vuetne on 2-Aug-18
 * */
public class D2Activity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer createRenderer() {
        return new D2Render();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected String initTitle() {
        return "Alert";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
