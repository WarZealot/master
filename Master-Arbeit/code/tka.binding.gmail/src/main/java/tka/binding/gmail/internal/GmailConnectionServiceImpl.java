/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.gmail.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.service.component.ComponentContext;

import tka.binding.core.AbstractConnectionService;
import tka.binding.gmail.GmailBindingConstants;
import tka.binding.gmail.GmailConnectionService;

/**
 * This class is responsible for maintaining the {@link GmailBindingConstants#GMAIL_CONNECTION} things.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class GmailConnectionServiceImpl extends AbstractConnectionService implements GmailConnectionService {

    /**
     * The supported event types.
     */
    private static final Set<String> SUBSCRIBED_EVENT_TYPES = new HashSet<>();
    static {
        SUBSCRIBED_EVENT_TYPES.add(ThingRemovedEvent.TYPE);
    }

    /**
     * The thing registry.
     */
    private ThingRegistry thingRegistry;

    /**
     * @see tka.binding.core.AbstractConnectionService#activate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public void activate(ComponentContext context) {
        System.out.println("GmailConnectionServiceImpl.activate()");
        Thing thing = thingRegistry.get(GmailBindingConstants.GMAIL_CONNECTION);
        if (thing == null) {
            createGmailThing();
        }
    }

    /**
     * Creates a thing of the type {@link GmailBindingConstants#GMAIL_CONNECTION}
     */
    private void createGmailThing() {
        Thing thing = thingRegistry.createThingOfType(GmailBindingConstants.THING_TYPE_GMAIL,
                GmailBindingConstants.GMAIL_CONNECTION, null, "gmailLabel", null);
        thingRegistry.add(thing);
    }

    /**
     * @see tka.binding.core.AbstractConnectionService#setThingRegistry(org.eclipse.smarthome.core.thing.ThingRegistry)
     */
    @Override
    public void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    /**
     * @see tka.binding.core.AbstractConnectionService#unsetThingRegistry(org.eclipse.smarthome.core.thing.ThingRegistry)
     */
    @Override
    public void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }

    /**
     * @see tka.binding.core.ConnectionService#isAuthorized()
     */
    @Override
    public boolean isAuthorized() {
        return true;
    }

    /**
     * @see tka.binding.core.ConnectionService#requestAuthorization()
     */
    @Override
    public Object requestAuthorization() {
        return null;
    }

    /**
     * @see tka.binding.core.ConnectionService#authorizationGrantedCallback(java.lang.Object)
     */
    @Override
    public boolean authorizationGrantedCallback(Object information) {
        return false;
    }

    /**
     * @see org.eclipse.smarthome.core.events.EventSubscriber#receive(org.eclipse.smarthome.core.events.Event)
     */
    @Override
    public void receive(Event event) {
    }

}
