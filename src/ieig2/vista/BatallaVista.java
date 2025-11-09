package ieig2.vista;

import javax.swing.*;
import java.awt.*;

public class BatallaVista extends JFrame {
    // ===== Menú =====
    public final JMenuItem miPausar    = new JMenuItem("Pausar");
    public final JMenuItem miGuardar   = new JMenuItem("Guardar partida");
    public final JMenuItem miAvanzar   = new JMenuItem("Avanzar turno");         // ⬅ nuevo
    public final JMenuItem miAuto      = new JMenuItem("Simulación automática");  // ⬅ nuevo
    public final JMenuItem miSalir     = new JMenuItem("Salir");

    // ===== Menú Reportes =====
    public final JMenuItem miHistorial = new JMenuItem("Historial de Batallas");
    public final JMenuItem miStats     = new JMenuItem("Estadísticas generales");
    public final JMenuItem miRanking   = new JMenuItem("Ranking de personajes");

    // ===== Paneles principales =====
    private final PanelInfoSuperior top = new PanelInfoSuperior();
    private final JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
    private final PanelPersonaje left  = new PanelPersonaje("Héroe");
    private final PanelPersonaje right = new PanelPersonaje("Villano");
    private final PanelEventos logPanel = new PanelEventos();

    public BatallaVista() {
        super("Simulación de Batalla");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        setMinimumSize(new Dimension(980, 640));

        setJMenuBar(buildMenu());
        add(top, BorderLayout.NORTH);

        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        center.add(left);
        center.add(right);
        add(center, BorderLayout.CENTER);

        add(logPanel, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar buildMenu() {
        JMenuBar bar = new JMenuBar();

        JMenu mPartida = new JMenu("Partida");
        mPartida.add(miPausar);
        mPartida.add(miGuardar);
        mPartida.add(miAvanzar);
        mPartida.add(miAuto);
        mPartida.addSeparator();
        mPartida.add(miSalir);

        JMenu mReportes = new JMenu("Reportes");
        mReportes.add(miHistorial);
        mReportes.add(miStats);
        mReportes.add(miRanking);

        bar.add(mPartida);
        bar.add(mReportes);
        return bar;
    }

    // ==== API para el Controlador ====
    public void setBattleInfo(int currentBattle, int totalBattles, int turn) {
        top.setBattleInfo(currentBattle, totalBattles, turn);
    }

    public void updateLeft(PersonajeVM vm)  { left.updateFrom(vm); }
    public void updateRight(PersonajeVM vm) { right.updateFrom(vm); }
    public void appendEvent(String text)    { logPanel.append(text); }
    public void autoScrollLog(boolean b)    { logPanel.setAutoScroll(b); }
    public void addGuardarListener(java.awt.event.ActionListener l) { miGuardar.addActionListener(l); }
    public void addPausarListener(java.awt.event.ActionListener l)  { miPausar.addActionListener(l); }
    public void addAvanzarListener(java.awt.event.ActionListener l) { miAvanzar.addActionListener(l); }
    public void addAutoListener(java.awt.event.ActionListener l)    { miAuto.addActionListener(l); }
    public void addSalirListener(java.awt.event.ActionListener l)   { miSalir.addActionListener(l); }

}