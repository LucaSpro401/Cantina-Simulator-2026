package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entidades.Cliente;
import dialogos.NodoDialogo;
import dialogos.OpcionDialogo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class JuegoPantalla implements Screen {

    private final Main juego;
    private OrthographicCamera camara;
    private Viewport vista;

    private Texture fondoCantina;
    private Texture personajeSteven;
    private Texture mostrador;
    private Cliente cliente;

    private boolean dialogoActivo = false;
    private boolean dialogoTerminado = false;

    private ShapeRenderer shapeRenderer;
    private BitmapFont fuente;

    // Movimiento y posición del torso
    private float posX = -350;
    private float posY = 60;
    private float destinoX = 200;
    private float velocidad = 250;
    private float tiempoCaminado = 0;
    private boolean llegoAlMostrador = false;

    // Dimensiones del sprite recortado
    private float anchoPersonaje = 480;
    private float altoPersonaje = 510;

    // Transición
    private boolean transicionando = false;
    private float tiempoTransicion = 0;
    private static final float DURACION_TRANSICION = 0.5f;

    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    public JuegoPantalla(Main juego) {

        this.juego = juego;

        camara = new OrthographicCamera();
        vista = new FitViewport(ANCHO_VIRTUAL, ALTO_VIRTUAL, camara);
        vista.apply();

        camara.position.set(
            ANCHO_VIRTUAL / 2f,
            ALTO_VIRTUAL / 2f,
            0
        );

        fondoCantina = new Texture(
            Gdx.files.internal("MENUS/CANTINA DEFI.png")
        );

        personajeSteven = new Texture(
            Gdx.files.internal("PERSONAJES/STEVEN SEAGAL.png")
        );

        mostrador = new Texture(
            Gdx.files.internal("MENUS/MOSTRADOR.png")
        );

        cliente = new Cliente(
            "Steven",
            "PERSONAJES/STEVEN SEAGAL.png"
        );

        shapeRenderer = new ShapeRenderer();

        fuente = new BitmapFont();
        fuente.getData().setScale(1.5f);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // -----------------------------------------
        // TRANSICIÓN HACIA LA COCINA
        // -----------------------------------------

        if (transicionando) {

            tiempoTransicion += delta;

            if (tiempoTransicion >= DURACION_TRANSICION) {
                juego.setScreen(new Cocina(juego, this));
                return;
            }
        }

        actualizarMovimiento(delta);

        if (llegoAlMostrador && !dialogoActivo && !dialogoTerminado) {
            dialogoActivo = true;
        }

        manejarClickDialogo();

        // Final del diálogo
        if (dialogoActivo &&
            cliente.getDialogo().getNodoActual().getOpciones().isEmpty()) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

                dialogoActivo = false;
                dialogoTerminado = true;
            }
        }

        // Ir hacia la cocina
        if (!dialogoActivo &&
            dialogoTerminado &&
            !transicionando &&
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {

            transicionando = true;
            tiempoTransicion = 0;
        }

        camara.update();

        if (juego.batch != null) {

            juego.batch.setProjectionMatrix(camara.combined);

            juego.batch.begin();

            // -----------------------------------------
            // FONDO
            // -----------------------------------------

            if (llegoAlMostrador) {
                juego.batch.setColor(
                    0.65f,
                    0.65f,
                    0.65f,
                    1f
                );
            } else {
                juego.batch.setColor(
                    1f,
                    1f,
                    1f,
                    1f
                );
            }

            juego.batch.draw(
                fondoCantina,
                0,
                0,
                ANCHO_VIRTUAL,
                ALTO_VIRTUAL
            );

            juego.batch.setColor(1f, 1f, 1f, 1f);

            // -----------------------------------------
            // STEVEN
            // -----------------------------------------

            float anguloInclinacion = 0;

            if (!llegoAlMostrador) {
                anguloInclinacion =
                    (float) Math.sin(tiempoCaminado * 12) * 10f;
            }

            juego.batch.draw(
                personajeSteven,
                posX,
                posY,
                anchoPersonaje / 2f,
                0,
                anchoPersonaje,
                altoPersonaje,
                1f,
                1f,
                anguloInclinacion,
                0,
                0,
                personajeSteven.getWidth(),
                personajeSteven.getHeight(),
                false,
                false
            );

            // -----------------------------------------
            // MOSTRADOR
            // -----------------------------------------

            juego.batch.draw(
                mostrador,
                0,
                0,
                ANCHO_VIRTUAL,
                ALTO_VIRTUAL
            );

            juego.batch.end();

            // -----------------------------------------
            // DIÁLOGO
            // -----------------------------------------

            if (dialogoActivo) {
                dibujarDialogo();
            }

            // -----------------------------------------
            // TRANSICIÓN NEGRA
            // -----------------------------------------

            if (transicionando) {

                float alpha =
                    tiempoTransicion / DURACION_TRANSICION;

                if (alpha > 1f) {
                    alpha = 1f;
                }

                dibujarTransicion(alpha);
            }
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

    private void dibujarDialogo() {

        NodoDialogo nodo =
            cliente.getDialogo().getNodoActual();

        shapeRenderer.setProjectionMatrix(camara.combined);

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        shapeRenderer.setColor(
            new Color(0, 0, 0, 0.85f)
        );

        shapeRenderer.rect(
            80,
            40,
            1120,
            250
        );

        shapeRenderer.end();

        juego.batch.begin();

        fuente.setColor(Color.WHITE);

        fuente.draw(
            juego.batch,
            "Steven:",
            120,
            250
        );

        fuente.draw(
            juego.batch,
            nodo.getTextoPersonaje(),
            120,
            210
        );

        float posicionY = 155;

        for (int i = 0;
             i < nodo.getOpciones().size();
             i++) {

            OpcionDialogo opcion =
                nodo.getOpciones().get(i);

            fuente.draw(
                juego.batch,
                (i + 1) + ". " +
                    opcion.getTextoOpcion(),
                140,
                posicionY
            );

            posicionY -= 55;
        }

        juego.batch.end();
    }

    private void manejarClickDialogo() {

        if (!dialogoActivo) {
            return;
        }

        if (!Gdx.input.justTouched()) {
            return;
        }

        Vector2 posicion = new Vector2(
            Gdx.input.getX(),
            Gdx.input.getY()
        );

        vista.unproject(posicion);

        float x = posicion.x;
        float y = posicion.y;

        NodoDialogo nodo =
            cliente.getDialogo().getNodoActual();

        float posicionY = 155;

        for (int i = 0;
             i < nodo.getOpciones().size();
             i++) {

            if (x >= 120 &&
                x <= 1150 &&
                y >= posicionY - 35 &&
                y <= posicionY + 15) {

                cliente.getDialogo()
                    .seleccionarOpcion(i);

                return;
            }

            posicionY -= 55;
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
    public void show() {
        transicionando = false;
        tiempoTransicion = 0;
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {

        if (fondoCantina != null)
            fondoCantina.dispose();

        if (personajeSteven != null)
            personajeSteven.dispose();

        if (mostrador != null)
            mostrador.dispose();

        if (shapeRenderer != null)
            shapeRenderer.dispose();

        if (fuente != null)
            fuente.dispose();
    }
}
