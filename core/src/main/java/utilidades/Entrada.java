package utilidades;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Entrada {

    private Scanner s = new Scanner(System.in);

    public int ingresarEntero(int min, int max){
        int opc = 0;
        boolean error = false;

        do {
            error = false;
            System.out.println("Ingrese una opción entre "+min+" y "+max+":");
            try {
                opc = s.nextInt();
                if(opc<min || opc>max){
                    System.out.println("Opción inválida, el número debe estar entre "+min+" y "+max+". Intente nuevamente.");
                    error = true;
                }
            } catch(InputMismatchException e){
                System.out.println("El dato ingresado debe ser un número, intente nuevamente.");
                error = true;
            } finally {
                s.nextLine();
            }
        } while(error);

        return opc;

    }

    public String ingresarTexto(){
        return s.nextLine();
    }

    public void cerrarScanner(){
        s.close();
    }

}
