/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ritu.app3;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * 一个视图容器，可以在屏幕上绘制OpenGL ES图形。
 * 此视图还可用于捕获触摸事件，例如用户
 * 与绘制对象交互。
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        //创建OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        //修复错误没有选择配置，但我不知道这是做什么的.
        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        // 设置渲染器以在GLSurfaceView上绘制
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        //仅在绘图数据发生更改时才渲染视图
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent从触摸屏报告输入详细信息
        //和其他输入控件。 在这种情况下，你是唯一的
        //对触摸位置发生变化的事件感兴趣

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                //在中线上方反向旋转
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                //反向旋转到中线左侧
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                        ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}
