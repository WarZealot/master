/**
 *
 */
package tka.binding.twitter.internal;

import java.util.Set;

import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;

/**
 * @author Konstantin
 *
 */
public class TwitterEventFactory extends AbstractEventFactory {

    public TwitterEventFactory(Set<String> supportedEventTypes) {
        super(supportedEventTypes);

    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        return new TwitterEvent(topic, payload, source);
    }

}
