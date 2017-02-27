/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.gmail.automation;

import java.util.Map;

import org.eclipse.smarthome.automation.Action;
import org.eclipse.smarthome.automation.handler.ActionHandler;
import org.eclipse.smarthome.automation.handler.BaseModuleHandler;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.events.ItemCommandEvent;
import org.eclipse.smarthome.core.items.events.ItemEventFactory;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import tka.binding.gmail.GmailBindingConstants;

/**
 * This class serves to handle the email action type provided by this binding. It is used to help the RuleEngine
 * to execute the {@link Action}.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class EmailActionHandler extends BaseModuleHandler<Action> implements ActionHandler {

    /**
     * The gson object.
     */
    private static final Gson GSON = new Gson();

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(EmailActionHandler.class);

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
     * @param module
     * @param eventPublisher
     * @param itemRegistry
     */
    public EmailActionHandler(Action module, EventPublisher eventPublisher, ItemRegistry itemRegistry) {
        super(module);
        this.eventPublisher = eventPublisher;
        this.itemRegistry = itemRegistry;
    }

    /**
     * @see org.eclipse.smarthome.automation.handler.ActionHandler#execute(java.util.Map)
     */
    @Override
    public Map<String, Object> execute(Map<String, ?> context) {
        String to = (String) module.getConfiguration().get(EmailActionType.CONFIG_TO);
        String subject = (String) module.getConfiguration().get(EmailActionType.CONFIG_SUBJECT);
        String body = (String) module.getConfiguration().get(EmailActionType.CONFIG_MESSAGE);

        if (to != null && subject != null && body != null && eventPublisher != null && itemRegistry != null) {
            try {
                Item item = itemRegistry.getItem(GmailBindingConstants.GMAIL_ITEM_NAME);
                EmailParameters emailParameters = new EmailParameters(to, subject, body);
                String message = GSON.toJson(emailParameters);
                Command commandObj = TypeParser.parseCommand(item.getAcceptedCommandTypes(), message);
                ItemCommandEvent itemCommandEvent = ItemEventFactory.createCommandEvent(item.getName(), commandObj);
                logger.debug("Executing ItemPostCommandAction on Item {} with Command {}",
                        itemCommandEvent.getItemName(), itemCommandEvent.getItemCommand());
                eventPublisher.post(itemCommandEvent);
            } catch (ItemNotFoundException e) {
                logger.error("Item with name {} not found in ItemRegistry.", GmailBindingConstants.GMAIL_ITEM_NAME);
            }
        } else {
            logger.error(
                    "Command was not posted because either the configuration was not correct or a Service was missing: ItemName: {}, Command: {}, eventPublisher: {}, ItemRegistry: {}",
                    GmailBindingConstants.GMAIL_ITEM_NAME, "'Email parameters'", eventPublisher, itemRegistry);
        }
        return null;
    }

}
