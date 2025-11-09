package ieig2.controlador;

import ieig2.modelo.*;
import ieig2.vista.BatallaVistaConsola;

import java.io.IOException;
import java.util.Random;

public class BatallaControlador {

    private BatallaVistaConsola vista;
    private Heroe heroe;
    private Villano villano;
    private HistorialBatallas historial;
    private Random rnd = new Random();
    private int turnoActual = 0;

    public BatallaControlador(BatallaVistaConsola vista) {
        this.vista = vista;
        try {
            this.historial = PersistenciaManager.cargarHistorial();
        } catch (IOException e) {
            this.historial = new HistorialBatallas();
            System.err.println("No se pudo cargar historial: " + e.getMessage());
        }
    }

    public void iniciarBatallaCon(Heroe h, Villano v) {
        this.heroe = h;
        this.villano = v;

        vista.mostrarMensaje("\nComienza la batalla");
        vista.mostrarEstadoPersonajes(heroe, villano);

        turnoActual = 0;
        while (heroe.estaVivo() && villano.estaVivo()) {
            turnoActual++;

            String logHeroe = heroe.decidirAccion(villano);
            vista.mostrarMensaje(logHeroe);
            if (!villano.estaVivo()) break;

            String logVillano = villano.decidirAccion(heroe);
            vista.mostrarMensaje(logVillano);

            vista.mostrarMensaje("\n--- Fin del Turno " + turnoActual + " ---");
            vista.mostrarEstadoPersonajes(heroe, villano);

            // Opciones de consola en cada turno
            String op = vista.pedirEntrada("Opciones: [A]vanzar | [G]uardar | [S]alir sin finalizar: ");
            System.out.println("[DEBUG] Opción ingresada: " + op);
            if (op != null) {
                op = op.trim();
                if (op.equalsIgnoreCase("G")) {
                    guardarPartidaManual();
                } else if (op.equalsIgnoreCase("S")) {
                    vista.mostrarMensaje("Saliendo de la batalla sin finalizar.");
                    return; // corta el método sin llamar a finalizarBatalla
                }
            }

        }

        finalizarBatalla(turnoActual);
    }

    public void iniciarBatalla() {
        setupBatalla();

        vista.mostrarMensaje("\nComienza la batalla");
        vista.mostrarEstadoPersonajes(heroe, villano);

        turnoActual = 0;
        while (heroe.estaVivo() && villano.estaVivo()) {
            turnoActual++;

            String logHeroe = heroe.decidirAccion(villano);
            vista.mostrarMensaje(logHeroe);

            if (!villano.estaVivo()) break;

            String logVillano = villano.decidirAccion(heroe);
            vista.mostrarMensaje(logVillano);

            vista.mostrarMensaje("\n--- Fin del Turno " + turnoActual + " ---");
            vista.mostrarEstadoPersonajes(heroe, villano);

            // Opciones de consola en cada turno
            String op = vista.pedirEntrada("Opciones: [A]vanzar | [G]uardar | [S]alir sin finalizar: ");
            System.out.println("[DEBUG] Opción ingresada: " + op);
            if (op != null) {
                op = op.trim();
                if (op.equalsIgnoreCase("G")) {
                    guardarPartidaManual();
                } else if (op.equalsIgnoreCase("S")) {
                    vista.mostrarMensaje("Saliendo de la batalla sin finalizar.");
                    return; // corta el método sin llamar a finalizarBatalla
                }
            }
        }

        finalizarBatalla(turnoActual);
    }

