package tka.demo;

import org.eclipse.smarthome.core.events.AbstractEvent;

public class MyEvent extends AbstractEvent {

    public static final String TYPE = MyEvent.class.getSimpleName();

    public MyEvent(String topic, String payload, String source) {
        super(topic, payload, source);
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
