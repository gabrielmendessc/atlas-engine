package gabrielmendessc.com.atlas.engine.eng;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    private boolean running;
    protected List<GameObject> gameObjectList = new ArrayList<>();

    public void init() {}

    public void start() {

        for (GameObject gameObject : gameObjectList) {

            gameObject.start();

        }

        running = true;

    }

    public void addGameObject(GameObject gameObject) {

        if (!running) {

            gameObjectList.add(gameObject);

            return;

        }

        gameObjectList.add(gameObject);
        gameObject.start();

    }

    public abstract void update(float dt);

}
