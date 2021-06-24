package Client.Event;

import java.util.EventObject;
import Client.Window.MqttWindow;

public class ClientEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    private final MqttWindow source;
    public ClientEvent(MqttWindow source) {
        super(source);
        this.source = source;
    }

    @Override
    public MqttWindow getSource() {
        return this.source;
    }
}
