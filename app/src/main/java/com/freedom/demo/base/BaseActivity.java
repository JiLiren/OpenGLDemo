package com.freedom.demo.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.freedom.demo.R;

import java.util.concurrent.TimeUnit;


/**
 * @author vurtne on 1-May-18.
 *
 */
@SuppressWarnings({"unused"})
public abstract class BaseActivity extends AppCompatActivity {

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        context = this;
        FrameLayout layout = findViewById(R.id.layout_content);
        ImageButton back = findViewById(R.id.iv_back);
        back.setOnClickListener( o -> {
            finish();
        });
        TextView titleView = findViewById(R.id.tv_title);
        titleView.setText(initTitle());
        layout.addView(contentView());
        initView(savedInstanceState);
        if (StatusBarUtil.canStatusChangeColor()) {
            StatusBarUtil.setTranslucentForImageView(this, 0, null, true);
            StatusBarUtil.setStatusContentColor(this,true);
        } else {
            StatusBarUtil.setTranslucentForImageView(this, 40, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = DeviceUtil.getStatusBarHeight(this);
            AppBarLayout appBarLayout = findViewById(R.id.layout_bar);
            LinearLayout.LayoutParams appBarParams = (LinearLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarParams.height += height / 1.5;
            appBarLayout.setPadding(0,  height / 2,0,0);
            appBarLayout.setLayoutParams(appBarParams);
        }
        initEvent();
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 设置ContentView
     *
     * @return 布局ID
     */
    protected abstract View contentView();


    /**
     * 界面设置
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 事件监听
     */
    protected abstract void initEvent();

    /**
     * 事件监听
     */
    protected abstract String initTitle();

    /**
     * 数据处理
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

}
