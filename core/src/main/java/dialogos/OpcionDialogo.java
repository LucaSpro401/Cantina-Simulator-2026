package dialogos;

public class OpcionDialogo {
    private String textoOpcion;
    private int siguienteNodoId;

    public OpcionDialogo(String textoOpcion, int siguienteNodoId) {
        this.textoOpcion = textoOpcion;
        this.siguienteNodoId = siguienteNodoId;
    }

    public String getTextoOpcion() { return textoOpcion; }
    public int getSiguienteNodoId() { return siguienteNodoId; }
}
