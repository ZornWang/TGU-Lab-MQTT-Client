package iot_sensor.Listener;

import iot_sensor.Event.iotEvent;

public interface iotListener {
    void onConnect(iotEvent event) ;
    void onSend(iotEvent event) ;
   // void onSubscribe(iotEvent event);
}
