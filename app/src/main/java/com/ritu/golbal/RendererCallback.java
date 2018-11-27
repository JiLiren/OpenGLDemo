package com.ritu.golbal;

import java.nio.ByteBuffer;

public interface RendererCallback {

    /**
     * 渲染完毕
     *
     * @param data   缓存数据
     * @param width  数据宽度
     * @param height 数据高度
     */
    void onRendererDone(ByteBuffer data,int width,int height);
}
