/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.twitter;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tka.binding.twitter.automation.TwitterModuleHandlerFactory;
import tka.binding.twitter.automation.TwitterModuleTypeProvider;

/**
 * This class is responsible for registering the services.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class Activator implements BundleActivator {

    /**
     * The twitter module type provider.
     */
    private TwitterModuleTypeProvider mtProvider;

    /**
     * The twitter module handler factory.
     */
    private TwitterModuleHandlerFactory handlerFactory;

    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        mtProvider = new TwitterModuleTypeProvider();
        mtProvider.register(context);

        handlerFactory = new TwitterModuleHandlerFactory(context);
        handlerFactory.register(context);

        System.out.println("Registered Twitter Types");
    }

    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        mtProvider.unregister();
        mtProvider = null;

        handlerFactory.unregister();
        handlerFactory = null;
    }

}
