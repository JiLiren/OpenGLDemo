package com.ritu.d3_start;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ritu.base.BaseActivity;

/**
 * @author Vuetne on 2-Aug-18
 * */
public class D3Activity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer createRenderer() {
        return new D3Render(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected String initTitle() {
        return "Star";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
