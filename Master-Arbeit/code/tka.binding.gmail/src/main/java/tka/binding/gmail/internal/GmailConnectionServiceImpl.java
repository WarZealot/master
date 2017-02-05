/**
 *
 */
package tka.binding.gmail.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.service.component.ComponentContext;

import tka.binding.gmail.GmailBindingConstants;
import tka.binding.gmail.GmailConnectionService;

/**
 * @author Konstantin
 *
 */
public class GmailConnectionServiceImpl implements GmailConnectionService {

    private static final Set<String> SUBSCRIBED_EVENT_TYPES = new HashSet<>();
    static {
        SUBSCRIBED_EVENT_TYPES.add(ThingRemovedEvent.TYPE);
    }

    /**
     *
     */
    private ThingRegistry thingRegistry;
    // private EventPublisher eventPublisher;
    // private ComponentContext context;
    // private BundleContext bundleContext;
    // private ServiceRegistration<?> eventFactoryService;
    // private ServiceRegistration<?> thisSubscriberService;

    public void activate(ComponentContext context) {
        System.out.println("GmailConnectionServiceImpl.activate()");
        Thing thing = thingRegistry.get(GmailBindingConstants.GMAIL_CONNECTION);
        if (thing == null) {
            createGmailThing();
        }
    }

    private void createGmailThing() {
        Thing thing = thingRegistry.createThingOfType(GmailBindingConstants.THING_TYPE_GMAIL,
                GmailBindingConstants.GMAIL_CONNECTION, null, "gmailLabel", null);
        thingRegistry.add(thing);
    }

    public void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    public void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }

}
