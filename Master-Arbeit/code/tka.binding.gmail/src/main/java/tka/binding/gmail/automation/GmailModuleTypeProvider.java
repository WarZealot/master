/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.gmail.automation;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.eclipse.smarthome.automation.type.ModuleType;
import org.eclipse.smarthome.automation.type.ModuleTypeProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The module type provider for this binding.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class GmailModuleTypeProvider implements ModuleTypeProvider {

    /**
     * The provided module types.
     */
    private Map<String, ModuleType> providedModuleTypes;

    /**
     * The service registration.
     */
    @SuppressWarnings("rawtypes")
    private ServiceRegistration providerReg;

    /**
     * The constructor.
     */
    public GmailModuleTypeProvider() {
        providedModuleTypes = new HashMap<String, ModuleType>();
        providedModuleTypes.put(EmailActionType.UID, new EmailActionType());
    }

    /**
     * @see org.eclipse.smarthome.automation.type.ModuleTypeProvider#getModuleType(java.lang.String, java.util.Locale)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ModuleType> T getModuleType(String UID, Locale locale) {
        return (T) providedModuleTypes.get(UID);
    }

    /**
     * @see org.eclipse.smarthome.automation.type.ModuleTypeProvider#getModuleTypes(java.util.Locale)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ModuleType> Collection<T> getModuleTypes(Locale locale) {
        return (Collection<T>) providedModuleTypes.values();
    }

    /**
     * To provide the {@link ModuleType}s should register the GmailModuleTypeProvider as
     * {@link ModuleTypeProvider} service.
     *
     * @param bc
     *            is a bundle's execution context within the Framework.
     */
    public void register(BundleContext bc) {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        // properties.put(REG_PROPERTY_MODULE_TYPES, providedModuleTypes.keySet());
        providerReg = bc.registerService(ModuleTypeProvider.class.getName(), this, properties);
    }

    /**
     * This method unregisters the GmailModuleTypeProvider as {@link ModuleTypeProvider}
     * service.
     */
    public void unregister() {
        providerReg.unregister();
        providerReg = null;
        providedModuleTypes = null;
    }

    /**
     * @return all provided module types.
     */
    public Collection<ModuleType> getAll() {
        return providedModuleTypes.values();
    }
}
