package com.ritu.util;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class ProjectionMatrixHelper {
    private int program;
    private String name;

    public ProjectionMatrixHelper(int program, String name) {
        this.program = program;
        this.name = name;
        uMatrixLocation = GLES20.glGetUniformLocation(program, name);
    }

    private int uMatrixLocation;

    private float[] mProjectionMatrix = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public void enable(int width,int height) {
        float aspectRatio = (width > height) ? width / height : height / width;
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
    }

}
