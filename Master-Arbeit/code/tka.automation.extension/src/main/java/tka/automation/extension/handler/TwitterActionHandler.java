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
import org.eclipse.smarthome.core.events.Event;
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

import tka.automation.extension.type.DropboxActionType;
import tka.automation.extension.type.TwitterActionType;

/**
 * This class serves to handle the Action types provided by this application. It is used to help the RuleEngine
 * to execute the {@link Action}s.
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class TwitterActionHandler extends BaseModuleHandler<Action> implements ActionHandler {

    private static final Gson GSON = new Gson();
    private final Logger logger = LoggerFactory.getLogger(DropboxActionHandler.class);

    private EventPublisher eventPublisher;
    private ItemRegistry itemRegistry;

    public TwitterActionHandler(Action module, EventPublisher eventPublisher, ItemRegistry itemRegistry) {
        super(module);
        this.eventPublisher = eventPublisher;
        this.itemRegistry = itemRegistry;
    }

    @Override
    public Map<String, Object> execute(Map<String, ?> context) {
        String itemName = (String) module.getConfiguration().get(DropboxActionType.CONFIG_ITEM_NAME);
        String message = (String) module.getConfiguration().get(DropboxActionType.CONFIG_DIRECTORY);
        if (message == null) {
            message = "";
        }

        Event event = (Event) context.get("event");
        if (event != null) {
            if (message.contains(TwitterActionType.PLACEHOLDER)) {
                message = message.replaceAll(TwitterActionType.PLACEHOLDER, event.getPayload());
            } else if (!event.getPayload().isEmpty()) {
                message += event.getPayload();
            }
        }

        if (itemName != null && !message.isEmpty() && eventPublisher != null && itemRegistry != null) {
            try {
                Item item = itemRegistry.getItem(itemName);
                Command commandObj = TypeParser.parseCommand(item.getAcceptedCommandTypes(), message);
                ItemCommandEvent itemCommandEvent = ItemEventFactory.createCommandEvent(itemName, commandObj);
                logger.debug("Executing ItemPostCommandAction on Item {} with Command {}",
                        itemCommandEvent.getItemName(), itemCommandEvent.getItemCommand());
                eventPublisher.post(itemCommandEvent);
            } catch (ItemNotFoundException e) {
                logger.error("Item with name {} not found in ItemRegistry.", itemName);
            }
        } else {
            logger.error(
                    "Command was not posted because either the configuration was not correct or a Service was missing: ItemName: {}, Command: {}, eventPublisher: {}, ItemRegistry: {}",
                    itemName, message, eventPublisher, itemRegistry);
        }
        return null;
    }

}
