/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.dropbox.internal;

import java.util.TreeMap;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeletedMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.google.gson.Gson;

import tka.binding.dropbox.DropboxBindingConstants;

/**
 * This class is responsible for tracking the changes in the dropbox.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class DropboxChangesTracker implements Runnable {

    /**
     * The GSON object.
     */
    private static final Gson GSON = new Gson();

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(DropboxChangesTracker.class);

    /**
     * The dropbox client.
     */
    private final DbxClientV2 client;

    /**
     * Used to publishe events in the OSGi event bus.
     */
    private final EventPublisher eventPublisher;

    /**
     * Reflects the current state of the dropbox. During checks the new state is compared against this one.
     */
    private TreeMap<String, Metadata> children;

    /**
     * The cursor.
     */
    private String cursor;

    /**
     * The constructor.
     *
     * @param client
     * @param eventPublisher
     */
    public DropboxChangesTracker(DbxClientV2 client, EventPublisher eventPublisher) {
        this.client = client;
        this.eventPublisher = eventPublisher;
        cursor = null;
        children = new TreeMap<String, Metadata>();

        // check current state
        try {
            ListFolderResult result = client.files().listFolderBuilder("").withRecursive(true).start();
            cursor = result.getCursor();
            for (Metadata md : result.getEntries()) {
                children.put(md.getPathLower(), md);
            }
        } catch (DbxException e) {
            logger.error(
                    "Could not check current state of dropbox. All existing files will be interpreted as newly added!",
                    e);
        }
    }

    /**
     * Uses a cursor to track changes.
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            ListFolderResult result;

            if (cursor == null) {
                // result = client.files().listFolder("");
                result = client.files().listFolderBuilder("").withRecursive(true).start();
            } else {
                result = client.files().listFolderContinue(cursor);
            }
            cursor = result.getCursor();
            for (Metadata md : result.getEntries()) {
                if (md instanceof DeletedMetadata) {
                    children.remove(md.getPathLower());
                    publishEvent(DropboxBindingConstants.TOPIC_DELETED, GSON.toJson(md));
                } else {
                    if (children.containsKey(md.getPathLower())) {
                        publishEvent(DropboxBindingConstants.TOPIC_CHANGED, GSON.toJson(md));
                    } else {
                        publishEvent(DropboxBindingConstants.TOPIC_ADDED, GSON.toJson(md));
                    }
                    children.put(md.getPathLower(), md);
                }
            }
        } catch (Exception e) {
            logger.error("Could not poll changes in dropbox.");
        }
    }

    /**
     * Publishes a flash event with the specified topic and payload
     *
     * @param topic
     * @param payload
     */
    private void publishEvent(final String topic, final String payload) {
        Event event = new Event() {
            @Override
            public String getType() {
                return "FlashEvent";
            }

            @Override
            public String getTopic() {
                return topic;
            }

            @Override
            public String getSource() {
                return DropboxBindingConstants.SOURCE;
            }

            @Override
            public String getPayload() {
                return payload;
            }
        };
        eventPublisher.post(event);
    }
}
