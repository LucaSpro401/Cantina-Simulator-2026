package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import dialogos.OpcionDialogo;

public class JuegoPantalla implements Screen {

    private final Main juego;
    private OrthographicCamera camara;
    private Viewport vista;

    private Texture fondoCantina;
    private Texture personajeSteven;
    private Texture mostrador;
    private Cliente cliente;

    private boolean dialogoActivo = false;

    private ShapeRenderer shapeRenderer;
    private BitmapFont fuente;

    // Movimiento y posición del torso
    private float posX = -350;
    private float posY = 60;            // Apoya justo sobre la barra del mostrador
    private float destinoX = 200;        // Se frena frente al jugador
    private float velocidad = 250;
    private float tiempoCaminado = 0;
    private boolean llegoAlMostrador = false;

    // Dimensiones del sprite recortado
    private float anchoPersonaje = 480;
    private float altoPersonaje = 510;

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

        mostrador = new Texture(Gdx.files.internal("MENUS/MOSTRADOR.png"));

        cliente = new Cliente("Steven", "PERSONAJES/STEVEN SEAGAL.png");

        shapeRenderer = new ShapeRenderer();
        fuente = new BitmapFont();
        fuente.getData().setScale(1.5f);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        actualizarMovimiento(delta);

        if (llegoAlMostrador && !dialogoActivo) {
            dialogoActivo = true;
        }
        manejarClickDialogo();

        camara.update();

        if (juego.batch != null) {
            juego.batch.setProjectionMatrix(camara.combined);

            juego.batch.begin();

            // 1. Dibujar el fondo
            if (llegoAlMostrador) {
                juego.batch.setColor(0.65f, 0.65f, 0.65f, 1f);
            } else {
                juego.batch.setColor(1f, 1f, 1f, 1f);
            }

            juego.batch.draw(fondoCantina, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);

// Volvemos al color normal para el resto de los elementos
            juego.batch.setColor(1f, 1f, 1f, 1f);

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
            // 4. Dibujar el mostrador por encima de Steven
            juego.batch.draw(mostrador, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);

            juego.batch.end();

            if (dialogoActivo) {
                dibujarDialogo();
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

        NodoDialogo nodo = cliente.getDialogo().getNodoActual();

        // Dibujar la caja del diálogo
        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(new Color(0, 0, 0, 0.85f));

        shapeRenderer.rect(
            80,       // X
            40,       // Y
            1120,     // ancho
            250       // alto
        );

        shapeRenderer.end();

        // Dibujar el texto
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

        // Dibujar las opciones
        float posicionY = 155;

        for (int i = 0; i < nodo.getOpciones().size(); i++) {

            OpcionDialogo opcion = nodo.getOpciones().get(i);

            fuente.draw(
                juego.batch,
                (i + 1) + ". " + opcion.getTextoOpcion(),
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

        // Convertimos la posición del mouse/pantalla
        // a las coordenadas virtuales del juego
        Vector2 posicion = new Vector2(
            Gdx.input.getX(),
            Gdx.input.getY()
        );

        vista.unproject(posicion);

        float x = posicion.x;
        float y = posicion.y;

        NodoDialogo nodo = cliente.getDialogo().getNodoActual();

        // Posición inicial de las opciones
        float posicionY = 155;

        for (int i = 0; i < nodo.getOpciones().size(); i++) {

            // Cada opción ocupa aproximadamente 45 píxeles de alto
            if (x >= 120 && x <= 1150 &&
                y >= posicionY - 35 && y <= posicionY + 15) {

                cliente.getDialogo().seleccionarOpcion(i);

                return;
            }

            posicionY -= 55;
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
        if (mostrador != null) mostrador.dispose();
    }
}
