package Client;

import Client.Event.ClientEvent;
import Client.Listener.ClientEventListener;
import Client.Others.Message;
import Client.Window.MqttWindow;
import org.eclipse.paho.client.mqttv3.*;

import javax.swing.text.BadLocationException;
import java.io.IOException;

public class MyMqttClient {
    MqttWindow window = new MqttWindow();
    MqttClient client;
    MqttConnectOptions Options = new MqttConnectOptions();

    public static void main(String[] args) {
        new MyMqttClient();
    }

    public MyMqttClient() {
        window.createWindow();

        MqttMessageListener mqttMessageListener = new MqttMessageListener();
        ClientEventListener clientEventListener = new ClientEventListener() {
            @Override
            public void onConnect(ClientEvent event) throws IOException {
                String broker = "tcp://" + window.getAddressInputValue() + ":" + window.getPortInputValue();
                String clientName = window.getClientIDInputValue();
                Options.setCleanSession(window.getCleanSessionValue());
                Options.setUserName(window.getUsernameInputValue());
                Options.setPassword(window.getPasswordInputValue());

                try {
                    client = new MqttClient(broker, clientName);
                    client.connect(Options);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                window.setAllowSend(true);
                window.setAllowConnect(false);
                window.setAllowDisonnect(true);
            }

            @Override
            public void onDisConnect(ClientEvent event) throws MqttException {
                client.disconnect();
                window.setAllowDisonnect(false);
                window.setAllowConnect(true);
            }

            @Override
            public void onSend(ClientEvent event) {
                int qos = Integer.parseInt(window.getMessageQoS());
                String topic = window.getTopicInputValue();
                String content = window.getMessageInputValue();
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                message.setRetained(window.getRetainValue());
                try {
                    client.publish(topic, message);
                    Message msg = new Message();
                    msg.setType(1); //发的topic
                    msg.setTopic(topic);
                    msg.setContent(message.toString());
                    window.addMessage(msg);
                } catch (MqttException | BadLocationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSubscribe(ClientEvent event) {
                int qos = Integer.parseInt(window.getTopicQoS());
                String topic = window.getTopicInputValue();
                try {
                    client.subscribe(topic, qos, mqttMessageListener);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        window.addListener(clientEventListener);
    }

    private class MqttMessageListener implements IMqttMessageListener {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("Reply topic: " + topic);
            System.out.println("Reply payload: " + message.toString());

            Message msg = new Message();
            msg.setType(2); //收的topic
            msg.setTopic(topic);
            msg.setContent(message.toString());

            try {
                window.addMessage(msg);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

}
