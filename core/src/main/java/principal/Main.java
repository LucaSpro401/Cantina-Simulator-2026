package principal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import db.DatabaseManager;

public class Main extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.inicializarBaseDeDatos();

        batch = new SpriteBatch();

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
    }
}
