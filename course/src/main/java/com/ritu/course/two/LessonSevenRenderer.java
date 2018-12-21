package com.ritu.course.two;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.ritu.course.MainActivity;
import com.ritu.course.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * 此类实现我们的自定义渲染器。 注意GL10参数
 * 传入的OpenGL ES 2.0渲染器尚未使用 - 静态类GLES20是
 * 改为使用。
 */
public class LessonSevenRenderer implements GLSurfaceView.Renderer {
    /**
     * 用于调试日志。
     */
    private static final String TAG = "LessonSevenRenderer";

    private final MainActivity mActivity;
    private final GLSurfaceView mGlSurfaceView;

    /**
     * 存储模型矩阵。 该矩阵用于从对象空间移动模型（可以考虑每个模型）
     * 位于宇宙中心）到世界空间。
     */
    private float[] mModelMatrix = new float[16];

    /**
     * 存储视图矩阵。 这可以被认为是我们的相机。 该矩阵将世界空间转换为眼睛空间;
     * 它定位相对于我们眼睛的东西。
     */
    private float[] mViewMatrix = new float[16];

    /**
     * 存储投影矩阵。 这用于将场景投影到2D视口上。
     */
    private float[] mProjectionMatrix = new float[16];

    /**
     * 为最终组合矩阵分配存储空间。 这将被传递到着色器程序。
     */
    private float[] mMVPMatrix = new float[16];

    /**
     * 存储累积的旋转。
     */
    private final float[] mAccumulatedRotation = new float[16];

    /**
     * 存储当前旋转。
     */
    private final float[] mCurrentRotation = new float[16];

    /**
     * 临时矩阵.
     */
    private float[] mTemporaryMatrix = new float[16];

    /**
     * 存储专门针对灯光位置的模型矩阵的副本。
     */
    private float[] mLightModelMatrix = new float[16];

    /**
     * 这将用于传递变换矩阵。
     */
    private int mMVPMatrixHandle;

    /**
     * 这将用于传递模型视图矩阵。
     */
    private int mMVMatrixHandle;

    /**
     * 这将用于传递光线位置。
     */
    private int mLightPosHandle;

    /**
     * 这将用于传递纹理。
     */
    private int mTextureUniformHandle;

    /**
     * 这将用于传递模型位置信息。
     */
    private int mPositionHandle;

    /**
     * 这将用于传递模型正常信息。
     */
    private int mNormalHandle;

    /**
     * 这将用于传递模型纹理坐标信息。
     */
    private int mTextureCoordinateHandle;

    /**
     * 多维数据集生成的其他信息
     */
    private int mLastRequestedCubeFactor;
    private int mActualCubeFactor;

    /**
     *控制是否将顶点缓冲区对象或客户端内存用于渲染。
     */
    private boolean mUseVBOs = true;

    /**
     * 控制是否使用步幅。
     */
    private boolean mUseStride = true;

    /**
     * 元素中位置数据的大小。
     */
    static final int POSITION_DATA_SIZE = 3;

    /**
     * 元素中正常数据的大小。
     */
    static final int NORMAL_DATA_SIZE = 3;

    /**
     * 元素中纹理坐标数据的大小。
     */
    static final int TEXTURE_COORDINATE_DATA_SIZE = 2;

    /**
     * 每个浮点数多少字节。
     */
    static final int BYTES_PER_FLOAT = 4;

    /**
     *用于在模型空间中保持以原点为中心的灯光。 我们需要第4个坐标，以便我们可以在何时获得翻译
     *我们将它乘以变换矩阵。
     */
    private final float[] mLightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

    /**
     * 用于保持光在世界空间中的当前位置（通过模型矩阵转换后）。
     */
    private final float[] mLightPosInWorldSpace = new float[4];

    /**
     * 用于保持光在眼睛空间中的变换位置（通过模型视图矩阵进行变换后）
     */
    private final float[] mLightPosInEyeSpace = new float[4];

    /**
     *这是我们的立方体着色程序的句柄。
     */
    private int mProgramHandle;

    /**
     * 这些是我们的纹理数据的句柄。
     */
    private int mAndroidDataHandle;

    //这些仍然没有波动，但不能保证刷新。
    public volatile float mDeltaX;
    public volatile float mDeltaY;

    /**
     * 用于在后台生成多维数据集数据的线程执行程序。
     */
    private final ExecutorService mSingleThreadedExecutor = Executors.newSingleThreadExecutor();

