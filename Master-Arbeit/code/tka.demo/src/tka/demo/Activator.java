package tka.demo;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import tka.binding.atomclock.AtomclockBindingConstants;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        System.out.println("Activator.start()");

        doSomething();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

    private void doSomething() {
        ServiceReference<ThingRegistry> reference = context.getServiceReference(ThingRegistry.class);
        ThingRegistry registry = context.getService(reference);
        System.out.println("Registry: " + registry);

        Configuration configuration = new Configuration();
        Thing thing = registry.createThingOfType(AtomclockBindingConstants.THING_TYPE_ATOMCLOCK,
                new ThingUID("atomclock", "atomclock", "myclock"), null, "someLabel", configuration);
        System.out.println("Thing: " + thing);

    }

}
