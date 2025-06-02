package frontend;

import backend.Prueba;
import backend.Pregunta;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class VentanaPrueba extends JFrame {
    private Prueba prueba;
    private List<Pregunta> preguntas;
    private int indiceActual = 0;

    private JLabel lblPregunta;
    private JRadioButton[] opciones;
    private ButtonGroup grupo;
    private JButton btnAnterior, btnSiguiente;
    private String[] respuestasUsuario;
    private boolean modoRevision = false;

    public VentanaPrueba(String rutaJSON) {
        setTitle("Realizar prueba");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);


        cargarPrueba(rutaJSON);
        construirGUI();
        mostrarPregunta();
    }

  
    private void cargarPrueba(String ruta) {
        try {
            Gson gson = new Gson();
            prueba = gson.fromJson(new FileReader(ruta), Prueba.class);
            preguntas = prueba.getPreguntas();
            respuestasUsuario = new String[preguntas.size()];
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo JSON:\n" + e.getMessage());
        }
    }

    private void construirGUI() {
        lblPregunta = new JLabel("", JLabel.CENTER);
        lblPregunta.setForeground(Color.WHITE);
        lblPregunta.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(lblPregunta, BorderLayout.NORTH);

        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBackground(Color.BLACK);
        grupo = new ButtonGroup();

        opciones = new JRadioButton[4];
        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = new JRadioButton();
            opciones[i].setOpaque(true);
            opciones[i].setBackground(Color.BLACK);
            opciones[i].setForeground(Color.WHITE);
            opciones[i].setFont(new Font("SansSerif", Font.PLAIN, 18));
            grupo.add(opciones[i]);
            panelOpciones.add(opciones[i]);
        }
        add(panelOpciones, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.BLACK);

        btnAnterior = new JButton("Anterior");
        btnAnterior.setBackground(Color.BLACK);
        btnAnterior.setForeground(Color.CYAN);
        btnAnterior.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setBackground(Color.BLACK);
        btnSiguiente.setForeground(Color.CYAN);
        btnSiguiente.setFont(new Font("SansSerif", Font.BOLD, 14));

        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        add(panelBotones, BorderLayout.SOUTH);

        btnAnterior.addActionListener(e -> {
            if (!modoRevision) guardarRespuesta();
            if (indiceActual > 0) {
                indiceActual--;
                mostrarPregunta();
            }
        });

        btnSiguiente.addActionListener(e -> {
            if (!modoRevision) guardarRespuesta();
            if (indiceActual < preguntas.size() - 1) {
                indiceActual++;
                mostrarPregunta();
            } else {
                if (!modoRevision) {
                    guardarRespuesta();
                    List<Integer> faltantes = new ArrayList<>();
                    for (int i = 0; i < respuestasUsuario.length; i++) {
                        if (respuestasUsuario[i] == null) {
                            faltantes.add(i + 1);
                        }
                    }

                    if (!faltantes.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Faltan preguntas por responder:\n" + faltantes.toString(),
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                    } else {
                        mostrarResumen();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Fin de la revisión.");
                    dispose();
                }
            }
        });
    }

    private void mostrarPregunta() {
    grupo.clearSelection();
    Pregunta p = preguntas.get(indiceActual);
    lblPregunta.setText((indiceActual + 1) + ". " + p.getPregunta());

    List<String> alts = p.getAlternativas();
    boolean esVF = p.getTipo().equalsIgnoreCase("Verdadero/Falso");

    for (int i = 0; i < opciones.length; i++) {
        if (esVF && i < 2) {
            opciones[i].setText(i == 0 ? "Verdadero" : "Falso");
            opciones[i].setVisible(true);
        } else if (!esVF && i < alts.size()) {
            String letra = String.valueOf((char) ('a' + i));
            opciones[i].setText(letra + ") " + alts.get(i));
            opciones[i].setVisible(true);
        } else {
            opciones[i].setVisible(false);
        }
    }

    String seleccionPrevia = respuestasUsuario[indiceActual];
    if (seleccionPrevia != null) {
        for (JRadioButton opcion : opciones) {
            if (opcion.isVisible()) {
                String textoPlano = opcion.getText().replaceFirst("^[a-dA-D]\\)\\s*", "")
                        .replace(" ✓", "").replace(" ✗", "");
                if (textoPlano.equalsIgnoreCase(seleccionPrevia)) {
                    opcion.setSelected(true);
                    break;
                }
            }
        }
    }

    if (modoRevision) {
        String correcta = p.getRespuesta_correcta();
        String textoCorrecto = correcta;

        if (!esVF && correcta.length() == 1 && correcta.charAt(0) >= 'a' && correcta.charAt(0) <= 'd') {
            int idx = correcta.charAt(0) - 'a';
            if (idx >= 0 && idx < alts.size()) textoCorrecto = alts.get(idx);
        }

        for (int i = 0; i < opciones.length; i++) {
            JRadioButton opcion = opciones[i];
            if (!opcion.isVisible()) continue;

            opcion.setEnabled(false);
            String textoPlano = opcion.getText().replaceFirst("^[a-dA-D]\\)\\s*", "")
                    .replace(" ✓", "").replace(" ✗", "");
            boolean esCorrecta = textoPlano.equalsIgnoreCase(textoCorrecto);
            boolean esSeleccionada = textoPlano.equalsIgnoreCase(seleccionPrevia);

            if (esSeleccionada) {
                opcion.setSelected(true);
                if (esCorrecta) {
                    opcion.setForeground(new Color(0, 128, 0));
                    if (!opcion.getText().contains("✓")) opcion.setText(opcion.getText() + " ✓");
                } else {
                    opcion.setForeground(Color.RED);
                    if (!opcion.getText().contains("✗")) opcion.setText(opcion.getText() + " ✗");
                }
            } else if (esCorrecta) {
                opcion.setForeground(new Color(0, 128, 0));
                if (!opcion.getText().contains("✓")) opcion.setText(opcion.getText() + " ✓");
            } else {
                opcion.setForeground(Color.WHITE);
            }
        }
    } else {
        for (JRadioButton opcion : opciones) {
            opcion.setEnabled(true);
            opcion.setForeground(Color.WHITE);
        }
    }

    btnAnterior.setEnabled(indiceActual > 0);
    btnSiguiente.setText(indiceActual == preguntas.size() - 1 ? (modoRevision ? "Cerrar" : "Finalizar") : "Siguiente");
}

    private void guardarRespuesta() {
        for (JRadioButton opcion : opciones) {
            if (opcion.isVisible() && opcion.isSelected()) {
                respuestasUsuario[indiceActual] = opcion.getText().replace(" ✓", "").replace(" ✗", "");
                break;
            }
        }
    }

   private void mostrarResumen() {
        Map<String, Integer> totalPorNivel = new HashMap<>();
        Map<String, Integer> aciertosPorNivel = new HashMap<>();
        Map<String, Integer> totalPorTipo = new HashMap<>();
        Map<String, Integer> aciertosPorTipo = new HashMap<>();

        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String nivel = p.getNivel_bloom();
            String tipo = p.getTipo();
            String respuestaUsuario = respuestasUsuario[i];

            totalPorNivel.put(nivel, totalPorNivel.getOrDefault(nivel, 0) + 1);
            totalPorTipo.put(tipo, totalPorTipo.getOrDefault(tipo, 0) + 1);

            if (respuestaUsuario != null && respuestaUsuario.equalsIgnoreCase(p.getRespuesta_correcta())) {
                aciertosPorNivel.put(nivel, aciertosPorNivel.getOrDefault(nivel, 0) + 1);
                aciertosPorTipo.put(tipo, aciertosPorTipo.getOrDefault(tipo, 0) + 1);
            }
        }

        JFrame resumen = new JFrame("Resumen de Evaluación");
        resumen.setSize(600, 600);
        resumen.setLocationRelativeTo(null);
        resumen.setLayout(new BorderLayout());
        resumen.getContentPane().setBackground(Color.BLACK);

        JLabel tituloResumen = new JLabel("RESUMEN DE EVALUACIÓN", JLabel.CENTER);
        tituloResumen.setForeground(Color.WHITE);
        tituloResumen.setFont(new Font("SansSerif", Font.BOLD, 22));
        resumen.add(tituloResumen, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel();
        panelContenido.setBackground(Color.BLACK);
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

        JPanel panelNivel = new JPanel();
        panelNivel.setBackground(Color.BLACK);
        panelNivel.setLayout(new BoxLayout(panelNivel, BoxLayout.Y_AXIS));

        JLabel subtituloNivel = new JLabel("% de acierto por nivel de Bloom:");
        subtituloNivel.setForeground(Color.WHITE);
        subtituloNivel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelNivel.add(subtituloNivel);

        for (String nivel : totalPorNivel.keySet()) {
            int total = totalPorNivel.get(nivel);
            int aciertos = aciertosPorNivel.getOrDefault(nivel, 0);
            int porcentaje = (int) ((aciertos * 100.0) / total);
            JLabel lbl = new JLabel("- " + nivel + ": " + porcentaje + "%");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelNivel.add(lbl);
        }

        JPanel panelTipo = new JPanel();
        panelTipo.setBackground(Color.BLACK);
        panelTipo.setLayout(new BoxLayout(panelTipo, BoxLayout.Y_AXIS));

        JLabel subtituloTipo = new JLabel("% de acierto por tipo de pregunta:");
        subtituloTipo.setForeground(Color.WHITE);
        subtituloTipo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelTipo.add(subtituloTipo);

        for (String tipo : totalPorTipo.keySet()) {
            int total = totalPorTipo.get(tipo);
            int aciertos = aciertosPorTipo.getOrDefault(tipo, 0);
            int porcentaje = (int) ((aciertos * 100.0) / total);
            JLabel lbl = new JLabel("- " + tipo + ": " + porcentaje + "%");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelTipo.add(lbl);
        }

        panelContenido.add(Box.createVerticalStrut(20));
        panelContenido.add(panelNivel);
        panelContenido.add(Box.createVerticalStrut(30));
        panelContenido.add(panelTipo);

        resumen.add(panelContenido, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.BLACK);
        JButton btnRevisar = new JButton("Revisar evaluación");
        JButton btnCerrar = new JButton("Cerrar");

        btnRevisar.setBackground(Color.BLACK);
        btnRevisar.setForeground(Color.CYAN);
        btnRevisar.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnCerrar.setBackground(Color.BLACK);
        btnCerrar.setForeground(Color.CYAN);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));

        panelBotones.add(btnRevisar);
        panelBotones.add(btnCerrar);
        resumen.add(panelBotones, BorderLayout.SOUTH);

        btnCerrar.addActionListener(e -> resumen.dispose());
        btnRevisar.addActionListener(e -> {
            resumen.dispose();
            modoRevision = true;
            indiceActual = 0;
            mostrarPregunta();
            setVisible(true);
        });

        resumen.setVisible(true);
        setVisible(false);
    }
}
