/**
 *
 */
package tka.demo;

import java.util.Set;

import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;

/**
 * @author Konstantin
 *
 */
public class MyDemoEventFactory extends AbstractEventFactory {

    public MyDemoEventFactory(Set<String> supportedEventTypes) {
        super(supportedEventTypes);

    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        return new MyEvent(topic, payload, source);
    }

}
