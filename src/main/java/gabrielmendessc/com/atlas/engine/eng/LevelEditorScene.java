package gabrielmendessc.com.atlas.engine.eng;

import gabrielmendessc.com.atlas.engine.components.FontRenderer;
import gabrielmendessc.com.atlas.engine.components.SpriteRenderer;
import gabrielmendessc.com.atlas.engine.renderer.Shader;
import gabrielmendessc.com.atlas.engine.renderer.Texture;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelEditorScene extends Scene {

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;
    GameObject testObj;
    private boolean firstTime = true;

    private float[] vertexArray = {
            //position                 //color                      //uv coordinates
            90.5f,  -90.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,      1, 1, //Bottom right 0
           -90.5f,  90.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f,      0, 0, //Top left     1
            90.5f,  90.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f,      1, 0, //Top right    2
           -90.5f, -90.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,      0, 1, //Bottom left  3
    };
    //Counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, //Top right triangle
            0, 1, 3, //Bottom left triangle
    };


    @Override
    public void init() {

        System.out.println("Creating 'test object'");

        this.testObj = new GameObject("test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObject(this.testObj);

        this.camera = new Camera(new Vector2f());

        defaultShader = new Shader("/shaders/default.glsl");
        defaultShader.compile();

        this.testTexture = new Texture("/images/testImage.png");

        //Generate Vertex Array Object(VAO), Vertex Buffer Object(VBO) and Element Buffer Object(EBO) Buffers
        vaoID = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vaoID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexBuffer, GL46.GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL46.GL_STATIC_DRAW);

        //Add the vertex attribute pointer
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSize = (positionSize + colorSize + uvSize) * Float.BYTES;
        GL46.glVertexAttribPointer(0, positionSize, GL46.GL_FLOAT, false, vertexSize, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glVertexAttribPointer(1, colorSize, GL46.GL_FLOAT, false, vertexSize, positionSize * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);

        GL46.glVertexAttribPointer(2, uvSize, GL46.GL_FLOAT, false, vertexSize, (positionSize + colorSize) * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);

    }

    @Override
    public void update(float dt) {

        camera.position.x -= dt * 25.0f;
        camera.position.y -= dt * 25.0f;

        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", (float) GLFW.glfwGetTime());

        //Bind VAO
        GL46.glBindVertexArray(vaoID);
        //Enable vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        //Draw
        GL46.glDrawElements(GL46.GL_TRIANGLES, elementArray.length, GL46.GL_UNSIGNED_INT, 0);

        //Unbid everything
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);

        GL46.glBindVertexArray(0);

        defaultShader.detach();

        if (firstTime) {

            System.out.println("Creating GameObject");
            GameObject gameObject2 = new GameObject("Game Test 2");
            gameObject2.addComponent(new SpriteRenderer());
            this.addGameObject(gameObject2);

            firstTime = false;

        }

        for (GameObject gameObject : this.gameObjectList) {

            gameObject.update(dt);

        }

    }

}
