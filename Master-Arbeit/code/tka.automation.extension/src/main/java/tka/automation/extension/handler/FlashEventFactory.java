/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.handler;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;

import tka.automation.extension.type.FlashEvent;

/**
 * This factory is responsible for publishing flash events.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class FlashEventFactory extends AbstractEventFactory {

    /**
     * The supported event types.
     */
    private static final Set<String> SUPPORTED_EVENT_TYPES = new HashSet<>();
    static {
        SUPPORTED_EVENT_TYPES.add(FlashEvent.TYPE);
    }

    /**
     * The constructor.
     */
    public FlashEventFactory() {
        super(SUPPORTED_EVENT_TYPES);
    }

    /**
     * @see org.eclipse.smarthome.core.events.AbstractEventFactory#createEventByType(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        return new FlashEvent(topic, payload, source);
    }

}
