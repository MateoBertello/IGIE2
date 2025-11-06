package ieig2;

import ieig2.controlador.ConfigInicialControlador;
import ieig2.controlador.BatallaControlador;     // tu controlador actual
import ieig2.modelo.*;
import ieig2.vista.BatallaVistaConsola;         // tu vista actual (consola)
import ieig2.vista.ConfigInicialPanel;

import javax.swing.*;
import java.util.List;

public class IEIG2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("IEIG2 — Configuración Inicial");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ConfigInicialPanel panel = new ConfigInicialPanel();
            new ConfigInicialControlador(panel, (List<Jugador> jugadores, ConfigPartida cfg) -> {
                // Elegimos el primer HÉROE y el primer VILLANO
                Jugador jH = jugadores.stream().filter(j -> j.getTipo()==TipoPersonaje.HEROE).findFirst().get();
                Jugador jV = jugadores.stream().filter(j -> j.getTipo()==TipoPersonaje.VILLANO).findFirst().get();

                // Construimos TUS clases del modelo con los stats configurados
                Heroe heroe = new Heroe(jH.getNombre() + " (" + jH.getApodo() + ")", cfg.vida, cfg.fuerza, cfg.defensa, cfg.bendicion);
                Villano villano = new Villano(jV.getNombre() + " (" + jV.getApodo() + ")", cfg.vida, cfg.fuerza, cfg.defensa, cfg.bendicion);

                // Por ahora, reusamos tu consola y tu BatallaControlador
                BatallaVistaConsola consola = new BatallaVistaConsola();
                BatallaControlador ctrl = new BatallaControlador(consola);

                // >>> Opción A: usar tu método iniciarBatallaCon (ver edición mínima abajo)
                ctrl.iniciarBatallaCon(heroe, villano);

                // Si después tenés una Ventana de Batalla Swing, abrila acá y cerrá el frame:
                // new BatallaVistaSwing(jugadores, cfg).setVisible(true);
                // f.dispose();
            });

            f.setContentPane(panel);
            f.pack();
            f.setSize(900, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
