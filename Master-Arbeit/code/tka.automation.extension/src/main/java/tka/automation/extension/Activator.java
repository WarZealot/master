/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tka.automation.extension.handler.FlashHandlerFactory;
import tka.automation.extension.type.FlashModuleTypeProvider;

/**
 * This class is responsible for starting and stopping the application that gives ability to the user to switch on the
 * air conditioner and lights in its home remotely. It initializes and registers or unregisters the
 * services that provide this functionality - Rule Provider, Rule Template Provider, Module Type Provider and Handler
 * Factory for handlers of the modules that compose the rules. Of course, these providers are not mandatory for each
 * application. Some applications may contain only Template Provider or Rule Provider, or Module Type Provider, or
 * Module Handler Factory for some particular module types. Also, to enable the user to have control over the settings
 * and to enforce execution, the demo initializes and registers a service that provides console commands.
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class Activator implements BundleActivator {

    private FlashModuleTypeProvider mtProvider;
    private FlashHandlerFactory handlerFactory;

    @Override
    public void start(BundleContext context) throws Exception {
        mtProvider = new FlashModuleTypeProvider();
        mtProvider.register(context);

        handlerFactory = new FlashHandlerFactory(context);
        handlerFactory.register(context);

        System.out.println("Registered FLASH Types");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        mtProvider.unregister();
        mtProvider = null;

        handlerFactory.unregister();
        handlerFactory = null;
    }

}
