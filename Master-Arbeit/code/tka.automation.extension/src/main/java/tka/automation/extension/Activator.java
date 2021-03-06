/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension;

import org.eclipse.smarthome.core.events.EventFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import tka.automation.extension.handler.FlashEventFactory;

/**
 * This class is responsible for registering the services.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class Activator implements BundleActivator {

    /**
     * The flash event factory.
     */
    private FlashEventFactory flashEventFactory;

    /**
     * The service registration.
     */
    private ServiceRegistration<?> eventFactoryService;

    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {

        flashEventFactory = new FlashEventFactory();
        eventFactoryService = context.registerService(EventFactory.class.getName(), flashEventFactory, null);

        System.out.println("Registered FLASH Types");
    }

    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {

        if (eventFactoryService != null) {
            eventFactoryService.unregister();
        }
    }

}
