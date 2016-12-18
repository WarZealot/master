/**
 *
 */
package tka.binding.twitter;

import org.eclipse.smarthome.core.events.EventPublisher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author Konstantin
 *
 *         Currently tracks tweets on twitter.
 */
public class Activator implements BundleActivator {

    private BundleContext context;
    private EventPublisher eventPublisher;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        System.out.println("Twitter Activator running...");

        // event publisher
        ServiceReference<EventPublisher> reference3 = context.getServiceReference(EventPublisher.class);
        eventPublisher = context.getService(reference3);

        // trackTwitter();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context = null;
    }
}
