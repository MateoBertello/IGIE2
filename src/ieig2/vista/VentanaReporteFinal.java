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