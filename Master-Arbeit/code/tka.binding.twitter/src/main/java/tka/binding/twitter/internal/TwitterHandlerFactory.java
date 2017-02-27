/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.twitter.internal;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;

import tka.binding.twitter.TwitterBindingConstants;
import tka.binding.twitter.handler.TwitterHandler;

/**
 * The factory responsible for creating the thing handlers.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class TwitterHandlerFactory extends BaseThingHandlerFactory {

    /**
     * The supported thing types.
     */
    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .singleton(TwitterBindingConstants.THING_TYPE_TWITTER);

    /**
     * @see org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory#supportsThingType(org.eclipse.smarthome.core.thing.ThingTypeUID)
     */
    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    /**
     * @see org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory#createHandler(org.eclipse.smarthome.core.thing.Thing)
     */
    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(TwitterBindingConstants.THING_TYPE_TWITTER)) {
            return new TwitterHandler(thing);
        }

        return null;
    }
}
