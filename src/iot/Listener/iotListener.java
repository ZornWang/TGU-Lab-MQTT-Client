package iot.Listener;

import iot.Event.iotEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface iotListener {
    void onConnect(iotEvent event) ;
    void onSend(iotEvent event) ;
   // void onSubscribe(iotEvent event);
}
