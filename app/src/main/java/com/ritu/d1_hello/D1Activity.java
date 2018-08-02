package com.ritu.d1_hello;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.ritu.base.BaseActivity;

/**
 * @author Vuetne on 2-Aug-18
 * */
public class D1Activity extends BaseActivity {


    @Override
    protected GLSurfaceView.Renderer createRenderer() {
        return new D1Render();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected String initTitle() {
        return "HelloGL";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }
}
