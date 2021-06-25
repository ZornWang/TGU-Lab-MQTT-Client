package iot_sensor.Window;

import iot_sensor.Event.iotEvent;
import iot_sensor.Listener.iotListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class iotWindow {
    private static final List<iotListener> LISTENERS = new ArrayList<>();
    private JTextField RouteMac;
    private JTextField NodeMac;
    private JTextField Temp;
    private JButton UploadButton;
    private JComboBox comboBoxConntactType;
    private JButton ConnectButton;
    private JPanel main;

    public void createWindow() {
        JFrame frame = new JFrame("温度传感器 王准衡 1911630215");
        frame.setContentPane(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        bindActions();
    }

    private void bindActions() {
        ConnectButton.addActionListener(e -> handleConnect());
        UploadButton.addActionListener(e -> handleSend());
    }

    private void handleSend() {
        for (iotListener listener : LISTENERS) {
            listener.onSend(new iotEvent(this));
        }
    }

    private void handleConnect() {
        for (iotListener listener : LISTENERS) {
            listener.onConnect(new iotEvent(this));
        }
    }

    public void addListener(iotListener e) {
        LISTENERS.add(e);
    }

    public void removeListener(iotListener e) {
        LISTENERS.remove(e);
    }

    public String getRouteMacInputValue() {
        return RouteMac.getText();
    }

    public String getNodeMacInputValue() {
        return NodeMac.getText();
    }

    public String getConnectType() {
        return (String) comboBoxConntactType.getSelectedItem();
    }

    public String getTempInputValue() {
        return Temp.getText();
    }

    public void setAllowSend(boolean value) {
        UploadButton.setEnabled(value);
    }
}