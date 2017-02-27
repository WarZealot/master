/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.dropbox;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tka.binding.dropbox.automation.DropboxModuleHandlerFactory;
import tka.binding.dropbox.automation.DropboxModuleTypeProvider;

/**
 * This class is responsible for registering the services.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class Activator implements BundleActivator {

    /**
     * The dropbox module type provider.
     */
    private DropboxModuleTypeProvider mtProvider;

    /**
     * The dropbox module handler factory.
     */
    private DropboxModuleHandlerFactory handlerFactory;

    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        mtProvider = new DropboxModuleTypeProvider();
        mtProvider.register(context);

        handlerFactory = new DropboxModuleHandlerFactory(context);
        handlerFactory.register(context);

        System.out.println("Registered Dropbox Types");
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
