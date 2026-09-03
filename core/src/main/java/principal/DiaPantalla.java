package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DiaPantalla implements Screen {

    private final Main juego;
    private OrthographicCamera camara;
    private Viewport vista;
    private Texture diaImagen;

    private float temporizador = 0;

    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    public DiaPantalla(Main juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        vista = new FitViewport(ANCHO_VIRTUAL, ALTO_VIRTUAL, camara);
        vista.apply();
        camara.position.set(ANCHO_VIRTUAL / 2f, ALTO_VIRTUAL / 2f, 0);

        diaImagen = new Texture(Gdx.files.internal("MENUS/DIA1.jpg"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        if (juego.batch != null) {
            juego.batch.setProjectionMatrix(camara.combined);
            juego.batch.begin();
            juego.batch.draw(diaImagen, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);
            juego.batch.end();
        }

        temporizador += delta;
        if (temporizador >= 2.0f) {
            juego.setScreen(new JuegoPantalla(juego));
        }
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
        camara.position.set(ANCHO_VIRTUAL / 2f, ALTO_VIRTUAL / 2f, 0);
    }

    @Override
    public void show() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (diaImagen != null) diaImagen.dispose();
    }
}
