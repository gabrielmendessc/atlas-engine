package gabrielmendessc.com.atlas.engine.eng;

import lombok.extern.java.Log;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.util.Objects;


@Log
public class Window {

    private static Window window;
    private static Scene currentScene;

    private int width;
    private int height;
    private String title;
    private long hWindow;

    private Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public static Window getInstance() {

        if (Objects.isNull(Window.window)) {

            Window.window = new Window(1280, 720, "Atlas Engine");

        }

        return Window.window;

    }

    public static void changeScene(int newScene) {

        switch (newScene) {
            case 0 -> currentScene = new LevelEditorScene();
            case 1 -> currentScene = new LevelScene();
            default -> { assert false : "Unknown scene '" + newScene + "'"; }
        }

        currentScene.init();
        currentScene.start();

    }


    public void run() {

        init();
        loop();

        //Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(hWindow);
        GLFW.glfwDestroyWindow(hWindow);

        //Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

    }

    private void init() {

        System.out.println("Initializing GLFW Window");

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {

            throw new IllegalStateException("Unable to initialize GLFW");

        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        hWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (hWindow == MemoryUtil.NULL) {

            throw new RuntimeException("Failed to create the GLFW window");

        }

        GLFW.glfwSetCursorPosCallback(hWindow, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(hWindow, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(hWindow, MouseListener::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(hWindow, KeyListener::keyCallback);
        //TODO - Add support to game-pad

        /*try (MemoryStack memoryStack = MemoryStack.stackPush()) {

            IntBuffer pWidth = memoryStack.mallocInt(1);
            IntBuffer pHeight = memoryStack.mallocInt(1);

            GLFW.glfwGetWindowSize(hWindow, pWidth, pHeight);

            GLFWVidMode glfwVidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(hWindow, (glfwVidMode.width() - pWidth.get(0)) / 2, (glfwVidMode.height() - pHeight.get(0)) / 2);

        }*/

        GLFW.glfwMakeContextCurrent(hWindow);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(hWindow);

        GL.createCapabilities();

        Window.changeScene(0);

    }

    private void loop() {

        float beginTime = (float) GLFW.glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!GLFW.glfwWindowShouldClose(hWindow)) {
            GLFW.glfwPollEvents();

            GL46.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {

                currentScene.update(dt);

            }

            GLFW.glfwSwapBuffers(hWindow);

            endTime = (float) GLFW.glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;

        }

    }

}
