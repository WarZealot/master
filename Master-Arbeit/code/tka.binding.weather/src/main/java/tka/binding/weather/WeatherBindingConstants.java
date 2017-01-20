
package tka.binding.weather;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * @author ktkachuk
 *
 */
public class WeatherBindingConstants {

    public static final String BINDING_ID = "weather";

    // List all Thing Type UIDs, related to the Weather Binding
    public final static ThingTypeUID THING_TYPE_WEATHER = new ThingTypeUID(BINDING_ID, "weather");

    // List all channels
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_RAIN = "rain";

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_WEATHER);

    public static final String WETTER_API_KEY = "a1de5d8edfdc4d2304641ac11b1b7d6f";
    public static final String WETTER_API_URL = "http://api.openweathermap.org/data/2.5/weather?appid="
            + WETTER_API_KEY;
    public static final String URL_PARAM_LOCATION = "&q=";
    public static final String CONFIG_LOCATION = "location";
    public static final String CONFIG_REFRESH = "refresh";

    public final static String TOPIC_STATUS_CHANGED = "flash/weather/temperature";
    public final static String SOURCE = "flash/weather";
}
