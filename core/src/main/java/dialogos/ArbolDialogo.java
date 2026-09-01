package dialogos;

import java.util.HashMap;
import java.util.Map;

public class ArbolDialogo {
    private Map<Integer, NodoDialogo> nodos;
    private NodoDialogo nodoActual;

    public ArbolDialogo() {
        this.nodos = new HashMap<>();
    }

    public void agregarNodo(NodoDialogo nodo) {
        nodos.put(nodo.getId(), nodo);
        if (nodoActual == null) {
            nodoActual = nodo;
        }
    }

    public void seleccionarOpcion(int indiceOpcion) {
        if (nodoActual != null && indiceOpcion >= 0 && indiceOpcion < nodoActual.getOpciones().size()) {
            int siguienteId = nodoActual.getOpciones().get(indiceOpcion).getSiguienteNodoId();
            if (nodos.containsKey(siguienteId)) {
                nodoActual = nodos.get(siguienteId);
            }
        }
    }

    public NodoDialogo getNodoActual() { return nodoActual; }
}
