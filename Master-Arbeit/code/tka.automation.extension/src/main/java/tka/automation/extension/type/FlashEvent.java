/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.type;

import org.eclipse.smarthome.core.events.AbstractEvent;

/**
 * This is a generic flash event type, which can be used by all bindings.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class FlashEvent extends AbstractEvent {

    /**
     * The type of the event.
     */
    public static final String TYPE = FlashEvent.class.getSimpleName();

    /**
     * The constructor.
     *
     * @param topic
     * @param payload
     * @param source
     */
    public FlashEvent(String topic, String payload, String source) {
        super(topic, payload, source);
    }

    /**
     * @see org.eclipse.smarthome.core.events.Event#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[source=" + getSource());
        builder.append(", topic=" + getTopic());
        builder.append(", payload=" + getPayload());
        builder.append("]");
        return builder.toString();
    }
}
