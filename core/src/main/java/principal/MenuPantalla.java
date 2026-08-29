package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuPantalla implements Screen {

    private final Main juego;
    private OrthographicCamera camara;
    private Viewport vista;

    // Recurso gráfico de la imagen del menú
    private Texture menuImagen;

    // Resolución de referencia virtual para mantener la proporción de pantalla
    private static final float ANCHO_VIRTUAL = 1280;
    private static final float ALTO_VIRTUAL = 720;

    public MenuPantalla(Main juego) {
        this.juego = juego;

        // Configuración del sistema de vista y cámara
        camara = new OrthographicCamera();
        vista = new FitViewport(ANCHO_VIRTUAL, ALTO_VIRTUAL, camara);
        vista.apply();
        camara.position.set(ANCHO_VIRTUAL / 2f, ALTO_VIRTUAL / 2f, 0);

        // Carga la textura desde la carpeta assets (assets/MENUS/MENU.jpeg)
        menuImagen = new Texture(Gdx.files.internal("MENUS/MENU.jpeg"));
    }

    @Override
    public void render(float delta) {
        // Limpiar el buffer de pantalla con fondo negro
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar la cámara y sincronizar el batch del juego
        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);

        // Dibujar el menú
        juego.batch.begin();
        juego.batch.draw(menuImagen, 0, 0, ANCHO_VIRTUAL, ALTO_VIRTUAL);
        juego.batch.end();
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
        // Se destruye la textura para liberar espacio en la GPU cuando la pantalla ya no exista
        if (menuImagen != null) {
            menuImagen.dispose();
        }
    }
}
