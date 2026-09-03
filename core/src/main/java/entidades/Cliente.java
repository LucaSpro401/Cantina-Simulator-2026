package entidades;

import com.badlogic.gdx.graphics.Texture;
import dialogos.ArbolDialogo;
import java.util.ArrayList;
import java.util.List;

public abstract class Cliente {
    protected String nombre;
    protected Texture textura;
    protected List<Producto> pedido;
    protected ArbolDialogo dialogo;

    public Cliente(String nombre, String rutaTextura) {
        this.nombre = nombre;
        this.textura = new Texture(rutaTextura);
        this.pedido = new ArrayList<>();
        this.dialogo = new ArbolDialogo();

        generarPedidoAleatorio();
        configurarDialogos(); // Método abstracto que implementa cada hijo
    }

    protected void generarPedidoAleatorio() {
        Producto pancho = new Producto("Pancho", 150.0, true);
        Producto coca = new Producto("Coca-Cola", 100.0, false);

        int tipoPedido = (int) (Math.random() * 3);

        switch (tipoPedido) {
            case 0:
                pedido.add(pancho);
                break;
            case 1:
                pedido.add(coca);
                break;
            case 2:
                pedido.add(pancho);
                pedido.add(coca);
                break;
        }
    }

    // Cada personaje hijo estará obligado a definir sus propios diálogos acá
    protected abstract void configurarDialogos();

    public String getNombre() { return nombre; }
    public Texture getTextura() { return textura; }
    public List<Producto> getPedido() { return pedido; }
    public ArbolDialogo getDialogo() { return dialogo; }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
