package gabrielmendessc.com.atlas.engine.eng;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MouseListener {

    private static MouseListener mouseListener;

    private final boolean[] mouseButtonPressed = new boolean[3];
    private double scrollX = 0.0, scrollY = 0.0;
    private double xPos = 0.0, yPos = 0.0, lastY = 0.0, lastX = 0.0;
    @Getter
    private boolean isDragging;

    public static MouseListener getInstance() {

        if (Objects.isNull(MouseListener.mouseListener)) {

            MouseListener.mouseListener = new MouseListener();

        }

        return MouseListener.mouseListener;

    }

    public static void mousePosCallback(long hWindow, double xPos, double yPos) {

        getInstance().setLastX(getInstance().xPos);
        getInstance().setLastY(getInstance().yPos);
        getInstance().setXPos(xPos);
        getInstance().setYPos(yPos);
        getInstance().setDragging(getInstance().mouseButtonPressed[0] || getInstance().mouseButtonPressed[1] || getInstance().mouseButtonPressed[2]);

    }

    public static void mouseButtonCallback(long hWindow, int button, int action, int mods) {

        if (action == GLFW.GLFW_PRESS) {

            if (button < getInstance().mouseButtonPressed.length) {

                getInstance().mouseButtonPressed[button] = true;

            }

        } else if (action == GLFW.GLFW_RELEASE) {

            if (button < getInstance().mouseButtonPressed.length) {

                getInstance().mouseButtonPressed[button] = false;
                getInstance().setDragging(false);

            }

        }

    }

    public static void mouseScrollCallback(long hWindow, double xOffset, double yOffset) {

        getInstance().setScrollX(xOffset);
        getInstance().setScrollY(yOffset);

    }

    public static void endFrame() {

        getInstance().setScrollX(0);
        getInstance().setScrollY(0);
        getInstance().setLastX(getInstance().xPos);
        getInstance().setLastY(getInstance().yPos);

    }

    public static float getDx() {
        return (float) (getInstance().lastX - getInstance().xPos);
    }

    public static float getDy() {
        return (float) (getInstance().lastX - getInstance().yPos);
    }

    public static float getScrollX() {
        return (float) getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float) getInstance().scrollY;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < getInstance().mouseButtonPressed.length) {
            return getInstance().mouseButtonPressed[button];
        }
        return false;
    }

}
