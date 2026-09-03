package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Cocina implements Screen {

    private final Main juego;
    private final JuegoPantalla pantallaCantina;
    private final ManejadorCocina cocinaManager;

    private OrthographicCamera camara;
    private Viewport vista;

    private Texture fondoCocina;
    private Texture panVacio;
    private Texture panSalchicha;
    private Texture salchichaQuemada;

    private ShapeRenderer shapeRenderer;

    private boolean entrando = true;
    private boolean saliendo = false;
    private float tiempoTransicion = 0;

    private static final float DURACION_TRANSICION = 0.5f;
    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    private static final float MESADA_X = 50;
    private static final float MESADA_Y = 50;
    private static final float MESADA_ANCHO = 350;
    private static final float MESADA_ALTO = 300;

    private static final float POS_PAN_X = 120;
    private static final float POS_PAN_Y = 160;
    private static final float ANCHO_PAN = 200;
    private static final float ALTO_PAN = 100;

    private static final float PLANCHA_X = 500;
    private static final float PLANCHA_Y = 180;
    private static final float PLANCHA_ANCHO = 400;
    private static final float PLANCHA_ALTO = 250;

    public Cocina(Main juego, JuegoPantalla pantallaCantina, ManejadorCocina cocinaManager) {
        this.juego = juego;
        this.pantallaCantina = pantallaCantina;
        this.cocinaManager = cocinaManager;

        camara = new OrthographicCamera();
        vista = new FitViewport(ANCHO_VIRTUAL, ALTO_VIRTUAL, camara);
        vista.apply();
        camara.position.set(ANCHO_VIRTUAL / 2f, ALTO_VIRTUAL / 2f, 0);

        fondoCocina = new Texture(Gdx.files.internal("MENUS/COCINFULL.png"));
        panVacio = new Texture(Gdx.files.internal("COCINA/PAN_VACIO.png"));
        panSalchicha = new Texture(Gdx.files.internal("COCINA/PAN_SALCHICHA.png"));
        salchichaQuemada = new Texture(Gdx.files.internal("COCINA/SALCHICHA_QUEMADA.png"));

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        cocinaManager.actualizar(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (entrando) {
            tiempoTransicion += delta;
            if (tiempoTransicion >= DURACION_TRANSICION) {
                tiempoTransicion = DURACION_TRANSICION;
                entrando = false;
            }
        }

        if (saliendo) {
            tiempoTransicion += delta;
            if (tiempoTransicion >= DURACION_TRANSICION) {
                juego.setScreen(pantallaCantina);
                return;
            }
        }

        if (!entrando && !saliendo && Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            vista.unproject(touchPos);

            float x = touchPos.x;
            float y = touchPos.y;

            if (x >= MESADA_X && x <= MESADA_X + MESADA_ANCHO &&
                y >= MESADA_Y && y <= MESADA_Y + MESADA_ALTO) {
                cocinaManager.tocarMesada();
            }

            if (x >= PLANCHA_X && x <= PLANCHA_X + PLANCHA_ANCHO &&
                y >= PLANCHA_Y && y <= PLANCHA_Y + PLANCHA_ALTO) {
                cocinaManager.tocarPlancha();
            }
        }

        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);
        juego.batch.begin();

        juego.batch.draw(fondoCocina, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);

        if (cocinaManager.isPanchoCompletoEnMesada()) {
            juego.batch.draw(panSalchicha, POS_PAN_X, POS_PAN_Y, ANCHO_PAN, ALTO_PAN);
        } else if (cocinaManager.isTienePanEnMesada()) {
            juego.batch.draw(panVacio, POS_PAN_X, POS_PAN_Y, ANCHO_PAN, ALTO_PAN);
        }

        switch (cocinaManager.getEstadoSalchicha()) {
            case COCINANDO:
            case LISTO:
                juego.batch.draw(panSalchicha, PLANCHA_X + 80, PLANCHA_Y + 30, 200, 100);
                break;
            case QUEMADO:
                juego.batch.draw(salchichaQuemada, PLANCHA_X + 80, PLANCHA_Y + 30, 200, 100);
                break;
            case VACIO:
                break;
        }

        juego.batch.end();

        dibujarBarraCoccion();

        if (!entrando && !saliendo && Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            saliendo = true;
            tiempoTransicion = 0;
        }

        if (entrando) {
            float alpha = 1f - (tiempoTransicion / DURACION_TRANSICION);
            dibujarTransicion(alpha);
        }

        if (saliendo) {
            float alpha = tiempoTransicion / DURACION_TRANSICION;
            if (alpha > 1f) alpha = 1f;
            dibujarTransicion(alpha);
        }
    }

    private void dibujarBarraCoccion() {
        if (cocinaManager.getEstadoSalchicha() == ManejadorCocina.EstadoSalchicha.VACIO) return;

        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barraAnchoTotal = 200f;
        float barraX = PLANCHA_X + 80;
        float barraY = PLANCHA_Y + 140;

        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(barraX, barraY, barraAnchoTotal, 16);

        float progreso = Math.min(1.0f, cocinaManager.getTiempoCoccion() / cocinaManager.getTiempoQuemado());

        if (cocinaManager.getEstadoSalchicha() == ManejadorCocina.EstadoSalchicha.COCINANDO) {
            shapeRenderer.setColor(1f, 0.8f, 0.2f, 1f);
        } else if (cocinaManager.getEstadoSalchicha() == ManejadorCocina.EstadoSalchicha.LISTO) {
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1f);
        } else {
            shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1f);
        }

        shapeRenderer.rect(barraX + 2, barraY + 2, (barraAnchoTotal - 4) * progreso, 12);
        shapeRenderer.end();
    }

    private void dibujarTransicion(float alpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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
        if (fondoCocina != null) fondoCocina.dispose();
        if (panVacio != null) panVacio.dispose();
        if (panSalchicha != null) panSalchicha.dispose();
        if (salchichaQuemada != null) salchichaQuemada.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
