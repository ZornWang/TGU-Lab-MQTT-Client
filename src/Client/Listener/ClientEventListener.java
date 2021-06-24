package Client.Listener;

import Client.Event.ClientEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public interface ClientEventListener {
    void onConnect(ClientEvent event) throws IOException;
    void onDisConnect(ClientEvent event) throws MqttException;
    void onSend(ClientEvent event);
    void onSubscribe(ClientEvent event);
}
