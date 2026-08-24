package entidades;

public class Jugador {
    private String nombre;
    private double dinero;
    private int hambre;
    private int infracciones;
    private int diaActual;
    private int stockPanchos;

    public Jugador(String nombre, double dineroInicial) {
        this.nombre = nombre;
        this.dinero = dineroInicial;
        this.hambre = 100;
        this.infracciones = 0;
        this.diaActual = 1;
        this.stockPanchos = 5;
    }

    public boolean tieneStock() {
        return stockPanchos > 0;
    }

    public void reponerStock(int cantidad, double costo) {
        if (this.dinero >= costo) {
            this.dinero -= costo;
            this.stockPanchos += cantidad;
        }
    }

    public void descontarStock() {
        if (stockPanchos > 0) {
            stockPanchos--;
        }
    }

    public void consumirComida() {
        this.hambre = Math.min(100, this.hambre + 30);
    }

    public void reducirHambre() {
        this.hambre -= 15;
    }

    public void agregarInfraccion() {
        this.infracciones++;
    }

    public boolean despido() {
        return this.infracciones >= 3;
    }

    public String getNombre() { return nombre; }
    public double getDinero() { return dinero; }
    public void setDinero(double dinero) { this.dinero = dinero; }
    public int getHambre() { return hambre; }
    public int getInfracciones() { return infracciones; }
    public int getDiaActual() { return diaActual; }
    public void setDiaActual(int diaActual) { this.diaActual = diaActual; }
    public int getStockPanchos() { return stockPanchos; }
}
