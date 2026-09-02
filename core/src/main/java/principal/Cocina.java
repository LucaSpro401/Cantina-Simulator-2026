package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Cocina implements Screen {

    private final Main juego;
    private final JuegoPantalla pantallaCantina;

    private OrthographicCamera camara;
    private Viewport vista;

    private Texture fondoCocina;
    private ShapeRenderer shapeRenderer;

    // Transición
    private boolean entrando = true;
    private boolean saliendo = false;

    private float tiempoTransicion = 0;

    private static final float DURACION_TRANSICION = 0.5f;

    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    public Cocina(Main juego, JuegoPantalla pantallaCantina) {

        this.juego = juego;
        this.pantallaCantina = pantallaCantina;

        camara = new OrthographicCamera();

        vista = new FitViewport(
            ANCHO_VIRTUAL,
            ALTO_VIRTUAL,
            camara
        );

        vista.apply();

        camara.position.set(
            ANCHO_VIRTUAL / 2f,
            ALTO_VIRTUAL / 2f,
            0
        );

        fondoCocina = new Texture(
            Gdx.files.internal("MENUS/COCINFULL.png")
        );

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // -----------------------------------------
        // ENTRADA A LA COCINA
        // -----------------------------------------

        if (entrando) {

            tiempoTransicion += delta;

            if (tiempoTransicion >= DURACION_TRANSICION) {

                tiempoTransicion = DURACION_TRANSICION;
                entrando = false;
            }
        }

        // -----------------------------------------
        // SALIDA DE LA COCINA
        // -----------------------------------------

        if (saliendo) {

            tiempoTransicion += delta;

            if (tiempoTransicion >= DURACION_TRANSICION) {

                // Terminó el fundido a negro.
                // Volvemos a la pantalla de la cantina.

                juego.setScreen(pantallaCantina);
                return;
            }
        }

        // -----------------------------------------
        // DIBUJAR COCINA
        // -----------------------------------------

        camara.update();

        juego.batch.setProjectionMatrix(
            camara.combined
        );

        juego.batch.begin();

        juego.batch.draw(
            fondoCocina,
            0,
            0,
            ANCHO_VIRTUAL,
            ALTO_VIRTUAL
        );

        juego.batch.end();

        // -----------------------------------------
        // VOLVER A LA CANTINA
        // -----------------------------------------

        if (!entrando &&
            !saliendo &&
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {

            saliendo = true;
            tiempoTransicion = 0;
        }

        // -----------------------------------------
        // DIBUJAR TRANSICIÓN
        // -----------------------------------------

        if (entrando) {

            float alpha =
                1f -
                    (tiempoTransicion / DURACION_TRANSICION);

            dibujarTransicion(alpha);
        }

        if (saliendo) {

            float alpha =
                tiempoTransicion / DURACION_TRANSICION;

            if (alpha > 1f) {
                alpha = 1f;
            }

            dibujarTransicion(alpha);
        }
    }

    private void dibujarTransicion(float alpha) {

        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.setProjectionMatrix(
            camara.combined
        );

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        shapeRenderer.setColor(
            0,
            0,
            0,
            alpha
        );

        shapeRenderer.rect(
            0,
            0,
            ANCHO_VIRTUAL,
            ALTO_VIRTUAL
        );

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height) {

        vista.update(width, height);

        camara.position.set(
            ANCHO_VIRTUAL / 2f,
            ALTO_VIRTUAL / 2f,
            0
        );
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

        if (fondoCocina != null)
            fondoCocina.dispose();

        if (shapeRenderer != null)
            shapeRenderer.dispose();
    }
}
