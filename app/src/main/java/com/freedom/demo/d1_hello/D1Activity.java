package com.freedom.demo.d1_hello;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.freedom.demo.base.BaseActivity;

/**
 * @author Vuetne on 2-Aug-18
 * */
public class D1Activity extends BaseActivity {

    private GLSurfaceView mView;

    @Override
    protected View contentView() {
        mView = new GLSurfaceView(this);
        return mView;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mView.setRenderer(new D1Render());
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