    /**
     * 当前的立方体对象。
     */
    private Cubes mCubes;
    private Cubes mCubes2;

    /**
     * 初始化模型数据。
     */
    public LessonSevenRenderer(final MainActivity lessonSevenActivity, final GLSurfaceView glSurfaceView) {
        mActivity = lessonSevenActivity;
        mGlSurfaceView = glSurfaceView;
    }

    private void generateCubes(int cubeFactor, boolean toggleVbos, boolean toggleStride) {
        mSingleThreadedExecutor.submit(new GenDataRunnable(cubeFactor, toggleVbos, toggleStride));
    }

    class GenDataRunnable implements Runnable {
        final int mRequestedCubeFactor;
        final boolean mToggleVbos;
        final boolean mToggleStride;

        GenDataRunnable(int requestedCubeFactor, boolean toggleVbos, boolean toggleStride) {
            mRequestedCubeFactor = requestedCubeFactor;
            mToggleVbos = toggleVbos;
            mToggleStride = toggleStride;
        }

        @Override
        public void run() {
            try {
                // X, Y, Z
                // The normal is used in light calculations and is a vector which points
                // orthogonal to the plane of the surface. For a cube model, the normals
                // should be orthogonal to the points of each face.
                final float[] cubeNormalData =
                        {
                                // Front face
                                0.0f, 0.0f, 1.0f,
                                0.0f, 0.0f, 1.0f,
                                0.0f, 0.0f, 1.0f,
                                0.0f, 0.0f, 1.0f,
                                0.0f, 0.0f, 1.0f,
                                0.0f, 0.0f, 1.0f,

                                // Right face
                                1.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f,

                                // Back face
                                0.0f, 0.0f, -1.0f,
                                0.0f, 0.0f, -1.0f,
                                0.0f, 0.0f, -1.0f,
                                0.0f, 0.0f, -1.0f,
                                0.0f, 0.0f, -1.0f,
                                0.0f, 0.0f, -1.0f,

                                // Left face
                                -1.0f, 0.0f, 0.0f,
                                -1.0f, 0.0f, 0.0f,
                                -1.0f, 0.0f, 0.0f,
                                -1.0f, 0.0f, 0.0f,
                                -1.0f, 0.0f, 0.0f,
                                -1.0f, 0.0f, 0.0f,

                                // Top face
                                0.0f, 1.0f, 0.0f,
                                0.0f, 1.0f, 0.0f,
                                0.0f, 1.0f, 0.0f,
                                0.0f, 1.0f, 0.0f,
                                0.0f, 1.0f, 0.0f,
                                0.0f, 1.0f, 0.0f,

                                // Bottom face
                                0.0f, -1.0f, 0.0f,
                                0.0f, -1.0f, 0.0f,
                                0.0f, -1.0f, 0.0f,
                                0.0f, -1.0f, 0.0f,
                                0.0f, -1.0f, 0.0f,
                                0.0f, -1.0f, 0.0f
                        };

                // S，T（或X，Y）
                //纹理坐标数据。
                //因为图像的Y轴指向下方（当您向下移动图像时值会增加）
                // OpenGL的Y轴指向上方，我们通过翻转Y轴来调整它。
                //更重要的是每张脸的纹理坐标都是一样的。
                final float[] cubeTextureCoordinateData =
                        {
                                // Front face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,

                                // Right face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,

                                // Back face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,

                                // Left face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,

                                // Top face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,

                                // Bottom face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f
                        };

                final float[] cubePositionData = new float[108 * mRequestedCubeFactor * mRequestedCubeFactor * mRequestedCubeFactor];
                int cubePositionDataOffset = 0;

                final int segments = mRequestedCubeFactor + (mRequestedCubeFactor - 1);
                final float minPosition = -1.0f;
                final float maxPosition = 1.0f;
                final float positionRange = maxPosition - minPosition;

                for (int x = 0; x < mRequestedCubeFactor; x++) {
                    for (int y = 0; y < mRequestedCubeFactor; y++) {
                        for (int z = 0; z < mRequestedCubeFactor; z++) {
                            final float x1 = minPosition + ((positionRange / segments) * (x * 2));
                            final float x2 = minPosition + ((positionRange / segments) * ((x * 2) + 1));

                            final float y1 = minPosition + ((positionRange / segments) * (y * 2));
                            final float y2 = minPosition + ((positionRange / segments) * ((y * 2) + 1));

                            final float z1 = minPosition + ((positionRange / segments) * (z * 2));
                            final float z2 = minPosition + ((positionRange / segments) * ((z * 2) + 1));

                            // Define points for a cube.
                            // X, Y, Z
                            final float[] p1p = {x1, y2, z2};
                            final float[] p2p = {x2, y2, z2};
                            final float[] p3p = {x1, y1, z2};
                            final float[] p4p = {x2, y1, z2};
                            final float[] p5p = {x1, y2, z1};
                            final float[] p6p = {x2, y2, z1};
                            final float[] p7p = {x1, y1, z1};
                            final float[] p8p = {x2, y1, z1};

                            final float[] thisCubePositionData = ShapeBuilder.generateCubeData(p1p, p2p, p3p, p4p, p5p, p6p, p7p, p8p,
                                    p1p.length);

                            System.arraycopy(thisCubePositionData, 0, cubePositionData, cubePositionDataOffset, thisCubePositionData.length);
                            cubePositionDataOffset += thisCubePositionData.length;
                        }
                    }
                }

                // Run on the GL thread -- the same thread the other members of the renderer run in.
                mGlSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        if (mCubes != null) {
                            mCubes.release();
                            mCubes = null;
                        }
                        if (mCubes2 != null){
                            mCubes2.release();
                            mCubes2 = null;
                        }

                        // Not supposed to manually call this, but Dalvik sometimes needs some additional prodding to clean up the heap.
                        System.gc();

                        try {
                            boolean useVbos = mUseVBOs;
                            boolean useStride = mUseStride;

                            if (mToggleVbos) {
                                useVbos = !useVbos;
                            }

                            if (mToggleStride) {
                                useStride = !useStride;
                            }

                            if (useStride) {
                                if (useVbos) {
                                    mCubes = new CubesWithVboWithStride(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                    mCubes2 = new CubesWithVboWithStride(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                } else {
                                    mCubes = new CubesClientSideWithStride(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                    mCubes2 = new CubesClientSideWithStride(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                }
                            } else {
                                if (useVbos) {
                                    mCubes = new CubesWithVbo(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                    mCubes2 = new CubesWithVbo(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                } else {
                                    mCubes = new CubesClientSide(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                    mCubes2 = new CubesClientSide(cubePositionData, cubeNormalData, cubeTextureCoordinateData, mRequestedCubeFactor);
                                }
                            }

                            mUseVBOs = useVbos;
                            mActivity.updateVboStatus(mUseVBOs);

                            mUseStride = useStride;
                            mActivity.updateStrideStatus(mUseStride);

                            mActualCubeFactor = mRequestedCubeFactor;
                        } catch (OutOfMemoryError err) {
                            if (mCubes != null) {
                                mCubes.release();
                                mCubes = null;
                            }
                            if (mCubes2 != null) {
                                mCubes2.release();
                                mCubes2 = null;
                            }
                            // Not supposed to manually call this, but Dalvik sometimes needs some additional prodding to clean up the heap.
                            System.gc();

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//									Toast.makeText(mActivity, "Out of memory; Dalvik takes a while to clean up the memory. Please try again.\nExternal bytes allocated=" + dalvik.system.VMRuntime.getRuntime().getExternalBytesAllocated(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            } catch (OutOfMemoryError e) {
                // Not supposed to manually call this, but Dalvik sometimes needs some additional prodding to clean up the heap.
                System.gc();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//						Toast.makeText(mActivity, "Out of memory; Dalvik takes a while to clean up the memory. Please try again.\nExternal bytes allocated=" + dalvik.system.VMRuntime.getRuntime().getExternalBytesAllocated(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void decreaseCubeCount() {
        if (mLastRequestedCubeFactor > 1) {
            generateCubes(--mLastRequestedCubeFactor, false, false);
        }
    }

    public void increaseCubeCount() {
        if (mLastRequestedCubeFactor < 16) {
            generateCubes(++mLastRequestedCubeFactor, false, false);
        }
    }

    public void toggleVBOs() {
        generateCubes(mLastRequestedCubeFactor, true, false);
    }

    public void toggleStride() {
        generateCubes(mLastRequestedCubeFactor, false, true);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        mLastRequestedCubeFactor = mActualCubeFactor = 3;
        generateCubes(mActualCubeFactor, false, false);

        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // 使用剔除去除背面。
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader = RawResourceReader.readTextFileFromRawResource(mActivity, R.raw.lesson_seven_vertex_shader);
        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(mActivity, R.raw.lesson_seven_fragment_shader);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"a_Position", "a_Normal", "a_TexCoordinate"});

        // Load the texture
        mAndroidDataHandle = TextureHelper.loadTexture(mActivity, R.mipmap.ic_launcher);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mAndroidDataHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mAndroidDataHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);

        // 初始化累积的旋转矩阵
        Matrix.setIdentityM(mAccumulatedRotation, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 1000.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mProgramHandle);

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_LightPos");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");

        // 计算光的位置。 推到远处。
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -1.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        //画一个立方体
        //将立方体翻译成屏幕。
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3.5f);

        // 设置包含当前旋转的矩阵。
        Matrix.setIdentityM(mCurrentRotation, 0);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        mDeltaX = 0.0f;
        mDeltaY = 0.0f;

        // Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

        // Rotate the cube taking the overall rotation into account.     	
        Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);

        // This multiplies the view matrix by the model matrix, and stores
        // the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix,
        // and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Pass in the texture information
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mAndroidDataHandle);

        // Tell the texture uniform sampler to use this texture in the
        // shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        if (mCubes != null) {
            mCubes.render();
        }
    }

    abstract class Cubes {
        abstract void render();

        abstract void release();

        FloatBuffer[] getBuffers(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            // First, copy cube information into client-side floating point buffers.
            final FloatBuffer cubePositionsBuffer;
            final FloatBuffer cubeNormalsBuffer;
            final FloatBuffer cubeTextureCoordinatesBuffer;

            cubePositionsBuffer = ByteBuffer.allocateDirect(cubePositions.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            cubePositionsBuffer.put(cubePositions).position(0);

            cubeNormalsBuffer = ByteBuffer.allocateDirect(cubeNormals.length * BYTES_PER_FLOAT * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();

            for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor * generatedCubeFactor); i++) {
                cubeNormalsBuffer.put(cubeNormals);
            }

            cubeNormalsBuffer.position(0);

            cubeTextureCoordinatesBuffer = ByteBuffer.allocateDirect(cubeTextureCoordinates.length * BYTES_PER_FLOAT * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();

            for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor * generatedCubeFactor); i++) {
                cubeTextureCoordinatesBuffer.put(cubeTextureCoordinates);
            }

            cubeTextureCoordinatesBuffer.position(0);

            return new FloatBuffer[]{cubePositionsBuffer, cubeNormalsBuffer, cubeTextureCoordinatesBuffer};
        }

        FloatBuffer getInterleavedBuffer(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            final int cubeDataLength = cubePositions.length
                    + (cubeNormals.length * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor)
                    + (cubeTextureCoordinates.length * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor);
            int cubePositionOffset = 0;
            int cubeNormalOffset = 0;
            int cubeTextureOffset = 0;

            final FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(cubeDataLength * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();

            for (int i = 0; i < generatedCubeFactor * generatedCubeFactor * generatedCubeFactor; i++) {
                for (int v = 0; v < 36; v++) {
                    cubeBuffer.put(cubePositions, cubePositionOffset, POSITION_DATA_SIZE);
                    cubePositionOffset += POSITION_DATA_SIZE;
                    cubeBuffer.put(cubeNormals, cubeNormalOffset, NORMAL_DATA_SIZE);
                    cubeNormalOffset += NORMAL_DATA_SIZE;
                    cubeBuffer.put(cubeTextureCoordinates, cubeTextureOffset, TEXTURE_COORDINATE_DATA_SIZE);
                    cubeTextureOffset += TEXTURE_COORDINATE_DATA_SIZE;
                }

                // The normal and texture data is repeated for each cube.
                cubeNormalOffset = 0;
                cubeTextureOffset = 0;
            }

            cubeBuffer.position(0);

            return cubeBuffer;
        }
    }

    class CubesClientSide extends Cubes {
        private FloatBuffer mCubePositions;
        private FloatBuffer mCubeNormals;
        private FloatBuffer mCubeTextureCoordinates;

        CubesClientSide(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            FloatBuffer[] buffers = getBuffers(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);

            mCubePositions = buffers[0];
            mCubeNormals = buffers[1];
            mCubeTextureCoordinates = buffers[2];
        }

        @Override
        public void render() {
            // Pass in the position information
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, mCubePositions);

            // Pass in the normal information
            GLES20.glEnableVertexAttribArray(mNormalHandle);
            GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, mCubeNormals);

            // Pass in the texture information
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
                    0, mCubeTextureCoordinates);

