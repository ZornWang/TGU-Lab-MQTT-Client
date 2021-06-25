package iot_server.Event;

import iot_sensor.Window.iotWindow;
import iot_server.Window.serverWindow;

import java.util.EventObject;

public class serverEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    private final serverWindow source;
    public serverEvent(serverWindow source) {
        super(source);
        this.source = source;
    }

    @Override
    public serverWindow getSource() {
        return this.source;
    }
}
