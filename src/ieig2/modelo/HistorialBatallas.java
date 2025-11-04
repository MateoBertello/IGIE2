package ieig2.modelo;

public class HistorialBatallas {

    // === Historial de batallas (últimas 5) ===
    // Estos campos ya no son 'static'
    private static final int MAX_BATALLAS = 5;
    private String[] historialBatallas = new String[MAX_BATALLAS];
    private int contadorBatallas = 0;
    private int numeroBatallaGlobal = 0;

    /**
     * Crea la entrada de texto para una batalla que acaba de terminar.
     *
     */
    public String crearEntradaBatalla(String heroe, String villano, String ganador, int turnos) {
        numeroBatallaGlobal++; // Incrementa el contador global
        StringBuilder sb = new StringBuilder();
        sb.append("BATALLA #").append(numeroBatallaGlobal).append(" - ")
          .append("Heroe: ").append(heroe).append(" | ")
          .append("Villano: ").append(villano).append(" | ")
          .append("Ganador: ").append(ganador).append(" | ")
          .append("Turnos: ").append(turnos);
        return sb.toString();
    }

    /**
     * Guarda la última batalla en el array, desplazando las antiguas si está lleno.
     *
     */
    public void guardarBatalla(String batalla) {
        if (contadorBatallas < MAX_BATALLAS) {
            historialBatallas[contadorBatallas] = batalla;
            contadorBatallas++;
        } else {
            // Desplaza todas las batallas una posición hacia arriba
            for (int i = 0; i < MAX_BATALLAS - 1; i++) {
                historialBatallas[i] = historialBatallas[i + 1];
            }
            // Añade la nueva batalla al final
            historialBatallas[MAX_BATALLAS - 1] = batalla;
        }
    }

    /**
     * ¡CAMBIO IMPORTANTE DE MVC!
     * Ya no 'imprime' el historial (void).
     * Ahora 'devuelve' el historial como un String para que la Vista lo imprima.
     *
     */
    public String obtenerHistorialComoString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- HISTORIAL RECIENTE ---\n");

        if (contadorBatallas == 0) {
            sb.append("Aún no se ha registrado ninguna batalla.\n");
        } else {
            for (int i = 0; i < contadorBatallas; i++) {
                String entrada = historialBatallas[i];
                if (entrada == null) entrada = "(vacío)";
                sb.append(entrada).append("\n");
            }
        }
        return sb.toString();
    }
    
    // Getters que el Controlador podría necesitar para el reporte
    public int getContadorBatallas() { return contadorBatallas; }
    public String[] getHistorialBatallas() { return historialBatallas; }
}