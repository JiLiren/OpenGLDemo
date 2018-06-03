//总变换矩阵
uniform mat4 uMVPMatrix;
//顶点位置
attribute vec3 aPosition;
//顶点颜色
attribute vec4 aColor;
//用于传递给片元卓舍弃的易变变量
varying vec4 vColor;
void main(){
    //根据总变换矩阵计算此次绘制此顶点的位置
    gl_position = uMVPMatrix * vec4(aPosition,1);
    //将接收的顶点颜色传递给片元着色器
    vColor = aColor;
}