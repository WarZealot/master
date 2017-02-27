/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.dropbox;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * Util class, that contains constants.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class DropboxBindingConstants {

    /**
     * The id of the binding.
     */
    public static final String BINDING_ID = "dropbox";

    /**
     * The uid of a single supported thing type.
     */
    public final static ThingTypeUID THING_TYPE_DROPBOX = new ThingTypeUID(BINDING_ID, "dropbox");

    /**
     * The name of the channel.
     */
    public static final String CHANNEL_FOLDER = "folder";

    /**
     * The set of all supported thing types.
     */
    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_DROPBOX);

    /**
     * The OAuth consumer key.
     */
    public final static String OAUTH_CONSUMER_KEY = "czrkhe4jhjqrxkv";

    /**
     * The OAuth consumer secret.
     */
    public final static String OAUTH_CONSUMER_SECRET = "cxaeslwf8paz5il";

    /**
     * The key, under which the oauth token is saved in the thing configuration.
     */
    public static final String KEY_OAUTH_TOKEN = "oauthToken";

    /**
     * The prefix of all event topics published by this binding.
     */
    public final static String TOPIC = "flash/dropbox/";

    /**
     * The topic of deleted events.
     */
    public final static String TOPIC_DELETED = TOPIC + "deleted";

    /**
     * The topic of added events.
     */
    public final static String TOPIC_ADDED = TOPIC + "added";

    /**
     * The topic of changed events.
     */
    public final static String TOPIC_CHANGED = TOPIC + "changed";

    /**
     * All events published by this binding have this source.
     */
    public final static String SOURCE = "flash/dropbox";

}
