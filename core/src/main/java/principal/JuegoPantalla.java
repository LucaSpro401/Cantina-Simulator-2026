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
import entidades.Steven;
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
    private Texture mostrador;
    private Texture texturaCoca;
    private Cliente cliente;

    private ManejadorCocina cocinaManager;

    private boolean dialogoActivo = false;
    private boolean dialogoTerminado = false;

    private ShapeRenderer shapeRenderer;
    private BitmapFont fuente;

    private float posX = -350;
    private float posY = 60;
    private float destinoX = 200;
    private float velocidad = 250;
    private float tiempoCaminado = 0;
    private boolean llegoAlMostrador = false;

    private float anchoPersonaje = 480;
    private float altoPersonaje = 510;

    private boolean transicionando = false;
    private float tiempoTransicion = 0;
    private static final float DURACION_TRANSICION = 0.5f;

    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    private boolean mostrandoRespuestaEntrega = false;
    private String textoRespuestaEntrega = "";

    // Coordenadas ajustadas para el estante medio de la heladera
    private float cocaX = 1205;
    private float cocaY = 275;
    private float cocaAncho = 26;
    private float cocaAlto = 48;

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

        mostrador = new Texture(
            Gdx.files.internal("MENUS/MOSTRADOR.png")
        );

        texturaCoca = new Texture(
            Gdx.files.internal("MENUS/COCA.png")
        );

        this.cliente = new Steven();

        shapeRenderer = new ShapeRenderer();

        fuente = new BitmapFont();
        fuente.getData().setScale(1.5f);

        this.cocinaManager = new ManejadorCocina();
    }

    @Override
    public void render(float delta) {

        cocinaManager.actualizar(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (transicionando) {

            tiempoTransicion += delta;

            if (tiempoTransicion >= DURACION_TRANSICION) {
                juego.setScreen(new Cocina(juego, this, cocinaManager));
                return;
            }
        }

        actualizarMovimiento(delta);

        if (llegoAlMostrador && !dialogoActivo && !dialogoTerminado && !mostrandoRespuestaEntrega) {
            dialogoActivo = true;
        }

        if (Gdx.input.justTouched()) {
            manejarEntregaPedido();
            manejarVentaCoca();
            if (!mostrandoRespuestaEntrega && dialogoActivo) {
                manejarClickDialogo();
            }
        }

        if (mostrandoRespuestaEntrega) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                mostrandoRespuestaEntrega = false;
            }
        } else if (dialogoActivo) {
            NodoDialogo nodoActual = cliente.getDialogo().getNodoActual();
            if (nodoActual.getOpciones().isEmpty() && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                dialogoActivo = false;
                dialogoTerminado = true;
            }
        }

        if (!transicionando && Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {

            transicionando = true;
            tiempoTransicion = 0;
        }

        camara.update();

        if (juego.batch != null) {

            juego.batch.setProjectionMatrix(camara.combined);

            juego.batch.begin();

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

            // 1. Fondo de la cantina (incluye la heladera de la derecha)
            juego.batch.draw(
                fondoCantina,
                0,
                0,
                ANCHO_VIRTUAL,
                ALTO_VIRTUAL
            );

            juego.batch.setColor(1f, 1f, 1f, 1f);

            float anguloInclinacion = 0;

            if (!llegoAlMostrador) {
                anguloInclinacion =
                    (float) Math.sin(tiempoCaminado * 12) * 10f;
            }

            Texture texturaCliente = cliente.getTextura();

            // 2. Cliente
            juego.batch.draw(
                texturaCliente,
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
                texturaCliente.getWidth(),
                texturaCliente.getHeight(),
                false,
                false
            );

            // 3. Mostrador de madera
            juego.batch.draw(
                mostrador,
                0,
                0,
                ANCHO_VIRTUAL,
                ALTO_VIRTUAL
            );

            // 4. Lata de Coca dibujada dentro de la heladera
            juego.batch.draw(
                texturaCoca,
                cocaX,
                cocaY,
                cocaAncho,
                cocaAlto
            );

            juego.batch.end();

            if (mostrandoRespuestaEntrega) {
                dibujarRespuestaEntrega();
            } else if (dialogoActivo) {
                dibujarDialogo();
            }

            dibujarBarraCoccionHUD();

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

    private void manejarEntregaPedido() {
        if (!llegoAlMostrador) {
            return;
        }

        Vector2 posicion = new Vector2(
            Gdx.input.getX(),
            Gdx.input.getY()
        );

        vista.unproject(posicion);

        float x = posicion.x;
        float y = posicion.y;

        if (x >= posX && x <= posX + anchoPersonaje &&
            y >= posY && y <= posY + altoPersonaje) {

            if (cocinaManager.entregarPanchoACliente()) {
                textoRespuestaEntrega = "Gracias por el pancho. Sos bastante eficiente.";
                mostrandoRespuestaEntrega = true;
                dialogoActivo = false;
                dialogoTerminado = true;
            }
        }
    }

    private void manejarVentaCoca() {
        if (!llegoAlMostrador) {
            return;
        }

        Vector2 posicion = new Vector2(
            Gdx.input.getX(),
            Gdx.input.getY()
        );

        vista.unproject(posicion);

        float x = posicion.x;
        float y = posicion.y;

        if (x >= cocaX && x <= cocaX + cocaAncho &&
            y >= cocaY && y <= cocaY + cocaAlto) {

            textoRespuestaEntrega = "¡Espectacular! Nada mejor que una Coca bien fría.";
            mostrandoRespuestaEntrega = true;
            dialogoActivo = false;
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
            cliente.getNombre() + ":",
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

    private void dibujarRespuestaEntrega() {

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
            cliente.getNombre() + ":",
            120,
            250
        );

        fuente.draw(
            juego.batch,
            textoRespuestaEntrega,
            120,
            210
        );

        juego.batch.end();
    }

    private void dibujarBarraCoccionHUD() {
        if (cocinaManager.getEstadoSalchicha() == ManejadorCocina.EstadoSalchicha.VACIO) return;

        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barraAncho = 180f;
        float barraX = 20f;
        float barraY = 720f - 40f;

        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(barraX, barraY, barraAncho, 16);

        float progreso = Math.min(1.0f, cocinaManager.getTiempoCoccion() / cocinaManager.getTiempoQuemado());

        if (cocinaManager.getEstadoSalchicha() == ManejadorCocina.EstadoSalchicha.COCINANDO) {
            shapeRenderer.setColor(1f, 0.8f, 0.2f, 1f);
        } else if (cocinaManager.getEstadoSalchicha() == ManejadorCocina.EstadoSalchicha.LISTO) {
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1f);
        } else {
            shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1f);
        }

        shapeRenderer.rect(barraX + 2, barraY + 2, (barraAncho - 4) * progreso, 12);
        shapeRenderer.end();
    }

    private void manejarClickDialogo() {

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

        if (cliente != null)
            cliente.dispose();

        if (mostrador != null)
            mostrador.dispose();

        if (texturaCoca != null)
            texturaCoca.dispose();

        if (shapeRenderer != null)
            shapeRenderer.dispose();

        if (fuente != null)
            fuente.dispose();
    }
}
