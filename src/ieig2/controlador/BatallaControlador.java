package ieig2.controlador;

// Importa todas las clases del Modelo y la Vista
import ieig2.modelo.*;
import ieig2.vista.BatallaVistaConsola;

import java.util.Random;

/**
 * El Controlador. El "director de orquesta".
 * Conecta la Vista (que muestra/pide) con el Modelo (que procesa).
 */
public class BatallaControlador {

    // El Controlador "posee" las instancias de la Vista y los componentes del Modelo
    private BatallaVistaConsola vista;
    private Heroe heroe;
    private Villano villano;
    private HistorialBatallas historial;
    private Random rnd = new Random();

    /**
     * Constructor. Recibe la Vista que va a controlar.
     */
    public BatallaControlador(BatallaVistaConsola vista) {
        this.vista = vista;
        this.historial = new HistorialBatallas(); // El controlador crea el historial
    }

    /**
     * Método principal que inicia todo el flujo del juego.
     * Reemplaza al antiguo 'main'.
     */
    
    public void iniciarBatallaCon(Heroe h, Villano v) {
    this.heroe = h;
    this.villano = v;

    vista.mostrarMensaje("\n⚔️ ¡Comienza la batalla! ⚔️");
    vista.mostrarEstadoPersonajes(heroe, villano);

    int turno = 0;
    while (heroe.estaVivo() && villano.estaVivo()) {
        turno++;

        // Turno del Héroe
        String logHeroe = heroe.decidirAccion(villano);
        vista.mostrarMensaje(logHeroe);
        if (!villano.estaVivo()) break;

        // Turno del Villano
        String logVillano = villano.decidirAccion(heroe);
        vista.mostrarMensaje(logVillano);

        // Fin de turno
        vista.mostrarMensaje("\n--- Fin del Turno " + turno + " ---");
        vista.mostrarEstadoPersonajes(heroe, villano);
    }

    finalizarBatalla(turno);
}
    
    
   
    public void iniciarBatalla() {
        
        setupBatalla(); // 1. Prepara la batalla (crea personajes)
        
        vista.mostrarMensaje("\n⚔️ ¡Comienza la batalla! ⚔️");
        vista.mostrarEstadoPersonajes(heroe, villano);
        
        // 2. Inicia el "Game Loop"
        int turno = 0;
        while (heroe.estaVivo() && villano.estaVivo()) {
            turno++;
            
            // --- Turno del Héroe ---
            // 1. El Modelo trabaja (y devuelve un log)
            String logHeroe = heroe.decidirAccion(villano); 
            // 2. La Vista muestra el log
            vista.mostrarMensaje(logHeroe); 
            
            if (!villano.estaVivo()) break; // Termina si el villano muere
            
            // --- Turno del Villano ---
            String logVillano = villano.decidirAccion(heroe);
            vista.mostrarMensaje(logVillano);
            
            // Pausa y muestra estado al final del turno
            vista.mostrarMensaje("\n--- Fin del Turno " + turno + " ---");
            vista.mostrarEstadoPersonajes(heroe, villano);
        }

        // 3. Finaliza la batalla
        finalizarBatalla(turno);
    }

    /**
     * Lógica para pedir datos y crear los personajes.
     * (Todo esto estaba en tu main original)
     */
    private void setupBatalla() {
        // --- Pedir Apodo Héroe (con validación MVC) ---
        Apodo apodoHeroe = null;
        while (apodoHeroe == null) {
            try {
                // 1. Vista pide el dato
                String input = vista.pedirApodo("Héroe");
                // 2. Modelo valida el dato
                apodoHeroe = new Apodo(input); 
            } catch (IllegalArgumentException e) {
                // 3. Vista muestra el error
                vista.mostrarMensaje("❌ " + e.getMessage());
            }
        }
        
        // --- Pedir Apodo Villano (con validación MVC) ---
        Apodo apodoVillano = null;
        while (apodoVillano == null) {
            try {
                String input = vista.pedirApodo("Villano");
                apodoVillano = new Apodo(input);
            } catch (IllegalArgumentException e) {
                vista.mostrarMensaje("❌ " + e.getMessage());
            }
        }

        // --- Crear Héroe ---
        String nombreHeroe = vista.pedirEntrada("Ingrese nombre del héroe: ");
        heroe = new Heroe(
                nombreHeroe,
                100 + rnd.nextInt(41),
                21 + rnd.nextInt(11),
                5 + rnd.nextInt(8),
                rnd.nextInt(102)
        );

        // --- Crear Villano ---
        String nombreVillano = vista.pedirEntrada("Ingrese nombre del villano: ");
        villano = new Villano(
                nombreVillano,
                90 + rnd.nextInt(41),
                20 + rnd.nextInt(11),
                6 + rnd.nextInt(8),
                rnd.nextInt(101)
        );
        
        // Guardamos los nombres formateados (del antiguo método nombreMostrar)
        heroe.setNombre(nombreMostrar(heroe.getNombre(), apodoHeroe));
        villano.setNombre(nombreMostrar(villano.getNombre(), apodoVillano));
    }
    
