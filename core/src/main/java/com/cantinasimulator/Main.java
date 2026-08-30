package com.cantinasimulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture menu;
    private Texture juego;
    private Texture ajustes;

    private boolean enJuego = false;
    private boolean enAjustes = false;

    private int volumen = 100;
    private boolean pantallaCompleta = false;

    @Override
    public void create() {

        // Creamos el SpriteBatch
        batch = new SpriteBatch();

        // Cargamos las imágenes
        menu = new Texture("MENUS/MENU.jpg");
        juego = new Texture("MENUS/CANTINA DEFI.png");
        ajustes = new Texture("MENUS/ajustess.jpg");

        // Detectamos los clics
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                System.out.println("HICISTE CLICK");
                System.out.println("X: " + screenX + " Y: " + screenY);


                // =========================
                // PANTALLA DE AJUSTES
                // =========================

                if (enAjustes) {

                    // BOTÓN BAJAR VOLUMEN
                    if (screenX >= 603 && screenX <= 646 &&
                        screenY >= 411 && screenY <= 447) {

                        if (volumen > 0) {
                            volumen -= 10;
                        }

                        System.out.println("VOLUMEN: " + volumen + "%");
                    }


                    // BOTÓN SUBIR VOLUMEN
                    if (screenX >= 707 && screenX <= 748 &&
                        screenY >= 411 && screenY <= 446) {

                        if (volumen < 100) {
                            volumen += 10;
                        }

                        System.out.println("VOLUMEN: " + volumen + "%");
                    }


                    // BOTÓN PANTALLA COMPLETA
                    if (screenX >= 602 && screenX <= 753 &&
                        screenY >= 485 && screenY <= 580) {

                        if (pantallaCompleta) {

                            Gdx.graphics.setWindowedMode(1280, 720);
                            pantallaCompleta = false;

                            System.out.println("MODO VENTANA");

                        } else {

                            Gdx.graphics.setFullscreenMode(
                                Gdx.graphics.getDisplayMode()
                            );

                            pantallaCompleta = true;

                            System.out.println("PANTALLA COMPLETA");
                        }
                    }

                    // BOTÓN VOLVER
                    if (screenX >= 601 && screenX <= 750 &&
                        screenY >= 606 && screenY <= 706) {

                        System.out.println("APRETASTE VOLVER");

                        enAjustes = false;
                        enJuego = false;
                    }

                    return true;
                }


                // =========================
                // PANTALLA DEL JUEGO
                // =========================

                if (enJuego) {

                    // Por ahora no tenemos botones en el juego.

                    return true;
                }


                // =========================
                // MENÚ PRINCIPAL
                // =========================

                // BOTÓN JUGAR
                if (screenX >= 558 && screenX <= 777 &&
                    screenY >= 322 && screenY <= 362) {

                    System.out.println("APRETASTE JUGAR");

                    enJuego = true;
                    enAjustes = false;
                }


                // BOTÓN AJUSTES
                if (screenX >= 560 && screenX <= 776 &&
                    screenY >= 386 && screenY <= 424) {

                    System.out.println("APRETASTE AJUSTES");

                    enAjustes = true;
                    enJuego = false;
                }


                // BOTÓN SALIR
                if (screenX >= 557 && screenX <= 779 &&
                    screenY >= 444 && screenY <= 486) {

                    System.out.println("APRETASTE SALIR");

                    Gdx.app.exit();
                }

                return true;
            }
        });

        System.out.println("JUEGO INICIADO");
    }


    @Override
    public void render() {

        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        if (enJuego) {

            batch.draw(
                juego,
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );

        } else if (enAjustes) {

            batch.draw(
                ajustes,
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );

        } else {

            batch.draw(
                menu,
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );
        }

        batch.end();
    }


    @Override
    public void dispose() {

        batch.dispose();
        menu.dispose();
        juego.dispose();
        ajustes.dispose();
    }
}
