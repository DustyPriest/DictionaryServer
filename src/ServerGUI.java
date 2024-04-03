import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerGUI implements SimpleLogger {
    private JTextField addressField;
    private JTextField portField;
    private JTextArea logArea;
    private JButton clearButton;
    private JPanel rootPanel;
    private JLabel numClientsLabel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ServerGUI() {

        clearButton.addActionListener(e -> logArea.setText(""));

        JFrame frame = new JFrame("Dictionary Server");
        frame.setContentPane(rootPanel);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void updateLog(String message) {
        logArea.append("[" + dateFormat.format(new Date()) + "] " + message + "\n");
    }
}
