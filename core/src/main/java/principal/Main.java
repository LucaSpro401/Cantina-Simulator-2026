package principal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        System.out.println("JUEGO INICIADO");

        this.setScreen(new MenuPantalla(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
