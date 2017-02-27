/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.weather;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * Util class. Contains constants.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class WeatherBindingConstants {

    /**
     * The id of the binding.
     */
    public static final String BINDING_ID = "weather";

    /**
     * The uid of a single supported thing type.
     */
    public final static ThingTypeUID THING_TYPE_WEATHER = new ThingTypeUID(BINDING_ID, "weather");

    /**
     * The name of the channel.
     */
    public static final String CHANNEL_TEMPERATURE = "temperature";

    /**
     * The name of the channel.
     */
    public static final String CHANNEL_HUMIDITY = "humidity";

    /**
     * The name of the channel.
     */
    public static final String CHANNEL_RAIN = "rain";

    /**
     * The set of all supported thing types.
     */
    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_WEATHER);

    /**
     * The api key.
     */
    public static final String WETTER_API_KEY = "a1de5d8edfdc4d2304641ac11b1b7d6f";

    /**
     * The url from where the weather information is loaded.
     */
    public static final String WETTER_API_URL = "http://api.openweathermap.org/data/2.5/weather?appid="
            + WETTER_API_KEY;

    /**
     * The url parameter name for the location.
     */
    public static final String URL_PARAM_LOCATION = "&q=";

    /**
     * The key under which the location is saved in the thing configuration.
     */
    public static final String CONFIG_LOCATION = "location";

    /**
     * The key under which the refresh period is saved in the thing configuration.
     */
    public static final String CONFIG_REFRESH = "refresh";
}
