package com.ritu.app2.rbgrnlivewallpaper;

import android.opengl.GLSurfaceView.Renderer;

import com.ritu.app2.lesson3.LessonThreeRenderer;


public class LessonThreeWallpaperService extends OpenGLES2WallpaperService {
	@Override
	Renderer getNewRenderer() {
		return new LessonThreeRenderer();
	}
}
