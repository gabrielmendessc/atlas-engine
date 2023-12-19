package gabrielmendessc.com.atlas.engine.components;

import gabrielmendessc.com.atlas.engine.eng.Component;

import java.util.Objects;

public class FontRenderer extends Component {

    @Override
    public void start() {

        if (Objects.isNull(gameObject.getComponent(SpriteRenderer.class))) {

            return;

        }

        System.out.println("Found Font Renderer!");

    }

    @Override
    public void update(float dt) {

    }
}
