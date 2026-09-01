package dialogos;

import java.util.ArrayList;
import java.util.List;

public class NodoDialogo {
    private int id;
    private String textoPersonaje;
    private List<OpcionDialogo> opciones;

    public NodoDialogo(int id, String textoPersonaje) {
        this.id = id;
        this.textoPersonaje = textoPersonaje;
        this.opciones = new ArrayList<>();
    }

    public void agregarOpcion(String texto, int siguienteNodoId) {
        if (opciones.size() < 3) {
            opciones.add(new OpcionDialogo(texto, siguienteNodoId));
        }
    }

    public int getId() { return id; }
    public String getTextoPersonaje() { return textoPersonaje; }
    public List<OpcionDialogo> getOpciones() { return opciones; }
}
