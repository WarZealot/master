package tka.binding.weather.handler;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tka.binding.weather.WeatherBindingConstants;

/**
 * @author ktkachuk
 *
 */
public class WeatherHandler extends ConfigStatusThingHandler {

    private final Logger logger = LoggerFactory.getLogger(WeatherHandler.class);

    private String location;
    private BigDecimal refresh;
    private static final JsonParser PARSER = new JsonParser();

    private String weatherJSON = null;
    ScheduledFuture<?> refreshJob;

    public WeatherHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Twitter handler.");
        super.initialize();
        Configuration config = getThing().getConfiguration();

        location = (String) config.get("location");

        try {
            refresh = (BigDecimal) config.get("refresh");
        } catch (Exception e) {
            logger.debug("Cannot set refresh parameter.", e);
        }

        if (refresh == null) {
            // let's go for the default
            refresh = new BigDecimal(60);
        }

        startAutomaticRefresh();
    }

    private void startAutomaticRefresh() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success = updateWeatherData();
                    if (success) {
                        updateState(new ChannelUID(getThing().getUID(), WeatherBindingConstants.CHANNEL_TEMPERATURE),
                                getTemperature());
                        updateState(new ChannelUID(getThing().getUID(), WeatherBindingConstants.CHANNEL_HUMIDITY),
                                getHumidity());
                        updateState(new ChannelUID(getThing().getUID(), WeatherBindingConstants.CHANNEL_RAIN),
                                getRain());
                    }
                } catch (Exception e) {
                    logger.debug("Exception occurred during execution: {}", e.getMessage(), e);
                }
            }
        };

        refreshJob = scheduler.scheduleAtFixedRate(runnable, 0, refresh.intValue(), TimeUnit.SECONDS);
    }

    @Override
    public void dispose() {
        refreshJob.cancel(true);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handle command: {}", command);
        logger.info("Command {} is not supported for channel: {}", command, channelUID.getId());
    }

    @Override
    public Collection<ConfigStatusMessage> getConfigStatus() {
        Collection<ConfigStatusMessage> configStatus = new ArrayList<>();

        if (weatherJSON != null && weatherJSON.contains("Invalid API")) {
            configStatus.add(ConfigStatusMessage.Builder.error("location").withMessageKey("weather.configparam.invalid")
                    .withArguments(location).build());
        }

        return configStatus;
    }

    private synchronized boolean updateWeatherData() {
        try {
            weatherJSON = getWeatherData();
            logger.info(weatherJSON);
            if (weatherJSON != null) {
                updateStatus(ThingStatus.ONLINE);
                return true;
            }
        } catch (IOException e) {
            logger.warn("Error accessing Wetter API: {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
        }
        return false;
    }

    private String getWeatherData() throws IOException {
        String urlString = WeatherBindingConstants.WETTER_API_URL + WeatherBindingConstants.URL_PARAM_LOCATION
                + location + "%27";
        try {
            URL url = new URL(urlString);
            logger.info("Wetter API Request: " + urlString);
            URLConnection connection = url
                    .openConnection(new Proxy(Type.HTTP, new InetSocketAddress("proxy.materna.de", 8080)));
            return IOUtils.toString(connection.getInputStream());
        } catch (MalformedURLException e) {
            logger.debug("Constructed url '{}' is not valid: {}", urlString, e.getMessage());
            return null;
        }
    }

    private State getHumidity() {
        if (weatherJSON != null) {
            JsonObject rootObj = PARSER.parse(weatherJSON).getAsJsonObject();
            BigDecimal temp = rootObj.getAsJsonObject("main").get("humidity").getAsBigDecimal();
            if (temp != null) {
                return new DecimalType(temp);
            }
        }
        return UnDefType.UNDEF;
    }

    private State getRain() {
        if (weatherJSON != null) {
            JsonObject rootObj = PARSER.parse(weatherJSON).getAsJsonObject();
            BigDecimal temp = rootObj.getAsJsonObject("main").get("temp").getAsBigDecimal();
            if (temp != null) {
                return new DecimalType(temp);
            }
        }
        return UnDefType.UNDEF;
    }

    private State getTemperature() {
        if (weatherJSON != null) {
            JsonObject rootObj = PARSER.parse(weatherJSON).getAsJsonObject();
            BigDecimal temp = rootObj.getAsJsonObject("main").get("temp").getAsBigDecimal();
            if (temp != null) {
                return new DecimalType(temp);
            }
        }
        return UnDefType.UNDEF;
    }
}
