package principal;

public class ManejadorCocina {

    public enum EstadoSalchicha {
        VACIO, COCINANDO, LISTO, QUEMADO
    }

    private boolean tienePanEnMesada = true;
    private EstadoSalchicha estadoSalchicha = EstadoSalchicha.VACIO;
    private float tiempoCoccion = 0;
    private boolean panchoCompletoEnMesada = false;
    private boolean tienePanchoEnMano = false;

    private static final float TIEMPO_LISTO = 4.0f;
    private static final float TIEMPO_QUEMADO = 8.0f;

    public void actualizar(float delta) {
        if (estadoSalchicha == EstadoSalchicha.COCINANDO || estadoSalchicha == EstadoSalchicha.LISTO) {
            tiempoCoccion += delta;
            if (tiempoCoccion >= TIEMPO_QUEMADO) {
                estadoSalchicha = EstadoSalchicha.QUEMADO;
            } else if (tiempoCoccion >= TIEMPO_LISTO) {
                estadoSalchicha = EstadoSalchicha.LISTO;
            }
        }
    }

    public void tocarMesada() {
        if (panchoCompletoEnMesada) {
            panchoCompletoEnMesada = false;
            tienePanchoEnMano = true;
            tienePanEnMesada = true;
        } else if (!tienePanEnMesada) {
            tienePanEnMesada = true;
        }
    }

    public void tocarPlancha() {
        if (estadoSalchicha == EstadoSalchicha.VACIO) {
            estadoSalchicha = EstadoSalchicha.COCINANDO;
            tiempoCoccion = 0;
        } else if (estadoSalchicha == EstadoSalchicha.LISTO) {
            if (tienePanEnMesada) {
                tienePanEnMesada = false;
                panchoCompletoEnMesada = true;
                estadoSalchicha = EstadoSalchicha.VACIO;
                tiempoCoccion = 0;
            }
        } else if (estadoSalchicha == EstadoSalchicha.QUEMADO) {
            estadoSalchicha = EstadoSalchicha.VACIO;
            tiempoCoccion = 0;
        }
    }

    public boolean entregarPanchoACliente() {
        if (tienePanchoEnMano) {
            tienePanchoEnMano = false;
            return true;
        }
        return false;
    }

    public boolean isTienePanEnMesada() { return tienePanEnMesada; }
    public boolean isPanchoCompletoEnMesada() { return panchoCompletoEnMesada; }
    public boolean isTienePanchoEnMano() { return tienePanchoEnMano; }
    public EstadoSalchicha getEstadoSalchicha() { return estadoSalchicha; }
    public float getTiempoCoccion() { return tiempoCoccion; }
    public float getTiempoQuemado() { return TIEMPO_QUEMADO; }
}