            // Draw the cubes.
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
        }

        @Override
        public void release() {
            mCubePositions.limit(0);
            mCubePositions = null;
            mCubeNormals.limit(0);
            mCubeNormals = null;
            mCubeTextureCoordinates.limit(0);
            mCubeTextureCoordinates = null;
        }
    }

    class CubesClientSideWithStride extends Cubes {
        private FloatBuffer mCubeBuffer;

        CubesClientSideWithStride(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            mCubeBuffer = getInterleavedBuffer(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);
        }

        @Override
        public void render() {
            final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;

            // Pass in the position information
            mCubeBuffer.position(0);
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, mCubeBuffer);

            // Pass in the normal information
            mCubeBuffer.position(POSITION_DATA_SIZE);
            GLES20.glEnableVertexAttribArray(mNormalHandle);
            GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, mCubeBuffer);

            // Pass in the texture information
            mCubeBuffer.position(POSITION_DATA_SIZE + NORMAL_DATA_SIZE);
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
                    stride, mCubeBuffer);

            // Draw the cubes.
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
        }

        @Override
        public void release() {
            mCubeBuffer.limit(0);
            mCubeBuffer = null;
        }
    }

    class CubesWithVbo extends Cubes {
        final int mCubePositionsBufferIdx;
        final int mCubeNormalsBufferIdx;
        final int mCubeTexCoordsBufferIdx;

        CubesWithVbo(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            FloatBuffer[] floatBuffers = getBuffers(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);

            FloatBuffer cubePositionsBuffer = floatBuffers[0];
            FloatBuffer cubeNormalsBuffer = floatBuffers[1];
            FloatBuffer cubeTextureCoordinatesBuffer = floatBuffers[2];

            // Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.
            final int buffers[] = new int[3];
            GLES20.glGenBuffers(3, buffers, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubePositionsBuffer.capacity() * BYTES_PER_FLOAT, cubePositionsBuffer, GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeNormalsBuffer.capacity() * BYTES_PER_FLOAT, cubeNormalsBuffer, GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[2]);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeTextureCoordinatesBuffer.capacity() * BYTES_PER_FLOAT, cubeTextureCoordinatesBuffer,
                    GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

            mCubePositionsBufferIdx = buffers[0];
            mCubeNormalsBufferIdx = buffers[1];
            mCubeTexCoordsBufferIdx = buffers[2];

            cubePositionsBuffer.limit(0);
            cubePositionsBuffer = null;
            cubeNormalsBuffer.limit(0);
            cubeNormalsBuffer = null;
            cubeTextureCoordinatesBuffer.limit(0);
            cubeTextureCoordinatesBuffer = null;
        }

        @Override
        public void render() {
            // Pass in the position information
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubePositionsBufferIdx);
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);

            // Pass in the normal information
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeNormalsBufferIdx);
            GLES20.glEnableVertexAttribArray(mNormalHandle);
            GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);

            // Pass in the texture information
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeTexCoordsBufferIdx);
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
                    0, 0);

            // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

            // Draw the cubes.
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
        }

        @Override
        public void release() {
            // Delete buffers from OpenGL's memory
            final int[] buffersToDelete = new int[]{mCubePositionsBufferIdx, mCubeNormalsBufferIdx,
                    mCubeTexCoordsBufferIdx};
            GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
        }
    }

    class CubesWithVboWithStride extends Cubes {
        final int mCubeBufferIdx;

        CubesWithVboWithStride(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            FloatBuffer cubeBuffer = getInterleavedBuffer(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);

            // Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.
            final int buffers[] = new int[1];
            GLES20.glGenBuffers(1, buffers, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeBuffer.capacity() * BYTES_PER_FLOAT, cubeBuffer, GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

            mCubeBufferIdx = buffers[0];

            cubeBuffer.limit(0);
            cubeBuffer = null;
        }

        @Override
        public void render() {
            final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;

            // Pass in the position information
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

            // Pass in the normal information
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
            GLES20.glEnableVertexAttribArray(mNormalHandle);
            GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, POSITION_DATA_SIZE * BYTES_PER_FLOAT);

            // Pass in the texture information
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
                    stride, (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * BYTES_PER_FLOAT);

            // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

            // Draw the cubes.
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
        }

        @Override
        public void release() {
            // Delete buffers from OpenGL's memory
            final int[] buffersToDelete = new int[]{mCubeBufferIdx};
            GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
        }
    }
}
