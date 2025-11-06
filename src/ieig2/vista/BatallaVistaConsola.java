package ieig2.vista;

import java.util.Scanner;
import ieig2.modelo.Heroe;
import ieig2.modelo.Villano;

/**
 * La Vista. Se encarga ÚNICAMENTE de mostrar cosas en la consola y de pedir
 * datos al usuario (System.out y Scanner).
 */
public class BatallaVistaConsola {

    // 1. Añade el Scanner (la Vista es dueña de la entrada)
    private Scanner sc = new Scanner(System.in);

    /**
     * Muestra un mensaje simple en la consola y aplica una pausa.
     */
    public void mostrarMensaje(String mensaje) {
        // TODO: Añade el System.out.println(mensaje) y una llamada a pausa()
    }

    /**
     * Muestra un prompt (sin salto de línea) y pide una entrada de texto.
     */
    public String pedirEntrada(String prompt) {
        // TODO: Muestra el 'prompt' con System.out.print()
        // TODO: Retorna un sc.nextLine()
        return ""; // Borra esta línea cuando lo implementes
    }

    /**
     * Método específico para pedir apodos.
     */
    public String pedirApodo(String tipoPersonaje) {
        // TODO: Muestra el prompt "Ingrese apodo para el [tipoPersonaje]: "
        // TODO: Retorna un sc.nextLine()
        return ""; // Borra esta línea cuando lo implementes
    }

    /**
     * Muestra el estado actual de los personajes.
     */
    public void mostrarEstadoPersonajes(Heroe h, Villano v) {
        // TODO: Imprime h.toString()
        // TODO: Imprime v.toString()
        // TODO: Llama a pausa(2000)
    }

    /**
     * Muestra el mensaje de victoria.
     */
    public void mostrarGanador(String ganadorNombre, int turnos) {
        // TODO: Imprime el mensaje ".... ha ganado la batalla!" como en tu main original
    }

    /**
     * Cierra el Scanner.
     */
    public void cerrarScanner() {
        try {
            if (sc != null) {
                sc.close();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Método de utilidad para pausar la consola.
     */
    public void pausa(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
