package microkernel.app;

import javax.swing.SwingUtilities;

//Class de entrada da aplicação, responsável por iniciar a interface gráfica.

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
