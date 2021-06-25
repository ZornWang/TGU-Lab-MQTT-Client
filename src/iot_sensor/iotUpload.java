package iot_sensor;

import iot_sensor.Event.iotEvent;
import iot_sensor.Sensor.TempSensor;
import iot_sensor.Window.iotWindow;
import iot_sensor.Listener.iotListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class iotUpload {
    iotWindow window = new iotWindow();
    MqttClient client;
    TempSensor tempSensor;
    MqttConnectOptions Options = new MqttConnectOptions();

    public static void main(String[] args) {
        new iotUpload();
    }

    public iotUpload() {
        window.createWindow();

        iotListener Listener = new iotListener() {
            @Override
            public void onConnect(iotEvent event) {
                String borker = "tcp://127.0.0.1:1883";
                String routeMac = window.getRouteMacInputValue();
                String NodeMac = window.getNodeMacInputValue();
                Integer connectType = Integer.parseInt(window.getConnectType());
                Options.setCleanSession(true);
                try {
                    client = new MqttClient(borker, NodeMac);
                    client.connect(Options);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

                window.setAllowSend(true);

                tempSensor = new TempSensor(client, routeMac, connectType, NodeMac);
            }

            @Override
            public void onSend(iotEvent event) {
                String tempValue = new String(window.getTempInputValue());
                float value = Float.parseFloat(tempValue);
                tempSensor.upload(value);
            }
        };
        window.addListener(Listener);
    }
}
