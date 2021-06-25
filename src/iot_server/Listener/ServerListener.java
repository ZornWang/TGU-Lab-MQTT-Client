package iot_server.Listener;

import iot_server.Event.serverEvent;

import java.io.IOException;

public interface ServerListener {
    void onConnect(serverEvent event) throws IOException;
}
