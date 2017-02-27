/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.twitter.automation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.smarthome.automation.Action;
import org.eclipse.smarthome.automation.Module;
import org.eclipse.smarthome.automation.handler.BaseModuleHandlerFactory;
import org.eclipse.smarthome.automation.handler.ModuleHandler;
import org.eclipse.smarthome.automation.handler.ModuleHandlerFactory;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an implementation of the {@link ModuleHandlerFactory}, which is registered as a service. It is used to
 * provide TwitterActionHandlers.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class TwitterModuleHandlerFactory extends BaseModuleHandlerFactory {

    /**
     * The name of the factory.
     */
    public static final String MODULE_HANDLER_FACTORY_NAME = "[TwitterHandlerFactory]";

    /**
     * The supported module types.
     */
    private static final Collection<String> TYPES;

    static {
        List<String> temp = new ArrayList<String>();
        temp.add(TwitterActionType.UID);
        TYPES = Collections.unmodifiableCollection(temp);
    }

    /**
     * The service registration.
     */
    @SuppressWarnings("rawtypes")
    private ServiceRegistration factoryRegistration;

    /**
     * The logger.
     */
    private Logger logger = LoggerFactory.getLogger(TwitterModuleHandlerFactory.class);

    /**
     * The event publisher.
     */
    private EventPublisher eventPublisher;

    /**
     * The item registry.
     */
    private ItemRegistry itemRegistry;

    /**
     * The constructor.
     *
     * @param bc the bundle context.
     */
    public TwitterModuleHandlerFactory(BundleContext bc) {
        activate(bc);

        ServiceReference<EventPublisher> reference3 = bc.getServiceReference(EventPublisher.class);
        eventPublisher = bc.getService(reference3);

        ServiceReference<ItemRegistry> reference4 = bc.getServiceReference(ItemRegistry.class);
        itemRegistry = bc.getService(reference4);
    }

    /**
     * @see org.eclipse.smarthome.automation.handler.ModuleHandlerFactory#getTypes()
     */
    @Override
    public Collection<String> getTypes() {
        return TYPES;
    }

    /**
     * Registers this class as a service.
     *
     * @param bc
     */
    public void register(BundleContext bc) {
        factoryRegistration = bc.registerService(ModuleHandlerFactory.class.getName(), this, null);
    }

    /**
     * Unregisters this service.
     */
    public void unregister() {
        factoryRegistration.unregister();
        factoryRegistration = null;
    }

    /**
     * @see org.eclipse.smarthome.automation.handler.BaseModuleHandlerFactory#internalCreate(org.eclipse.smarthome.automation.Module,
     *      java.lang.String)
     */
    @Override
    protected ModuleHandler internalCreate(Module module, String ruleUID) {
        System.out.println("FlashHandlerFactory.internalCreate() " + module + " " + ruleUID);
        ModuleHandler moduleHandler = null;
        if (TwitterActionType.UID.equals(module.getTypeUID())) {
            moduleHandler = new TwitterActionHandler((Action) module, eventPublisher, itemRegistry);
        } else {
            logger.error(MODULE_HANDLER_FACTORY_NAME + "Not supported moduleHandler: {}", module.getTypeUID());
        }
        return moduleHandler;
    }
}
