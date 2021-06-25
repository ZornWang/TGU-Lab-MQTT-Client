package iot_sensor.Event;

import java.util.EventObject;
import iot_sensor.Window.iotWindow;

public class iotEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    private final iotWindow source;
    public iotEvent(iotWindow source) {
        super(source);
        this.source = source;
    }

    @Override
    public iotWindow getSource() {
        return this.source;
    }
}
