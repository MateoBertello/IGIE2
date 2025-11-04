package ieig2;


import ieig2.controlador.BatallaControlador;
import ieig2.vista.BatallaVistaConsola;


public class IEIG2 {

    public static void main(String[] args) {
        
        // 1. Crear la Vista
        BatallaVistaConsola vista = new BatallaVistaConsola();
        
        // 2. Crear el Controlador y pasarle la Vista
        BatallaControlador controlador = new BatallaControlador(vista);
        
        // 3. Iniciar la aplicación
        // El controlador ahora toma el control de todo el flujo del juego.
        controlador.iniciarBatalla();
    }
}