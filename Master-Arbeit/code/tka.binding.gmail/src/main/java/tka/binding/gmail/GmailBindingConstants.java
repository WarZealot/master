/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.gmail;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;

import com.google.common.collect.ImmutableSet;

/**
 * Util class, that contains constants.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class GmailBindingConstants {

    /**
     * The id of the binding.
     */
    public static final String BINDING_ID = "gmail";

    /**
     * The uid of a single supported thing type.
     */
    public final static ThingTypeUID THING_TYPE_GMAIL = new ThingTypeUID(BINDING_ID, "gmail");

    /**
     * The name of the channel.
     */
    public static final String CHANNEL_EMAIL = "email";

    /**
     * The set of all supported thing types.
     */
    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_GMAIL);

    /**
     * The name of the single instance of the thing type.
     */
    public static final ThingUID GMAIL_CONNECTION = new ThingUID("gmail", "gmailThingTypeId", "gmailconnection");

    /**
     * The name of the corresponding item.
     */
    public static final String GMAIL_ITEM_NAME = "gmail_gmailThingTypeId_gmailconnection_email";

    /**
     * The username of the account from which emails are sent.
     */
    public static final String USERNAME = "noreply.flash.ma@gmail.com";

    /**
     * The password of the account from which emails are sent.
     */
    public static final String PASSWORD = "a1b1c1d1";

}
