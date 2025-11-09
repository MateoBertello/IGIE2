package ieig2.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class VentanaReporteFinal extends JFrame {
    private final CardLayout layout = new CardLayout();
    private final JPanel panelPrincipal = new JPanel(layout);

    private JTable tablaRanking;
    private JTextArea areaStats;
    private JTextArea areaHistorial;

    public VentanaReporteFinal(String seccionInicial) {
        setTitle("Reporte Final del Juego");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear las tres secciones
        panelPrincipal.add(crearPanelRanking(), "ranking");
        panelPrincipal.add(crearPanelStats(), "stats");
        panelPrincipal.add(crearPanelHistorial(), "historial");

        add(panelPrincipal);
        layout.show(panelPrincipal, seccionInicial);
    }

    // ==============================
    // PANEL RANKING
    // ==============================
    private JPanel crearPanelRanking() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Ranking de Personajes", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        String[] columnas = {"Nombre", "Apodo", "Tipo", "Vida Final", "Victorias", "Ataques Supremos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablaRanking = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaRanking);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
      // ==============================
    // PANEL ESTADÍSTICAS
    // ==============================
    private JPanel crearPanelStats() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Estadísticas Generales", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        areaStats = new JTextArea();
        areaStats.setEditable(false);
        areaStats.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaStats.setText("No hay estadísticas disponibles todavía.");

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaStats), BorderLayout.CENTER);
        return panel;
    }

    // ==============================
    // PANEL HISTORIAL
    // ==============================
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Historial de Últimas 5 Partidas", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaHistorial.setText("No hay historial disponible todavía.");

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaHistorial), BorderLayout.CENTER);
        return panel;
    }

    // ==============================
    // MÉTODOS PARA CARGAR DATOS
    // ==============================

    // 🔹 Cargar el ranking (más adelante se pasará la lista real)
    public void cargarRanking(List<Object[]> datos) {
        String[] columnas = {"Nombre", "Apodo", "Tipo", "Vida Final", "Victorias", "Ataques Supremos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        if (datos != null && !datos.isEmpty()) {
            for (Object[] fila : datos) {
                modelo.addRow(fila);
            }
        } else {
            modelo.addRow(new Object[]{"-", "-", "-", "-", "-", "-"});
        }

        tablaRanking.setModel(modelo);
    }

    // 🔹 Cargar estadísticas generales
    public void cargarEstadisticas(Map<String, String> estadisticas) {
        if (estadisticas == null || estadisticas.isEmpty()) {
            areaStats.setText("No hay estadísticas disponibles todavía.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Mayor daño en un solo ataque: ").append(estadisticas.getOrDefault("mayorDanio", "-")).append("\n");
        sb.append("Batalla más larga: ").append(estadisticas.getOrDefault("batallaLarga", "-")).append("\n");
        sb.append("Total de armas invocadas: ").append(estadisticas.getOrDefault("armas", "-")).append("\n");
        sb.append("Ataques supremos ejecutados: ").append(estadisticas.getOrDefault("ataques", "-")).append("\n");
        sb.append("Porcentaje de victorias por tipo: ").append(estadisticas.getOrDefault("porcentaje", "-")).append("\n");

        areaStats.setText(sb.toString());
    }

    // 🔹 Cargar historial de partidas
    public void cargarHistorial(List<String> historial) {
        if (historial == null || historial.isEmpty()) {
            areaHistorial.setText("No hay historial disponible todavía.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String linea : historial) {
            sb.append(linea).append("\n");
        }
        areaHistorial.setText(sb.toString());
    }

    // ==============================
    // TEST VISUAL (podés borrar esto si querés)
    // ==============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaReporteFinal vista = new VentanaReporteFinal("ranking");
            vista.setVisible(true);
        });
    }
}
