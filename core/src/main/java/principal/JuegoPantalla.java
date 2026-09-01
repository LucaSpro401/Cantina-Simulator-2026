package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class JuegoPantalla implements Screen {

    private final Main juego;
    private OrthographicCamera camara;
    private Viewport vista;

    private Texture fondoCantina;
    private Texture personajeSteven;

    // Movimiento y posición del torso
    private float posX = -350;
    private float posY = 210;            // Apoya justo sobre la barra del mostrador
    private float destinoX = 200;        // Se frena frente al jugador
    private float velocidad = 250;
    private float tiempoCaminado = 0;
    private boolean llegoAlMostrador = false;

    // Dimensiones del sprite recortado
    private float anchoPersonaje = 320;
    private float altoPersonaje = 410;

    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    public JuegoPantalla(Main juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        vista = new FitViewport(ANCHO_VIRTUAL, ALTO_VIRTUAL, camara);
        vista.apply();
        camara.position.set(ANCHO_VIRTUAL / 2f, ALTO_VIRTUAL / 2f, 0);

        // Carga de texturas desde sus carpetas correspondientes
        fondoCantina = new Texture(Gdx.files.internal("MENUS/CANTINA DEFI.png"));

        // ASEGURATE de que el nombre del PNG adentro de PERSONAJES sea exacto a este:
        personajeSteven = new Texture(Gdx.files.internal("PERSONAJES/STEVEN SEAGAL.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        actualizarMovimiento(delta);

        camara.update();

        if (juego.batch != null) {
            juego.batch.setProjectionMatrix(camara.combined);

            juego.batch.begin();

            // 1. Dibujar el fondo de la cantina
            juego.batch.draw(fondoCantina, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);

            // 2. Ángulo de inclinación (bamboleo al caminar)
            float anguloInclinacion = 0;
            if (!llegoAlMostrador) {
                anguloInclinacion = (float) Math.sin(tiempoCaminado * 12) * 10f;
            }

            // 3. Dibujar el torso de Steven detras del mostrador
            juego.batch.draw(
                personajeSteven,
                posX, posY,
                anchoPersonaje / 2f, 0,     // Pivote de rotación en el centro inferior del torso
                anchoPersonaje, altoPersonaje,
                1f, 1f,
                anguloInclinacion,
                0, 0,
                personajeSteven.getWidth(),
                personajeSteven.getHeight(),
                false, false
            );

            juego.batch.end();
        }
    }

    private void actualizarMovimiento(float delta) {
        if (!llegoAlMostrador) {
            tiempoCaminado += delta;
            posX += velocidad * delta;

            if (posX >= destinoX) {
                posX = destinoX;
                llegoAlMostrador = true;
            }
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
        if (fondoCantina != null) fondoCantina.dispose();
        if (personajeSteven != null) personajeSteven.dispose();
    }
}
