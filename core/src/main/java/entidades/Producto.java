package entidades;

public class Producto {
    private String nombre;
    private double precioVenta;
    private boolean requiereElaboracion;

    public Producto(String nombre, double precioVenta, boolean requiereElaboracion) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.requiereElaboracion = requiereElaboracion;
    }

    public String getNombre() { return nombre; }
    public double getPrecioVenta() { return precioVenta; }
    public boolean isRequiereElaboracion() { return requiereElaboracion; }
}
