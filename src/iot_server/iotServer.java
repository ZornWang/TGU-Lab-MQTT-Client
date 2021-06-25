package iot_server;

import iot_server.Event.serverEvent;
import iot_server.Listener.ServerListener;
import iot_server.Window.serverWindow;
import iot_server.util.Message;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

public class iotServer {
    serverWindow window = new serverWindow();
    MqttClient client;

    public static void main(String[] args) {
        new iotServer();
    }

    public iotServer() {
        window.createWindow();
        MqttMessageListener mqttMessageListener = new MqttMessageListener();
        ServerListener serverListener = new ServerListener() {
            @Override
            public void onConnect(serverEvent event) throws IOException {
                String broker = "tcp://localhost:1883";
                String address = window.getAddressInputValue();
                String clientName = window.getClientNameInputValue();

                String topic = "IOTV3-GW/GW{" + clientName + "}";

                try {
                    client = new MqttClient(broker, topic);
                    client.connect();
                    client.subscribe(topic, 2, mqttMessageListener);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        window.addListener(serverListener);
    }

    private class MqttMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("Reply topic: " + topic);
            System.out.println("Reply payload: " + message.toString());
            Message msg = new Message();
            msg.parse(message.getPayload());

            window.addMessage(msg);
        }
    }

}
