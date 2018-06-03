precision mediump float;
//接收从顶点着色器传过来的易变变量
varying vec4 vColor;
void main(){
    //给此片元赋颜色值
    gl_FragColor = vColor;
}