package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuPantalla implements Screen {

    private final Main juego;
    private OrthographicCamera camara;
    private Viewport vista;
    private Texture menuImagen;

    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    public MenuPantalla(Main juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        vista = new FitViewport(ANCHO_VIRTUAL, ALTO_VIRTUAL, camara);
        vista.apply();
        camara.position.set(ANCHO_VIRTUAL / 2f, ALTO_VIRTUAL / 2f, 0);

        menuImagen = new Texture(Gdx.files.internal("MENUS/MENU.jpg"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);

        juego.batch.begin();
        juego.batch.draw(menuImagen, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);
        juego.batch.end();

        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();

            // BOTÓN JUGAR -> Pasa a DiaPantalla
            if (screenX >= 558 && screenX <= 777 && screenY >= 322 && screenY <= 362) {
                System.out.println("APRETASTE JUGAR");
                juego.setScreen(new DiaPantalla(juego));
            }

            // BOTÓN AJUSTES -> Pasa a AjustesPantalla
            if (screenX >= 560 && screenX <= 776 && screenY >= 386 && screenY <= 424) {
                System.out.println("APRETASTE AJUSTES");
                juego.setScreen(new AjustesPantalla(juego));
            }

            // BOTÓN SALIR -> Cierra la aplicación
            if (screenX >= 557 && screenX <= 779 && screenY >= 444 && screenY <= 486) {
                System.out.println("APRETASTE SALIR");
                Gdx.app.exit();
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
        if (menuImagen != null) menuImagen.dispose();
    }
}
