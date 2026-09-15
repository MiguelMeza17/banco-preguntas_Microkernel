package microkernel.app;

import microkernel.core.QuestionMicrokernel;
import microkernel.entities.Question;
import microkernel.entities.QuestionRequest;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;

public class MainFrame extends JFrame {

    private final QuestionMicrokernel microkernel = new QuestionMicrokernel();

    private final JTextField txtTitle = new JTextField();
    private final JTextField txtContent = new JTextField();
    private final JComboBox<String> cmbType =
            new JComboBox<>(new String[]{"MULTIPLE_CHOICE", "CASE", "MULTIMEDIA"});
    private final JTextField txtClassification = new JTextField("Arquitectura de software");
    private final JTextField txtOption1 = new JTextField();
    private final JTextField txtOption2 = new JTextField();
    private final JTextField txtOption3 = new JTextField();
    private final JTextField txtOption4 = new JTextField();
    private final JTextField txtCorrect = new JTextField();
    private final JTextField txtResource = new JTextField();
    private final JTextArea txtOutput = new JTextArea(12, 40);

    public MainFrame() {
        setTitle("Banco de Preguntas Saber PRO - Microkernel + Tuberias y Filtros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(buildForm(), BorderLayout.CENTER);
        add(buildOutput(), BorderLayout.SOUTH);

        cmbType.addActionListener(e -> updateFieldsForType());
        updateFieldsForType();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(new JLabel("Titulo:"));
        form.add(txtTitle);
        form.add(new JLabel("Contenido / enunciado:"));
        form.add(txtContent);
        form.add(new JLabel("Tipo (plugin):"));
        form.add(cmbType);
        form.add(new JLabel("Clasificacion:"));
        form.add(txtClassification);
        form.add(new JLabel("Opcion 1:"));
        form.add(txtOption1);
        form.add(new JLabel("Opcion 2:"));
        form.add(txtOption2);
        form.add(new JLabel("Opcion 3:"));
        form.add(txtOption3);
        form.add(new JLabel("Opcion 4:"));
        form.add(txtOption4);
        form.add(new JLabel("Respuesta correcta:"));
        form.add(txtCorrect);
        form.add(new JLabel("Recurso multimedia (URL/ruta):"));
        form.add(txtResource);

        JButton btnGenerate = new JButton("Generar pregunta");
        btnGenerate.addActionListener(e -> onGenerate());
        JButton btnList = new JButton("Ver banco de preguntas");
        btnList.addActionListener(e -> listQuestions());

        JPanel buttons = new JPanel();
        buttons.add(btnGenerate);
        buttons.add(btnList);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttons, BorderLayout.SOUTH);
        return wrapper;
    }

    private JScrollPane buildOutput() {
        txtOutput.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtOutput);
        scroll.setPreferredSize(new Dimension(560, 220));
        scroll.setBorder(BorderFactory.createTitledBorder("Salida"));
        return scroll;
    }

    private void updateFieldsForType() {
        String type = (String) cmbType.getSelectedItem();
        boolean esSeleccionMultiple = "MULTIPLE_CHOICE".equals(type);
        boolean esMultimedia = "MULTIMEDIA".equals(type);

        setEnabledField(txtOption1, esSeleccionMultiple);
        setEnabledField(txtOption2, esSeleccionMultiple);
        setEnabledField(txtOption3, esSeleccionMultiple);
        setEnabledField(txtOption4, esSeleccionMultiple);
        setEnabledField(txtCorrect, esSeleccionMultiple);

        setEnabledField(txtResource, esMultimedia);

        clearForm();
    }

    private void clearForm() {
        txtTitle.setText("");
        txtContent.setText("");
        txtClassification.setText("Arquitectura de software");
        txtOption1.setText("");
        txtOption2.setText("");
        txtOption3.setText("");
        txtOption4.setText("");
        txtCorrect.setText("");
        txtResource.setText("");
    }

    private void setEnabledField(JComponent field, boolean enabled) {
        field.setEnabled(enabled);
        field.setBackground(enabled ? Color.WHITE : new Color(235, 235, 235));
    }

    private void onGenerate() {
        String type = (String) cmbType.getSelectedItem();

        String content = txtContent.getText();
        if ("MULTIMEDIA".equals(type) && !txtResource.getText().trim().isEmpty()) {
            content = content + " [Recurso: " + txtResource.getText().trim() + "]";
        }

        QuestionRequest request = new QuestionRequest(
                txtTitle.getText(),
                content,
                type,
                txtClassification.getText(),
                Arrays.asList(
                        txtOption1.getText(),
                        txtOption2.getText(),
                        txtOption3.getText(),
                        txtOption4.getText()),
                txtCorrect.getText()
        );

        try {
            Question question = microkernel.executePlugin(type, request);
            if (question != null) {
                append("OK - Pregunta generada y almacenada:");
                append("   " + question);
                clearForm();
            } else {
                append("RECHAZADA - La pregunta no paso el pipeline de validacion.");
                append("   Revise: 4 opciones no vacias ni duplicadas, clasificacion valida,");
                append("   contenido con longitud minima y respuesta correcta dentro de las opciones.");
            }
        } catch (IllegalArgumentException ex) {
            append("ERROR - " + ex.getMessage());
        }
        append("");
    }

    private void listQuestions() {
        append("===== Banco de preguntas (" + microkernel.getQuestions().size() + ") =====");
        if (microkernel.getQuestions().isEmpty()) {
            append("   (vacio)");
        } else {
            for (Question q : microkernel.getQuestions().values()) {
                append("   " + q);
            }
        }
        append("");
    }

    private void append(String line) {
        txtOutput.append(line + System.lineSeparator());
        txtOutput.setCaretPosition(txtOutput.getDocument().getLength());
    }
}
