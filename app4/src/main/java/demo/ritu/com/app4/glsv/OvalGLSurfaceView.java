package demo.ritu.com.app4.glsv;

import android.content.Context;
import android.opengl.GLSurfaceView;

import demo.ritu.com.app4.base.BaseGLSurfaceView;
import demo.ritu.com.app4.shape.oval.Ball;
import demo.ritu.com.app4.shape.oval.BallWithLight;
import demo.ritu.com.app4.shape.oval.Cone;
import demo.ritu.com.app4.shape.oval.Cylinder;
import demo.ritu.com.app4.shape.oval.Oval;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制圆形的GLSurfaceView
 */
public class OvalGLSurfaceView extends BaseGLSurfaceView {

    public OvalGLSurfaceView(Context context) {
        super(context);

        setRenderer(new BallWithLightRenderer());

        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    class OvaleRenderer implements Renderer {

        Oval oval;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            oval = new Oval();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            oval.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            oval.draw();
        }
    }


    class ConeRenderer implements Renderer {

        Cone cone = new Cone();

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            cone.onSurfaceCreate();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            cone.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            cone.draw();
        }
    }


    /**
     * 圆柱体渲染器
     */
    class CylinderRenderer implements Renderer {

        Cylinder cylinder = new Cylinder();

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            cylinder.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            cylinder.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            cylinder.draw();
        }
    }

    /**
     * 球体渲染器
     */
    class BallRenderer implements Renderer {

        Ball ball = new Ball();

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            ball.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            ball.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            ball.draw();
        }
    }

    /**
     * 带光源球体渲染器
     */
    class BallWithLightRenderer implements Renderer {

        BallWithLight ballWithLight = new BallWithLight();

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            ballWithLight.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            ballWithLight.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            ballWithLight.draw();
        }
    }

}