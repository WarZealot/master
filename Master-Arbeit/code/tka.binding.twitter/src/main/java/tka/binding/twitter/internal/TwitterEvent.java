package tka.binding.twitter.internal;

import org.eclipse.smarthome.core.events.AbstractEvent;

public class TwitterEvent extends AbstractEvent {

    public static final String TYPE = TwitterEvent.class.getSimpleName();

    public TwitterEvent(String topic, String payload, String source) {
        super(topic, payload, source);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
