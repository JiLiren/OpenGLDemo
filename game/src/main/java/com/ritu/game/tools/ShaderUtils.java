package com.ritu.game.tools;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author vurtne on 3-Jun-18
 * */
public class ShaderUtils {

    private static final String TAG = "ES20_ERROR";

    /**
     * 加载制定着色器的方法
     * */
    public static int loadShader(int shaderType,String source){
        //创建一个着色器
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0){
            //加载着色器源代码
            GLES20.glShaderSource(shader,source);
            //编译着色器
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            //获取编译的结果
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0);
            //检测结果是否身材，如果失败打印日志并且删除着色器
            if (compiled[0] == 0){
                Log.e(TAG,"Could not compile shader" + shaderType + ":" +
                GLES20.glGetShaderInfoLog(shader));
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 创建着色器程序的方法
     * */
    public static int createProgram(String vertexSource,String fragmentSource){
        //加载定点着色器
        int vertexShader =loadShader(GLES20.GL_SHADER_TYPE,vertexSource);
        if (vertexShader ==  0){
            return 0;
        }
        //加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        if (pixelShader == 0){
            return 0;
        }
        //创建程序
        int program = GLES20.glCreateProgram();
        //向创建成功的程序中加入定点着色器和片元着色器
        if (program != 0){
            //加入定点着色器
            GLES20.glAttachShader(program,vertexShader);
            checkGLError("glAttachShader");
            //加入片元着色器
            GLES20.glAttachShader(program,pixelShader);
            checkGLError("glAttachShader");
            //链接程序
            GLES20.glLinkProgram(program);
            //存放链接成功的program状态值的数组
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
            if (linkStatus[0] != GLES20.GL_TRUE){
                //链接失败 打印日志
                Log.e(TAG,"Could not link program: " + GLES20.glGetShaderInfoLog(pixelShader));
                //涮出程序
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    /**
     * 检测每一步操作是否有错误的方法
     * */
    public static void checkGLError(String op){
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.e(TAG,op + ": glError" + error);
            throw new RuntimeException(op + ": glError" + error);
        }
    }

    /**
     * 从SH 脚本中加载着色器内容
     * */
    public static String loadFromAssetsFile(String fname, Resources resources){
        String result = null;
        try {
            InputStream is = resources.getAssets().open(fname);
            int ch = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            while ((ch = is.read()) != -1){
                os.write(ch);
            }
            byte[] buff = os.toByteArray();
            os.close();
            is.close();
            result = new String(buff,"UTF-8");
            result = result.replaceAll("\\r\\n","\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
