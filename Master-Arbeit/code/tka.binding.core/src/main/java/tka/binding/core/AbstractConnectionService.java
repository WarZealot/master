/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.core;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;

/**
 * This class is an abstract implementation of the {@link ConnectionService}. It offers some generally useful
 * functionality, to save work in the subclasses.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public abstract class AbstractConnectionService implements ConnectionService, EventSubscriber {

    /**
     * Subscribing only to {@link ThingRemovedEvent} so that the extending ConnectionService can react properly.
     */
    protected static final Set<String> SUBSCRIBED_EVENT_TYPES = new HashSet<>();
    static {
        SUBSCRIBED_EVENT_TYPES.add(ThingRemovedEvent.TYPE);
    }

    /**
     * The thing registry.
     */
    protected ThingRegistry thingRegistry;

    /**
     * The event publisher.
     */
    protected EventPublisher eventPublisher;

    /**
     * The component context.
     */
    protected ComponentContext context;

    /**
     * The bundle context.
     */
    protected BundleContext bundleContext;

    /**
     * Own service registration as an event subscriber.
     */
    protected ServiceRegistration<?> subscriberServiceRegistration;

    /**
     * This method is automatically called when the bundle starts and this component is activated.
     *
     * @param context
     */
    public void activate(ComponentContext context) {
        this.context = context;
        this.bundleContext = context.getBundleContext();
        regsiterServices();
    }

    /**
     * Registers services.
     */
    protected void regsiterServices() {
        subscriberServiceRegistration = bundleContext.registerService(EventSubscriber.class.getName(), this, null);
    }

    /**
     * This method is automatically called when the bundle stops and this component is deactivated..
     *
     * @param context
     */
    public void deactivate(ComponentContext context) {
        if (subscriberServiceRegistration != null) {
            subscriberServiceRegistration.unregister();
        }

        this.bundleContext = null;
        this.context = null;
    }

    /**
     * Sets the thing registry.
     *
     * @param thingRegistry
     */
    public void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    /**
     * Unsets the thing registry.
     *
     * @param thingRegistry
     */
    public void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }

    /**
     * Sets the event publisher.
     *
     * @param eventPublisher
     */
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Unsets the event publisher.
     *
     * @param eventPublisher
     */
    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    /**
     * @see org.eclipse.smarthome.core.events.EventSubscriber#getSubscribedEventTypes()
     */
    @Override
    public Set<String> getSubscribedEventTypes() {
        return SUBSCRIBED_EVENT_TYPES;
    }

    @Override
    public EventFilter getEventFilter() {
        return null;
    }
}
