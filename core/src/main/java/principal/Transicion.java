package principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Transicion {

    private ShapeRenderer shapeRenderer;
    private float tiempo;
    private boolean activa;

    private static final float DURACION = 0.5f;

    public Transicion() {
        shapeRenderer = new ShapeRenderer();
        tiempo = 0;
        activa = false;
    }

    public void iniciar() {
        tiempo = 0;
        activa = true;
    }

    public boolean actualizar(float delta) {

        if (!activa) {
            return false;
        }

        tiempo += delta;

        if (tiempo >= DURACION) {
            activa = false;
            return true;
        }

        return false;
    }

    public void dibujar() {

        if (!activa) {
            return;
        }

        float alpha = tiempo / DURACION;

        Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);

        shapeRenderer.rect(
            0,
            0,
            1280,
            720
        );

        shapeRenderer.end();

        Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
    }

    public boolean estaActiva() {
        return activa;
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
