/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ieig2.modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Objects;

/**
 * Clase encargada de manejar la persistencia de datos (Punto 5).
 * Utiliza archivos de texto para guardar el historial y el estado de partida.
 */
public class PersistenciaManager {

    private static final String HISTORIAL_FILE = "historial_batallas.txt";
    private static final String PARTIDA_FILE   = "batalla_guardada.txt";

    // ================================================================
    // 1) Persistencia de Historial (historial_batallas.txt)
    // ================================================================

    /**
     * Guarda el historial de batallas en el archivo.
     * Formato:
     *   <numeroBatallaGlobal>
     *   BATALLA #1 - ...
     *   BATALLA #2 - ...
     */
    public static void guardarHistorial(HistorialBatallas historial) throws IOException {
        Objects.requireNonNull(historial, "historial no puede ser null");
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(HISTORIAL_FILE), StandardCharsets.UTF_8)) {

            writer.write(String.valueOf(historial.getNumeroBatallaGlobal())); // usar getter
            writer.newLine();

            String[] batallas = historial.getHistorialBatallas();
            int cant = historial.getContadorBatallas();
            for (int i = 0; i < cant; i++) {
                if (batallas[i] != null) {
                    writer.write(batallas[i]);
                    writer.newLine();
                }
            }
        }
    }

    /**
     * Carga el historial de batallas desde el archivo.
     * Si el archivo no existe, devuelve un historial vacío.
     */
    public static HistorialBatallas cargarHistorial() throws IOException {
        HistorialBatallas historial = new HistorialBatallas();
        Path p = Paths.get(HISTORIAL_FILE);
        if (!Files.exists(p)) return historial;

        try (BufferedReader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            // 1) número de batalla global
            String lineaNumeroBatalla = reader.readLine();
            if (lineaNumeroBatalla != null && !lineaNumeroBatalla.isEmpty()) {
                try {
                    historial.setNumeroBatallaGlobal(Integer.parseInt(lineaNumeroBatalla));
                } catch (NumberFormatException nfe) {
                    // Si hay basura en la primera línea, lo dejamos en 0 sin romper.
                    historial.setNumeroBatallaGlobal(0);
                }
            }

            // 2) líneas del historial
            String linea;
            while ((linea = reader.readLine()) != null) {
                historial.guardarBatalla(linea);
            }
        }
        return historial;
    }

    // ================================================================
    // 2) Persistencia de Estado de Partida (batalla_guardada.txt)
    //    Formato de ejemplo (CSV simple):
    //      TURNO:<int>
    //      HEROE:Nombre,vida,fuerza,defensa
    //      VILLANO:Nombre,vida,fuerza,defensa
    // ================================================================

    /**
     * Guarda el estado completo de la partida (personajes y turno actual).
     * Requiere que Personaje tenga: getNombre(), getVida(), getFuerza(), getDefensa()
     */
    public static void guardarPartida(Personaje heroe, Personaje villano, int turnoActual) throws IOException {
        Objects.requireNonNull(heroe, "heroe no puede ser null");
        Objects.requireNonNull(villano, "villano no puede ser null");

       
    }

    /**
     * DTO para devolver al cargar la partida, sin acoplarse a constructores específicos.
     * El controlador puede usar esto para instanciar Heroe/Villano como corresponda.
     */
    public static class PartidaCargada {
        public int turno;

        public String heroeNombre;
        public int heroeVida, heroeFuerza, heroeDefensa;

        public String villanoNombre;
        public int villanoVida, villanoFuerza, villanoDefensa;

        @Override public String toString() {
            return "PartidaCargada{turno=" + turno +
                    ", HEROE=(" + heroeNombre + "," + heroeVida + "," + heroeFuerza + "," + heroeDefensa + ")" +
                    ", VILLANO=(" + villanoNombre + "," + villanoVida + "," + villanoFuerza + "," + villanoDefensa + ")}";
        }
    }

    /**
     * Carga el estado de la partida desde archivo y devuelve un DTO con los datos.
     * Si no existe archivo, devuelve null.
     */
    public static PartidaCargada cargarPartida() throws IOException {
        Path p = Paths.get(PARTIDA_FILE);
        if (!Files.exists(p)) return null;

        PartidaCargada dto = new PartidaCargada();

        try (BufferedReader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String linea;

            // TURNO
            linea = reader.readLine();
            if (linea == null || !linea.startsWith("TURNO:")) {
                throw new IOException("Formato inválido: falta línea TURNO");
            }
            dto.turno = parseEnteroDespuesDePrefijo(linea, "TURNO:");

            // HEROE
            linea = reader.readLine();
            if (linea == null || !linea.startsWith("HEROE:")) {
                throw new IOException("Formato inválido: falta línea HEROE");
            }
            String[] h = parseCSVDespuesDePrefijo(linea, "HEROE:", 4);
            dto.heroeNombre  = h[0];
            dto.heroeVida    = parseEntero(h[1], "HEROE.vida");
            dto.heroeFuerza  = parseEntero(h[2], "HEROE.fuerza");
            dto.heroeDefensa = parseEntero(h[3], "HEROE.defensa");

            // VILLANO
            linea = reader.readLine();
            if (linea == null || !linea.startsWith("VILLANO:")) {
                throw new IOException("Formato inválido: falta línea VILLANO");
            }
            String[] v = parseCSVDespuesDePrefijo(linea, "VILLANO:", 4);
            dto.villanoNombre  = v[0];
            dto.villanoVida    = parseEntero(v[1], "VILLANO.vida");
            dto.villanoFuerza  = parseEntero(v[2], "VILLANO.fuerza");
            dto.villanoDefensa = parseEntero(v[3], "VILLANO.defensa");
        }

        return dto;
    }

    // ================================================================
    // Utilitarios de parseo/seguridad simples
    // ================================================================

    private static String sanitize(String s) {
        // Evita comas y saltos de línea que rompan el CSV simple
        if (s == null) return "";
        return s.replace(",", " ").replace("\r", " ").replace("\n", " ").trim();
    }

    private static int parseEnteroDespuesDePrefijo(String linea, String prefijo) throws IOException {
        try {
            return Integer.parseInt(linea.substring(prefijo.length()).trim());
        } catch (Exception e) {
            throw new IOException("No se pudo parsear entero en: " + linea, e);
        }
    }

    private static int parseEntero(String val, String campo) throws IOException {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            throw new IOException("No se pudo parsear entero en " + campo + ": '" + val + "'", e);
        }
    }

    private static String[] parseCSVDespuesDePrefijo(String linea, String prefijo, int esperados) throws IOException {
        String datos = linea.substring(prefijo.length()).trim();
        String[] arr = datos.split(",", -1);
        if (arr.length != esperados) {
            throw new IOException("Cantidad de campos inválida en " + prefijo + " (esperados " + esperados + ", got " + arr.length + ")");
        }
        return arr;
    }
}
