/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.automation.Action;
import org.eclipse.smarthome.automation.Condition;
import org.eclipse.smarthome.automation.Module;
import org.eclipse.smarthome.automation.Trigger;
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

import tka.automation.extension.type.AlwaysTrueConditionType;
import tka.automation.extension.type.DropboxActionType;
import tka.automation.extension.type.TkaTriggerType;
import tka.automation.extension.type.TwitterActionType;

/**
 * This class is a simple implementation of the {@link ModuleHandlerFactory}, which is registered as a service.
 *
 */
public class FlashHandlerFactory extends BaseModuleHandlerFactory {

    public static final String MODULE_HANDLER_FACTORY_NAME = "[TkaHandlerFactory]";
    private static final Collection<String> TYPES;

    static {
        List<String> temp = new ArrayList<String>();
        temp.add(TwitterActionType.UID);
        temp.add(DropboxActionType.UID);
        temp.add(AlwaysTrueConditionType.UID);
        temp.add(TkaTriggerType.UID);
        TYPES = Collections.unmodifiableCollection(temp);
    }

    @SuppressWarnings("rawtypes")
    private ServiceRegistration factoryRegistration;
    private Map<String, TkaTriggerHandler> triggerHandlers;
    private Logger logger = LoggerFactory.getLogger(FlashHandlerFactory.class);
    private EventPublisher eventPublisher;
    private ItemRegistry itemRegistry;

    public FlashHandlerFactory(BundleContext bc) {
        triggerHandlers = new HashMap<String, TkaTriggerHandler>();
        activate(bc);

        ServiceReference<EventPublisher> reference3 = bc.getServiceReference(EventPublisher.class);
        eventPublisher = bc.getService(reference3);

        ServiceReference<ItemRegistry> reference4 = bc.getServiceReference(ItemRegistry.class);
        itemRegistry = bc.getService(reference4);
    }

    @Override
    public Collection<String> getTypes() {
        return TYPES;
    }

    public void register(BundleContext bc) {
        factoryRegistration = bc.registerService(ModuleHandlerFactory.class.getName(), this, null);
    }

    public void unregister() {
        factoryRegistration.unregister();
        factoryRegistration = null;
    }

    public TkaTriggerHandler getTriggerHandler(String uid) {
        return triggerHandlers.get(uid);
    }

    @Override
    protected ModuleHandler internalCreate(Module module, String ruleUID) {
        System.out.println("FlashHandlerFactory.internalCreate() " + module + " " + ruleUID);
        ModuleHandler moduleHandler = null;
        if (TwitterActionType.UID.equals(module.getTypeUID())) {
            moduleHandler = new TwitterActionHandler((Action) module, eventPublisher);
        } else if (DropboxActionType.UID.equals(module.getTypeUID())) {
            moduleHandler = new DropboxActionHandler((Action) module, eventPublisher, itemRegistry);
        } else if (AlwaysTrueConditionType.UID.equals(module.getTypeUID())) {
            moduleHandler = new AlwaysTrueConditionHandler((Condition) module);
        } else if (TkaTriggerType.UID.equals(module.getTypeUID())) {
            moduleHandler = new TkaTriggerHandler((Trigger) module);
            triggerHandlers.put(ruleUID, (TkaTriggerHandler) moduleHandler);
        } else {
            logger.error(MODULE_HANDLER_FACTORY_NAME + "Not supported moduleHandler: {}", module.getTypeUID());
        }
        System.out.println("TriggerHandlers: " + triggerHandlers);
        return moduleHandler;
    }
}
