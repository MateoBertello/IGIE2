package ieig2.vista;

import javax.swing.*;
import java.awt.*;

public class BatallaVista extends JFrame {
    // Menú
    public final JMenuItem miPausar    = new JMenuItem("Pausar");
    public final JMenuItem miGuardar   = new JMenuItem("Guardar partida");
    public final JMenuItem miAvanzar   = new JMenuItem("Avanzar turno");         // ⬅ nuevo
    public final JMenuItem miAuto      = new JMenuItem("Simulación automática");  // ⬅ nuevo
    public final JMenuItem miSalir     = new JMenuItem("Salir");

    public final JMenuItem miHistorial = new JMenuItem("Historial de Partidas");
    public final JMenuItem miStats     = new JMenuItem("Estadísticas generales");
    public final JMenuItem miRanking   = new JMenuItem("Ranking de personajes");

    // Paneles
    private final PanelInfoSuperior top = new PanelInfoSuperior();
    private final JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
    private final PanelPersonaje left  = new PanelPersonaje("Héroe");
    private final PanelPersonaje right = new PanelPersonaje("Villano");
    private final PanelEventos logPanel = new PanelEventos();

    public BatallaVista() {
        super("Batalla — Ventana Principal");
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
        mPartida.add(miAvanzar);    // ⬅ nuevo
        mPartida.add(miAuto);       // ⬅ nuevo
        mPartida.addSeparator();
        mPartida.add(miSalir);

        JMenu mVer = new JMenu("Ver");
        mVer.add(miHistorial);
        mVer.add(miStats);
        mVer.add(miRanking);

        bar.add(mPartida);
        bar.add(mVer);
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
}
