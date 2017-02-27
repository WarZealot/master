/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.twitter;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * Util class that contains constants.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class TwitterBindingConstants {

    /**
     * The id of the binding.
     */
    public static final String BINDING_ID = "twitter";

    /**
     * The uid of a single supported thing type.
     */
    public final static ThingTypeUID THING_TYPE_TWITTER = new ThingTypeUID(BINDING_ID, "twitter");

    /**
     * The name of the channel.
     */
    public static final String CHANNEL_STATUS = "status";

    /**
     * The set of all supported thing types.
     */
    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_TWITTER);

    /**
     * The OAuth consumer key.
     */
    public final static String OAUTH_CONSUMER_KEY = "9F8YmBxVk6o7XZfGNLgQieREN";
    /**
     * The OAuth consumer secret.
     */
    public final static String OAUTH_CONSUMER_SECRET = "avJ03ScGJwMCR3GmZ1FkFtbDiGk7eJcts9R61Hy3BvXgVMPie7";

    /**
     * The key, under which the oauth token secret is saved in the thing configuration.
     */
    public static final String KEY_OAUTH_TOKEN_SECRET = "oauthTokenSecret";

    /**
     * The key, under which the oauth token is saved in the thing configuration.
     */
    public static final String KEY_OAUTH_TOKEN = "oauthToken";

    /**
     * The prefix of all event topics published by this binding.
     */
    private final static String TOPIC = "flash/twitter/";

    /**
     * The topic of status events.
     */
    public final static String TOPIC_STATUS_CHANGED = TOPIC + "status";

    /**
     * The topic of media events.
     */
    public final static String TOPIC_MEDIA = TOPIC + "media";

    /**
     * The topic of direct message received events.
     */
    public final static String TOPIC_MESSAGE = TOPIC + "message";

    /**
     * All events published by this binding have this source.
     */
    public final static String SOURCE = "flash/twitter";
}