    /**
     * Lógica para mostrar ganador, guardar historial y reporte.
     * (Todo esto estaba al final de tu main)
     */
    private void finalizarBatalla(int turno) {
        
        String ganadorNombre = heroe.estaVivo() ? heroe.getNombre() : villano.getNombre();
        vista.mostrarGanador(ganadorNombre, turno);

        // --- Historial ---
        String entrada = historial.crearEntradaBatalla(
            heroe.getNombre(), villano.getNombre(), ganadorNombre, turno
        );
        historial.guardarBatalla(entrada);
        String logHistorial = historial.obtenerHistorialComoString();
        vista.mostrarMensaje(logHistorial);

        // --- Reporte Final ---
        String reporte = generarReporteFinal(heroe, villano, turno, historial);
        vista.mostrarMensaje(reporte);

        // --- Limpieza ---
        vista.cerrarScanner();
    }

    /**
     * Método de utilidad para formatear el nombre (era 'nombreMostrar' en IEIG1.java)
     *
     */
    private String nombreMostrar(String nombreBase, Apodo apodo) {
        if (apodo == null || apodo.getValor() == null || apodo.getValor().isBlank()) return nombreBase;
        return nombreBase + " (" + apodo.getValor() + ")";
    }

    /**
     * Método de utilidad para armar el reporte (era 'generarReporteFinal' en IEIG1.java)
     *
     */
    private String generarReporteFinal(Heroe heroe, Villano villano, int turnos, HistorialBatallas hist) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=======================================\n");
        sb.append("        REPORTE FINAL DE LA BATALLA\n");
        sb.append("=======================================\n");

        sb.append("Heroe: ").append(heroe.getNombre())
          .append(" - Vida final: ").append(heroe.getVida()).append("\n");
        sb.append("Villano: ").append(villano.getNombre())
          .append(" - Vida final: ").append(villano.getVida()).append("\n");

        // --- ARMAS INVOCADAS ---
        sb.append("\n--- ARMAS INVOCADAS ---\n");
        // (El método 'formatearArmasInvocadas' lo movemos aquí adentro)
        sb.append(formatearArmasInvocadas(heroe));
        sb.append(formatearArmasInvocadas(villano));

        // --- ATAQUES ESPECIALES ---
        sb.append("\n--- ATAQUES ESPECIALES ---\n");
        boolean huboAlgo = false;

        if (heroe.getSupremosUsados() > 0) {
            sb.append(heroe.getNombre())
              .append(" activó \"Castigo Bendito\" → ")
              .append(heroe.getUltimoDanoCastigo())
              .append(" de daño\n");
            huboAlgo = true;
        }
        
        if (villano.getSupremosUsados() > 0 || villano.leviatanInterrumpido()) {
             // (Lógica del Leviatán...)
             huboAlgo = true;
        }

        if (!huboAlgo) {
            sb.append("(sin usos de ataques supremos)\n");
        }

        // --- HISTORIAL RECIENTE ---
        sb.append("\n--- HISTORIAL RECIENTE ---\n");
        sb.append(hist.obtenerHistorialComoString()); // Usamos el método del modelo

        sb.append("=======================================\n");
        return sb.toString();
    }
    
    /**
     * Método de utilidad (era 'formatearArmasInvocadas' en IEIG1.java)
     *
     */
    private String formatearArmasInvocadas(Personaje p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getNombre()).append(": ");
        if (p.getArmasInvocadas().isEmpty()) {
            sb.append("(no invocó armas)");
        } else {
            boolean primero = true;
            for (var e : p.getArmasInvocadas().entrySet()) {
                if (!primero) sb.append(", ");
                sb.append(e.getKey()).append(" (").append(e.getValue()).append(")");
                primero = false;
            }
        }
        return sb.append("\n").toString();
    }
}