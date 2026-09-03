package entidades;

import dialogos.NodoDialogo;

public class Steven extends Cliente {

    public Steven() {
        super("Steven", "PERSONAJES/STEVEN SEAGAL.png");
    }

    @Override
    protected void configurarDialogos() {
        // NODO 3: Conclusiones
        NodoDialogo nFinalPiedra = new NodoDialogo(5, "Tenés razón. Le pongo un cero directo y llamo a los padres.");
        NodoDialogo nFinalComprensivo = new NodoDialogo(6, "Mmm, tenés un punto. Le voy a tomar un recuperatorio oral a ver si recapacita.");
        NodoDialogo nFinalTrabajo = new NodoDialogo(7, "Buena idea. Le voy a mandar a hacer una monografía de 20 páginas para el lunes.");

        // NODO 2: La anécdota del alumno
        NodoDialogo nMachete = new NodoDialogo(4, "Igual te cuento... atrapé a un alumno copiándose en el examen de Lengua. Todavía no lo sancioné, no sé qué castigo darle...");
        nMachete.agregarOpcion("Hay que expulsarlo o ponerle un cero sin dudar.", 5);
        nMachete.agregarOpcion("Hablá con él, capaz tuvo un problema personal y no llegó a estudiar.", 6);
        nMachete.agregarOpcion("Que haga un trabajo práctico integrador bien largo de castigo.", 7);

        // RESPUESTAS INTERMEDIAS A TU PRIMERA SELECCIÓN
        NodoDialogo nRespMarchando = new NodoDialogo(2, "¡Excelente! Me gusta la velocidad. Traeme también una Coca si tenés.");
        nRespMarchando.agregarOpcion("(Avanzar diálogo)", 4);

        NodoDialogo nRespTranquilo = new NodoDialogo(3, "Está bien, no te me alteres... Atendeme tranquilo, pero apurate.");
        nRespTranquilo.agregarOpcion("(Avanzar diálogo)", 4);

        // NODO 1: Saludo inicial
        NodoDialogo nInicial = new NodoDialogo(1, "Buenas. Quiero pedir un pancho bien caliente, por favor.");
        nInicial.agregarOpcion("Sale un pancho marchando.", 2);
        nInicial.agregarOpcion("Pará un poco, recién llego a la barra.", 3);

        dialogo.agregarNodo(nInicial);
        dialogo.agregarNodo(nRespMarchando);
        dialogo.agregarNodo(nRespTranquilo);
        dialogo.agregarNodo(nMachete);
        dialogo.agregarNodo(nFinalPiedra);
        dialogo.agregarNodo(nFinalComprensivo);
        dialogo.agregarNodo(nFinalTrabajo);
    }
}
