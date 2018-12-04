package demo.ritu.com.app4.glsv;

import android.content.Context;
import android.opengl.GLSurfaceView;

import demo.ritu.com.app4.base.BaseGLSurfaceView;
import demo.ritu.com.app4.image.ImageRenderer;
import demo.ritu.com.app4.image.ImageTransformRenderer;

import java.io.IOException;

/**
 * 展示图片的 GLSurfaceView
 */
public class ImageGLSurfaceView extends BaseGLSurfaceView {

    public ImageGLSurfaceView(Context context) throws IOException {
        super(context);

        setRenderer(new ImageRenderer(context));  // 展示图片渲染器

        // setRenderer(new ImageTransformRenderer(context, ImageTransformRenderer.Filter.MAGN));  // 展示图片处理渲染器

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        requestRender();
    }
}
