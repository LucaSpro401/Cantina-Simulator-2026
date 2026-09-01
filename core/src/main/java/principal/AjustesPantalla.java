package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AjustesPantalla implements Screen {

    private final Main juego;
    private OrthographicCamera camara;
    private Viewport vista;
    private Texture ajustesImagen;

    private static int volumen = 100;
    private static boolean pantallaCompleta = false;

    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    public AjustesPantalla(Main juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        vista = new FitViewport(ANCHO_VIRTUAL, ALTO_VIRTUAL, camara);
        vista.apply();
        camara.position.set(ANCHO_VIRTUAL / 2f, ALTO_VIRTUAL / 2f, 0);

        ajustesImagen = new Texture(Gdx.files.internal("MENUS/ajustess.jpg"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);

        juego.batch.begin();
        juego.batch.draw(ajustesImagen, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);
        juego.batch.end();

        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();

            // BOTÓN BAJAR VOLUMEN
            if (screenX >= 603 && screenX <= 646 && screenY >= 411 && screenY <= 447) {
                if (volumen > 0) volumen -= 10;
                System.out.println("VOLUMEN: " + volumen + "%");
            }

            // BOTÓN SUBIR VOLUMEN
            if (screenX >= 707 && screenX <= 748 && screenY >= 411 && screenY <= 446) {
                if (volumen < 100) volumen += 10;
                System.out.println("VOLUMEN: " + volumen + "%");
            }

            // BOTÓN PANTALLA COMPLETA
            if (screenX >= 602 && screenX <= 753 && screenY >= 485 && screenY <= 580) {
                if (pantallaCompleta) {
                    Gdx.graphics.setWindowedMode(1280, 720);
                    pantallaCompleta = false;
                    System.out.println("MODO VENTANA");
                } else {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    pantallaCompleta = true;
                    System.out.println("PANTALLA COMPLETA");
                }
            }

            // BOTÓN VOLVER
            if (screenX >= 601 && screenX <= 750 && screenY >= 606 && screenY <= 706) {
                System.out.println("APRETASTE VOLVER");
                juego.setScreen(new MenuPantalla(juego));
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
        if (ajustesImagen != null) ajustesImagen.dispose();
    }
}
