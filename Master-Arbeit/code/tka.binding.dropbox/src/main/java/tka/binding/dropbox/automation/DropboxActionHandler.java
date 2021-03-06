/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.dropbox.automation;

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
import com.google.gson.JsonParser;

/**
 * This class serves to handle the dropbox action type provided by this binding. It is used to help the RuleEngine
 * to execute the {@link Action}.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class DropboxActionHandler extends BaseModuleHandler<Action> implements ActionHandler {

    /**
     * The Gson object.
     */
    private static final Gson GSON = new Gson();

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(DropboxActionHandler.class);

    /**
     * The event publisher service.
     */
    private EventPublisher eventPublisher;

    /**
     * The item registry.
     */
    private ItemRegistry itemRegistry;

    /**
     * @param module
     * @param eventPublisher
     * @param itemRegistry
     */
    public DropboxActionHandler(Action module, EventPublisher eventPublisher, ItemRegistry itemRegistry) {
        super(module);
        this.eventPublisher = eventPublisher;
        this.itemRegistry = itemRegistry;
    }

    /**
     * @see org.eclipse.smarthome.automation.handler.ActionHandler#execute(java.util.Map)
     */
    @Override
    public Map<String, Object> execute(Map<String, ?> context) {
        String itemName = (String) module.getConfiguration().get(DropboxActionType.CONFIG_ITEM_NAME);
        String directory = (String) module.getConfiguration().get(DropboxActionType.CONFIG_DIRECTORY);

        Event event = (Event) context.get("event");
        if (event == null) {
            logger.error("Command was not posted because event information is missing from inputs");
            return null;
        }
        String payload = event.getPayload();

        if (itemName != null && directory != null && eventPublisher != null && itemRegistry != null) {
            try {
                String json;
                if (payload.contains("pathLower") && payload.contains("serverModified")) {
                    json = GSON
                            .toJson(new DropboxUploadEntity(directory, GSON.toJson(new JsonParser().parse(payload))));
                } else {
                    json = buildJson(directory, payload);
                }

                Item item = itemRegistry.getItem(itemName);
                Command commandObj = TypeParser.parseCommand(item.getAcceptedCommandTypes(), json);
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
                    itemName, directory, eventPublisher, itemRegistry);
        }
        return null;
    }

    /**
     * Builds a Dropbox Upload entity.
     *
     * @param directory
     * @param payload
     * @return json String
     */
    private String buildJson(String directory, String payload) {
        DropboxUploadEntity entity = new DropboxUploadEntity(directory, payload);

        return GSON.toJson(entity);
    }

}
