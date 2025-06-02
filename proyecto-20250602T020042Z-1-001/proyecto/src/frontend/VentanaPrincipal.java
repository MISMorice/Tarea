package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class VentanaPrincipal extends JFrame {
    private JButton btnCargarArchivo;
    private JButton btnRealizarPrueba;
    private String rutaArchivoCargado;

    public VentanaPrincipal() {
        setTitle("Sistema de Evaluación – Taxonomía de Bloom");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.BLACK);

        JLabel titulo = new JLabel("SISTEMA DE PRUEBAS EDUCATIVAS", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(Color.BLACK);

        btnCargarArchivo = new JButton("CARGAR ARCHIVO");
        btnRealizarPrueba = new JButton("REALIZAR PRUEBA");
        btnRealizarPrueba.setEnabled(false);

        configurarBotonEstiloFinal(btnCargarArchivo, new Color(0, 191, 255), Color.BLACK);
        configurarBotonEstiloFinal(btnRealizarPrueba, new Color(190, 174, 255), Color.BLACK);

        panelBotones.add(btnCargarArchivo);
        panelBotones.add(Box.createVerticalStrut(20));
        panelBotones.add(btnRealizarPrueba);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        add(panelPrincipal);

        btnCargarArchivo.addActionListener(e -> {
            FileDialog dialogoArchivo = new FileDialog(this, "Selecciona el archivo de preguntas", FileDialog.LOAD);
            dialogoArchivo.setDirectory(System.getProperty("user.home"));
            dialogoArchivo.setVisible(true);

            String archivo = dialogoArchivo.getFile();
            String directorio = dialogoArchivo.getDirectory();

            if (archivo != null) {
                File archivoSeleccionado = new File(directorio, archivo);

                try {
                    File carpetaRecursos = new File("recursos");
                    if (!carpetaRecursos.exists()) {
                        carpetaRecursos.mkdirs();
                    }

                    File archivoDestino = new File(carpetaRecursos, "prueba.json");
                    Files.copy(archivoSeleccionado.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    rutaArchivoCargado = archivoDestino.getAbsolutePath();
                    JOptionPane.showMessageDialog(this, "Archivo cargado correctamente:\n" + rutaArchivoCargado);
                    btnRealizarPrueba.setEnabled(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al copiar el archivo:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRealizarPrueba.addActionListener(e -> new VentanaPrueba(rutaArchivoCargado).setVisible(true));
    }

    private void configurarBotonEstiloFinal(JButton boton, Color fondo, Color texto) {
    boton.setBackground(fondo);
    boton.setForeground(texto);
    boton.setFocusPainted(false);
    boton.setFont(new Font("SansSerif", Font.BOLD, 18));
    boton.setPreferredSize(new Dimension(260, 50));
    boton.setMaximumSize(new Dimension(260, 50));
    boton.setAlignmentX(Component.CENTER_ALIGNMENT);
    boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    boton.setContentAreaFilled(true);
    boton.setOpaque(true);
}

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
