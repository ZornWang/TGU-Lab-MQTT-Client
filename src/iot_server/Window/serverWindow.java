package iot_server.Window;

import iot_server.Event.serverEvent;
import iot_server.Listener.ServerListener;
import iot_server.util.Message;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class serverWindow {
    private static final List<ServerListener> LISTENERS = new ArrayList<>();

    private JTextField IPAddress;
    private JTextField Prot;
    private JTextField ClientName;
    private JButton buttonConnect;
    private JTextPane TextPane;
    private JPanel main;

    public void createWindow() {
        JFrame frame = new JFrame("体温传感器接收端 王准衡 1911630215");
        frame.setContentPane(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        bindActions();
    }

    public void addListener(ServerListener e) {
        LISTENERS.add(e);
    }

    public void removeListener(ServerListener e) {
        LISTENERS.remove(e);
    }

    public String getAddressInputValue() {
        return IPAddress.getText();
    }

    public String getPortInputValue() {
        return Prot.getText();
    }

    public String getClientNameInputValue() {
        return ClientName.getText();
    }

    private void bindActions() {
        buttonConnect.addActionListener(e -> {
            try {
                handleConnect();
            } catch (IOException | MqttException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private void handleConnect() throws IOException, MqttException {
        for (ServerListener listener : LISTENERS) {
            listener.onConnect(new serverEvent(this));
        }
    }

    public void addMessage(Message message) {
        StyledDocument doc = TextPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), message.getTemperature() + "°C" +"\n\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
