package com.ritu.game.demo.triangle;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author vurtne on 3-Jun-18
 * */
public class TriangleActivity extends AppCompatActivity {

    private TDView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("绘制三角形");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mView = new TDView(this);
        //给view 获取焦点
        mView.requestFocus();
        //设置可触摸
        mView.setFocusableInTouchMode(true);
        setContentView(mView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }
}
