/**
 *
 */
package tka.binding.core;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;

/**
 * @author Konstantin
 *
 */
public abstract class AbstractConnectionService implements ConnectionService, EventSubscriber {

    protected static final Set<String> SUBSCRIBED_EVENT_TYPES = new HashSet<>();
    static {
        SUBSCRIBED_EVENT_TYPES.add(ThingRemovedEvent.TYPE);
    }
    /**
     *
     */
    protected ThingRegistry thingRegistry;
    protected EventPublisher eventPublisher;
    protected ComponentContext context;
    protected BundleContext bundleContext;
    protected ServiceRegistration<?> subscriberServiceRegistration;

    public void activate(ComponentContext context) {
        this.context = context;
        this.bundleContext = context.getBundleContext();
        regsiterServices();
    }

    private void regsiterServices() {
        subscriberServiceRegistration = bundleContext.registerService(EventSubscriber.class.getName(), this, null);
    }

    public void deactivate(ComponentContext context) {
        if (subscriberServiceRegistration != null) {
            subscriberServiceRegistration.unregister();
        }

        this.bundleContext = null;
        this.context = null;
    }

    public void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    public void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return SUBSCRIBED_EVENT_TYPES;
    }
}
