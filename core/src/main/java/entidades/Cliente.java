package entidades;

import com.badlogic.gdx.graphics.Texture;
import dialogos.ArbolDialogo;
import dialogos.NodoDialogo;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private Texture textura;
    private List<Producto> pedido;
    private ArbolDialogo dialogo;

    public Cliente(String nombre, String rutaTextura) {
        this.nombre = nombre;
        this.textura = new Texture(rutaTextura);
        this.pedido = new ArrayList<>();
        this.dialogo = new ArbolDialogo();
        generarPedidoAleatorio();
        configurarDialogos();
    }

    private void generarPedidoAleatorio() {
        Producto pancho = new Producto("Pancho", 150.0, true);
        Producto coca = new Producto("Coca-Cola", 100.0, false);

        int tipoPedido = (int) (Math.random() * 3); // 0, 1 o 2

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

    private void configurarDialogos() {
        NodoDialogo n1 = new NodoDialogo(1, "Buenas. Dame lo que te pedí rápido, no tengo todo el día.");
        n1.agregarOpcion("Acá tenés, que tengas buen día.", 2);
        n1.agregarOpcion("Pará un poco, recién llego a la barra.", 3);
        n1.agregarOpcion("No me apures o no te vendo nada.", 4);

        NodoDialogo n2 = new NodoDialogo(2, "Gracias. Por lo menos sos eficiente.");
        NodoDialogo n3 = new NodoDialogo(3, "Mmmm... la próxima vení más preparado.");
        NodoDialogo n4 = new NodoDialogo(4, "¿Cómo dijiste? Hablá bien con los clientes.");

        dialogo.agregarNodo(n1);
        dialogo.agregarNodo(n2);
        dialogo.agregarNodo(n3);
        dialogo.agregarNodo(n4);
    }

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
