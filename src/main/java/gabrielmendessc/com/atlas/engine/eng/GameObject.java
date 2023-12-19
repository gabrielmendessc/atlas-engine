package gabrielmendessc.com.atlas.engine.eng;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private String name;
    private List<Component> componentList = new ArrayList<>();

    public GameObject(String name) {
        this.name = name;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {

        for (Component component : componentList) {

            if (!componentClass.isAssignableFrom(component.getClass())) {

                continue;

            }

            return componentClass.cast(component);

        }

        return null;

    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {

        for (int i = 0; i < componentList.size(); i++) {

            if (!componentClass.isAssignableFrom(componentList.get(i).getClass())) {

                continue;

            }

            componentList.remove(i);

            return;

        }

    }

    public void addComponent(Component component) {

        componentList.add(component);

        component.gameObject = this;

    }

    public void update(float dt) {

        for (Component component : componentList) {

            component.update(dt);

        }

    }

    public void start() {

        for (Component component : componentList) {

            component.start();

        }

    }

}
