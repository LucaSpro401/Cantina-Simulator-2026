package principal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {

    // SpriteBatch público e inicializado para que todas las pantallas lo reutilicen
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        System.out.println("JUEGO INICIADO");

        // Arrancamos el flujo oficial entrando al Menú Principal
        this.setScreen(new MenuPantalla(this));
    }

    @Override
    public void render() {
        // super.render() es VITAL: se encarga de llamar al render() de la pantalla activa
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
