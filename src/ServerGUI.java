import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
    private int clientCounter = 0;

    public ServerGUI(String address, int port) {
        addressField.setText(address);
        portField.setText(String.valueOf(port));

        WindowListener closeListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                updateLog("INFO: Initiating server shutdown...");
                ServerDictionary dictionary = ServerDictionary.getInstance();
                if (dictionary.getChangeCounter() > 0) {
                    dictionary.saveDictionaryToFile();
                }
                System.exit(0);
            }
        };
        clearButton.addActionListener(e -> logArea.setText(""));

        JFrame frame = new JFrame("Dictionary Server");
        frame.setContentPane(rootPanel);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(closeListener);
        frame.pack();
        frame.setVisible(true);
    }

    public void updateLog(String message) {
        logArea.append("[" + dateFormat.format(new Date()) + "] " + message + "\n");
    }

    public void incrementClientCounter() {
        clientCounter++;
        numClientsLabel.setText(String.valueOf(clientCounter));
    }

    public void decrementClientCounter() {
        clientCounter--;
        numClientsLabel.setText(String.valueOf(clientCounter));
    }

    public int getClientCount() {
        return clientCounter;
    }

    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
