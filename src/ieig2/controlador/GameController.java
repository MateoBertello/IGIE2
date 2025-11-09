package ieig2.controlador;

import ieig2.vista.*;
import ieig2.modelo.Heroe;
import ieig2.modelo.Villano;
import ieig2.modelo.HistorialBatallas;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameController {
    private final BatallaVista view;
    private final Heroe h;
    private final Villano v;
    private final HistorialBatallas historial;

    private int currentBattle;
    private final int totalBattles;
    private int turn = 1;
    private boolean paused = false;

    // simulación automática
    private Timer autoTimer;

    public GameController(Heroe heroe, Villano villano, HistorialBatallas hist,
                          int currentBattle, int totalBattles) {
        this.h = heroe; this.v = villano; this.historial = hist;
        this.currentBattle = currentBattle; this.totalBattles = totalBattles;

        this.view = new BatallaVista();
        bindMenu();
        refreshAll();
        this.view.setVisible(true);
    }

    private void bindMenu() {
        view.miPausar.addActionListener(this::onPausar);
        view.miGuardar.addActionListener(this::onGuardar);
        view.miSalir.addActionListener(e -> System.exit(0));

        // nuevos
        view.miAvanzar.addActionListener(e -> advanceOneTurn());
        view.miAuto.addActionListener(e -> toggleAuto());

        view.miHistorial.addActionListener(e ->
                JOptionPane.showMessageDialog(view, historial.obtenerHistorialComoString(),
                        "Historial de Batallas", JOptionPane.INFORMATION_MESSAGE));
        view.miStats.addActionListener(e ->
                JOptionPane.showMessageDialog(view, "(Estadísticas generales)", "Estadísticas",
                        JOptionPane.INFORMATION_MESSAGE));
        view.miRanking.addActionListener(e ->
                JOptionPane.showMessageDialog(view, "(Ranking de personajes)", "Ranking",
                        JOptionPane.INFORMATION_MESSAGE));
    }

    private void onPausar(ActionEvent e) {
        paused = !paused;
        onEvent(paused ? "Partida en pausa" : "Partida reanudada");
    }

    private void onGuardar(ActionEvent e) {
        try {
            historial.guardarBatalla("Guardado en turno " + turn);
            onEvent("Partida guardada correctamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + ex.getMessage(),
                    "Guardar partida", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Avanza UN turno completo: actúa Héroe, luego Villano (si sigue vivo). */
    public void advanceOneTurn() {
        if (paused) { onEvent("(pausado)"); return; }
        if (isFinished()) { checkEndAndShowReportIfNeeded(); return; }

        // Turno del Héroe
        String logH = safeDecidirAccion(h, v);
        if (logH != null && !logH.isBlank()) onEvent(logH);

        if (!v.estaVivo()) {
            endTurn(); return;
        }

        // Turno del Villano
        String logV = safeDecidirAccion(v, h);
        if (logV != null && !logV.isBlank()) onEvent(logV);

        endTurn();
    }

    private String safeDecidirAccion(Object atacante, Object defensor) {
        try {
            // Ambos tienen decidirAccion(Personaje) en tu modelo
            return (String) atacante.getClass()
                    .getMethod("decidirAccion", defensor.getClass().getSuperclass())
                    .invoke(atacante, defensor);
        } catch (NoSuchMethodException nsme) {
            try {
                // fallback: decidirAccion(Object)
                return (String) atacante.getClass()
                        .getMethod("decidirAccion", Object.class)
                        .invoke(atacante, defensor);
            } catch (Exception ignore) { return null; }
        } catch (Exception e) {
            return null;
        }
    }

    private void endTurn() {
        onEvent("\n--- Fin del Turno " + turn + " ---");
        turn++;
        refreshAll();
        checkEndAndShowReportIfNeeded();
    }

    private boolean isFinished() { return !h.estaVivo() || !v.estaVivo(); }

    // ===== Simulación automática con javax.swing.Timer =====
    private void toggleAuto() {
        if (autoTimer != null && autoTimer.isRunning()) {
            autoTimer.stop();
            onEvent("(auto) detenido");
            return;
        }
        autoTimer = new Timer(900, e -> {
            if (paused) return;
            if (isFinished()) { ((Timer)e.getSource()).stop(); checkEndAndShowReportIfNeeded(); return; }
            advanceOneTurn();
        });
        onEvent("(auto) iniciado");
        autoTimer.start();
    }

    // ===== API que podría usar otro motor =====
    public boolean isPaused() { return paused; }
    public void onEvent(String text) { view.appendEvent(text); }
    public void setCurrentBattle(int n) { currentBattle = n; refreshAll(); }

    public void refreshAll() {
        view.setBattleInfo(currentBattle, totalBattles, turn);
        view.updateLeft(Mapper.toVM(h));
        view.updateRight(Mapper.toVM(v));
    }

    public void checkEndAndShowReportIfNeeded() {
        if (isFinished()) {
            view.appendEvent("¡Fin de batalla!");
            String ganador = h.estaVivo() ? h.getNombre() : v.getNombre();
            JOptionPane.showMessageDialog(view,
                    "¡Ganador: " + ganador + "!\nTurnos: " + (turn-1),
                    "Reporte Final", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===== Mapeo de tu modelo → VM de la vista (compactado) =====
    private static class Mapper {
        static PersonajeVM toVM(Object p) {
            String nombre = callStr(p, "getNombre");
            // separar "(Apodo)" si viene adentro del nombre
            String apodo = null;
            int a = nombre.indexOf(" (");
            if (a > 0 && nombre.endsWith(")")) {
                apodo = nombre.substring(a + 2, nombre.length() - 1);
                nombre = nombre.substring(0, a);
            }
            int vida = callInt(p, "getVida");
            int vidaMax = callInt(p, "getVidaMax"); if (vidaMax <= 0) vidaMax = 160;
            int bend  = clamp(tryGet(p, "getBendicion"), 0, 100);
            String arma = String.valueOf(callObj(p, "getArma")); if ("null".equals(arma)) arma = "—";
            boolean critico = vida <= Math.max(1, (int)(vidaMax * 0.15));
            String estadoEspecial = null; // poné acá si tenés info (leviatán, etc.)
            return new PersonajeVM(nombre, apodo, vida, vidaMax, bend, arma, estadoEspecial, critico);
        }
        static int clamp(int v, int lo, int hi){ return Math.max(lo, Math.min(hi, v)); }
        static Object callObj(Object o, String m){ try { return o.getClass().getMethod(m).invoke(o);} catch(Exception e){return null;}}
        static String callStr(Object o, String m){ Object r=callObj(o,m); return r==null?"-":r.toString();}
        static int callInt(Object o, String m){ Object r=callObj(o,m); return (r instanceof Number)?((Number)r).intValue():0; }
        static int tryGet(Object o, String m){ Object r=callObj(o,m); return (r instanceof Number)?((Number)r).intValue():-1; }
    }
}
