package demo.ritu.com.app4.paint;

public interface PPgles {

    void init(int program, int vertexShader, int fragmentShader);

    String getVertexShader();

    String getFragmentShader();

    void draw();
}
