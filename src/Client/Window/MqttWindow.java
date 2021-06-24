package Client.Window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Client.Event.ClientEvent;
import Client.Listener.ClientEventListener;
import Client.Others.Message;
import org.eclipse.paho.client.mqttv3.MqttException;

import static Client.util.Time.getTime;


public class MqttWindow {
    private static final List<ClientEventListener> LISTENERS = new ArrayList<>();

    private JPanel mainJPanel;
    private JTextField IPAddress;
    private JTextField Port;
    private JTextField ClientID;
    private JButton buttonConnect;
    private JTextField Topic;
    private JButton buttonSubscribe;
    private JTextPane textPane1;
    private JTextField Message;
    private JButton buttonSend;
    private JComboBox TopicQoS;
    private JComboBox MessQoS;
    private JRadioButton cleanSessionRadioButton;
    private JRadioButton retainRadioButton;
    private JButton buttonDisconnect;

    public void createWindow() {
        JFrame frame = new JFrame("MQTT 客户端 王准衡 1911630215");
        frame.setContentPane(mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        bindActions();
    }

    public void addListener(ClientEventListener e) {
        LISTENERS.add(e);
    }

    public void removeListener(ClientEventListener e) {
        LISTENERS.remove(e);
    }

    public String getAddressInputValue() {
        return IPAddress.getText();
    }

    public String getPortInputValue() {
        return Port.getText();
    }

    public String getClientIDInputValue() {
        return ClientID.getText();
    }

    public boolean getRetainValue() {
        return retainRadioButton.isSelected();
    }

    public boolean getCleanSessionValue() {
        return cleanSessionRadioButton.isSelected();
    }

    public String getMessageInputValue() {
        return Message.getText();
    }

    public String getTopicInputValue() {
        return Topic.getText();
    }

    public String getTopicQoS() {
        return (String) TopicQoS.getSelectedItem();
    }

    public String getMessageQoS() {
        return (String) MessQoS.getSelectedItem();
    }

    public void setAllowSend(boolean value) {
        buttonSend.setEnabled(value);
        buttonSubscribe.setEnabled(value);
    }

    public void setAllowConnect(boolean value) {
        buttonConnect.setEnabled(value);
    }

    public void setAllowDisonnect(boolean value) {
        buttonDisconnect.setEnabled(value);
    }


    public void addMessage(Message msg) throws BadLocationException {
        StyledDocument doc = textPane1.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        if (msg.getType() == 1) {
            //发的
            StyleConstants.setForeground(keyWord, Color.blue);
        } else {
            //收的
            StyleConstants.setForeground(keyWord, Color.ORANGE);
        }
        StyleConstants.setBold(keyWord, true);
        synchronized (this) {
            doc.insertString(doc.getLength(), getTime() + " Topic: " + msg.getTopic() + "\n", keyWord);
            doc.insertString(doc.getLength(), msg.getContent() + "\n\n", null);
        }
    }


    private void bindActions() {
        buttonConnect.addActionListener(e -> {
            try {
                handleConnect();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonDisconnect.addActionListener(e -> {
            try {
                handleDisConnect();
            } catch (MqttException mqttException) {
                mqttException.printStackTrace();
            }
        });
        buttonSend.addActionListener(e -> handleSend());
        buttonSubscribe.addActionListener(e -> handleSubscribe());
    }

    private void handleConnect() throws IOException {
        for (ClientEventListener listener : LISTENERS) {
            listener.onConnect(new ClientEvent(this));
        }
    }

    private void handleDisConnect() throws MqttException {
        for (ClientEventListener listener : LISTENERS) {
            listener.onDisConnect(new ClientEvent(this));
        }
    }

    private void handleSend() {
        for (ClientEventListener listener : LISTENERS) {
            listener.onSend(new ClientEvent(this));
        }
    }

    private void handleSubscribe() {
        for (ClientEventListener listener : LISTENERS) {
            listener.onSubscribe(new ClientEvent(this));
        }
    }
}
