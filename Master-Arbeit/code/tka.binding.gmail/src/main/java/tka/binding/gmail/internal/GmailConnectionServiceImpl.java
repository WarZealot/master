/**
 *
 */
package tka.binding.gmail.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.service.component.ComponentContext;

import tka.binding.core.AbstractConnectionService;
import tka.binding.gmail.GmailBindingConstants;
import tka.binding.gmail.GmailConnectionService;

/**
 * @author Konstantin
 *
 */
public class GmailConnectionServiceImpl extends AbstractConnectionService implements GmailConnectionService {

    private static final ThingUID GMAIL_CONNECTION = new ThingUID("gmail", "gmailThingTypeId", "gmailconnection");
    private static final Set<String> SUBSCRIBED_EVENT_TYPES = new HashSet<>();
    static {
        SUBSCRIBED_EVENT_TYPES.add(ThingRemovedEvent.TYPE);
    }

    /**
     *
     */
    // private ThingRegistry thingRegistry;
    // private EventPublisher eventPublisher;
    // private ComponentContext context;
    // private BundleContext bundleContext;
    // private ServiceRegistration<?> eventFactoryService;
    // private ServiceRegistration<?> thisSubscriberService;

    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
        System.out.println("GmailConnectionServiceImpl.activate()");
        // this.context = context;
        // this.bundleContext = context.getBundleContext();
        //
        // regsiterServices();
        //
        // Thing gmailConnection = thingRegistry.get(GMAIL_CONNECTION);
        // if (gmailConnection != null) {
        // Configuration properties = gmailConnection.getConfiguration();
        // String token = (String) properties.get(GmailBindingConstants.KEY_OAUTH_TOKEN);
        // String tokenSecret = (String) properties.get(GmailBindingConstants.KEY_OAUTH_TOKEN_SECRET);
        //
        // createGmailStream();
        // return;
        // }
    }

    // private void regsiterServices() {
    // thisSubscriberService = bundleContext.registerService(EventSubscriber.class.getName(), this, null);
    // }

    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
        // if (eventFactoryService != null) {
        // eventFactoryService.unregister();
        // }
        // if (thisSubscriberService != null) {
        // thisSubscriberService.unregister();
        // }

        // this.bundleContext = null;
        // this.context = null;
    }

    private void createGmailThing() {

        Thing thing = thingRegistry.createThingOfType(GmailBindingConstants.THING_TYPE_GMAIL, GMAIL_CONNECTION, null,
                "gmailLabel", null);
        thingRegistry.add(thing);

    }

    // @Override
    // public void setThingRegistry(ThingRegistry thingRegistry) {
    // this.thingRegistry = thingRegistry;
    // }
    //
    // @Override
    // public void unsetThingRegistry(ThingRegistry thingRegistry) {
    // this.thingRegistry = null;
    // }
    //
    // @Override
    // public void setEventPublisher(EventPublisher eventPublisher) {
    // this.eventPublisher = eventPublisher;
    // }
    //
    // @Override
    // public void unsetEventPublisher(EventPublisher eventPublisher) {
    // this.eventPublisher = null;
    // }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return SUBSCRIBED_EVENT_TYPES;
    }

    @Override
    public EventFilter getEventFilter() {
        return null;
    }

    @Override
    public void receive(Event event) {
        ThingRemovedEvent e = (ThingRemovedEvent) event;
        if (e.getThing().thingTypeUID.equals(GmailBindingConstants.THING_TYPE_GMAIL.toString())) {
            // do something
        }
    }

    @Override
    public Object requestAuthorization() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean authorizationGrantedCallback(Object information) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAuthorized() {
        // TODO Auto-generated method stub
        return false;
    }

}
