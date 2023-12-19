package gabrielmendessc.com.atlas.engine.eng;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.lwjgl.glfw.GLFW;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyListener {

    private static KeyListener keyListener;

    private boolean[] keyPressed = new boolean[350];

    public static KeyListener getInstance() {

        if (KeyListener.keyListener == null) {

            KeyListener.keyListener = new KeyListener();

        }

        return keyListener;

    }

    public static void keyCallback(long hWindow, int key, int scancode, int action, int mods) {

        if (action == GLFW.GLFW_PRESS) {

            getInstance().keyPressed[key] = true;

        } else if (action == GLFW.GLFW_RELEASE) {

            getInstance().keyPressed[key] = false;

        }

    }

    public static boolean isKeyPressed(int keyCode) {
        return getInstance().keyPressed[keyCode];
    }

}