    private void setupBatalla() {
        Apodo apodoHeroe = null;
        while (apodoHeroe == null) {
            try {
                String input = vista.pedirApodo("Héroe");
                apodoHeroe = new Apodo(input);
            } catch (IllegalArgumentException e) {
                vista.mostrarMensaje("Error: " + e.getMessage());
            }
        }

        Apodo apodoVillano = null;
        while (apodoVillano == null) {
            try {
                String input = vista.pedirApodo("Villano");
                apodoVillano = new Apodo(input);
            } catch (IllegalArgumentException e) {
                vista.mostrarMensaje("Error: " + e.getMessage());
            }
        }

        String nombreHeroe = vista.pedirEntrada("Ingrese nombre del héroe: ");
        heroe = new Heroe(
                nombreHeroe,
                100 + rnd.nextInt(41),
                21 + rnd.nextInt(11),
                5 + rnd.nextInt(8),
                rnd.nextInt(102)
        );

        String nombreVillano = vista.pedirEntrada("Ingrese nombre del villano: ");
        villano = new Villano(
                nombreVillano,
                90 + rnd.nextInt(41),
                20 + rnd.nextInt(11),
                6 + rnd.nextInt(8),
                rnd.nextInt(101)
        );

        heroe.setNombre(nombreMostrar(heroe.getNombre(), apodoHeroe));
        villano.setNombre(nombreMostrar(villano.getNombre(), apodoVillano));
    }

    private void finalizarBatalla(int turno) {
        String ganadorNombre = heroe.estaVivo() ? heroe.getNombre() : villano.getNombre();
        vista.mostrarGanador(ganadorNombre, turno);

        String entrada = historial.crearEntradaBatalla(
                heroe.getNombre(), villano.getNombre(), ganadorNombre, turno
        );
        historial.guardarBatalla(entrada);

        try {
            PersistenciaManager.guardarHistorial(historial);
        } catch (IOException e) {
            System.err.println("No se pudo guardar historial: " + e.getMessage());
        }

        String logHistorial = historial.obtenerHistorialComoString();
        vista.mostrarMensaje(logHistorial);

        String reporte = generarReporteFinal(heroe, villano, turno, historial);
        vista.mostrarMensaje(reporte);

        vista.cerrarScanner();
    }

    public void guardarPartidaManual() {
    System.out.println("[DEBUG] guardarPartidaManual() llamado. turno=" + turnoActual);
    try {
        PersistenciaManager.guardarPartida(heroe, villano, turnoActual);
        System.out.println("[DEBUG] guardarPartidaManual() OK");
        vista.mostrarMensaje("\nPartida guardada con éxito.\n");
    } catch (IOException ex) {
        System.out.println("[DEBUG] guardarPartidaManual() ERROR: " + ex.getMessage());
        vista.mostrarMensaje("\nError al guardar partida: " + ex.getMessage() + "\n");
    }
}

    private String nombreMostrar(String nombreBase, Apodo apodo) {
        if (apodo == null || apodo.getValor() == null || apodo.getValor().isBlank()) return nombreBase;
        return nombreBase + " (" + apodo.getValor() + ")";
    }

    private String generarReporteFinal(Heroe heroe, Villano villano, int turnos, HistorialBatallas hist) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=======================================\n");
        sb.append("        REPORTE FINAL DE LA BATALLA\n");
        sb.append("=======================================\n");

        sb.append("Heroe: ").append(heroe.getNombre())
          .append(" - Vida final: ").append(heroe.getVida()).append("\n");
        sb.append("Villano: ").append(villano.getNombre())
          .append(" - Vida final: ").append(villano.getVida()).append("\n");

        sb.append("\n--- ARMAS INVOCADAS ---\n");
        sb.append(formatearArmasInvocadas(heroe));
        sb.append(formatearArmasInvocadas(villano));

        sb.append("\n--- ATAQUES ESPECIALES ---\n");
        boolean huboAlgo = false;

        if (heroe.getSupremosUsados() > 0) {
            sb.append(heroe.getNombre()).append(" activó un ataque supremo.\n");
            huboAlgo = true;
        }

        if (villano.getSupremosUsados() > 0) {
            sb.append(villano.getNombre()).append(" activó un ataque supremo.\n");
            huboAlgo = true;
        }

        if (!huboAlgo) {
            sb.append("(sin usos de ataques supremos)\n");
        }

        sb.append("\n--- HISTORIAL RECIENTE ---\n");
        sb.append(hist.obtenerHistorialComoString());

        sb.append("=======================================\n");
        return sb.toString();
    }

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
