/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.handler;

import java.util.Map;

import org.eclipse.smarthome.automation.Action;
import org.eclipse.smarthome.automation.handler.ActionHandler;
import org.eclipse.smarthome.automation.handler.BaseModuleHandler;
import org.eclipse.smarthome.core.events.EventPublisher;

/**
 * This class serves to handle the Action types provided by this application. It is used to help the RuleEngine
 * to execute the {@link Action}s.
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class TwitterActionHandler extends BaseModuleHandler<Action> implements ActionHandler {

    private EventPublisher eventPublisher;

    public TwitterActionHandler(Action module, EventPublisher eventPublisher) {
        super(module);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Map<String, Object> execute(Map<String, ?> context) {
        System.out.println("Wohoo! TwitterActionHandler called! Action executed!");
        // Command command = new StringType("MyStringTypeCodeCommand: " + new Date());
        // ItemCommandEvent event = ItemEventFactory.createCommandEvent("twitter_mycheaptwitter_percode_status",
        // command);
        // eventPublisher.post(event);
        return null;
    }

}
