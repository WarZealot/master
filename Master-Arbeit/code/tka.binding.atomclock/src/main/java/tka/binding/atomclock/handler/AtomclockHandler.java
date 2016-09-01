package tka.binding.atomclock.handler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tka.binding.atomclock.AtomclockBindingConstants;

/**
 * @author ktkachuk
 *
 */
public class AtomclockHandler extends ConfigStatusThingHandler {

    private final Logger logger = LoggerFactory.getLogger(AtomclockHandler.class);

    private BigDecimal location;
    private BigDecimal refresh;

    private String timeData = null;

    ScheduledFuture<?> refreshJob;

    public AtomclockHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Atomclock handler.");
        super.initialize();

        Configuration config = getThing().getConfiguration();

        try {
            refresh = (BigDecimal) config.get("refresh");
        } catch (Exception e) {
            logger.debug("Cannot set refresh parameter.", e);
        }

        if (refresh == null) {
            refresh = new BigDecimal(10);
        }

        startAutomaticRefresh();
    }

    @Override
    public void dispose() {
        refreshJob.cancel(true);
    }

    private void startAutomaticRefresh() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success = updateTimeData();
                    logger.info("updatedWeatherData: {} {}", success, timeData);
                    if (success) {
                        updateState(new ChannelUID(getThing().getUID(), AtomclockBindingConstants.CHANNEL_TIME),
                                getTime());

                    }
                } catch (Exception e) {
                    logger.debug("Exception occurred during execution: {}", e.getMessage(), e);
                }
            }
        };
        logger.info("Starting automatic refresh");
        refreshJob = scheduler.scheduleAtFixedRate(runnable, 0, refresh.intValue(), TimeUnit.SECONDS);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handle command: {}", command);
        if (command instanceof RefreshType) {
            boolean success = updateTimeData();
            if (success) {
                switch (channelUID.getId()) {
                    case AtomclockBindingConstants.CHANNEL_TIME:
                        updateState(channelUID, getTime());
                        break;
                    default:
                        logger.debug("Command received for an unknown channel: {}", channelUID.getId());
                        break;
                }
            }
        } else {
            logger.debug("Command {} is not supported for channel: {}", command, channelUID.getId());
        }
    }

    @Override
    public Collection<ConfigStatusMessage> getConfigStatus() {
        logger.info("getting config status");
        Collection<ConfigStatusMessage> configStatus = new ArrayList<>();

        try {
            String weatherData = getTimeData();
            String result = StringUtils.substringBetween(weatherData, "<item><title>", "</title>");
            if ("City not found".equals(result)) {
                // TODO:
            }
        } catch (IOException e) {
            logger.debug("Communication error occurred while getting Atomclock information.", e);
        }

        return configStatus;
    }

    private synchronized boolean updateTimeData() {
        logger.info("updating Time Data");
        try {
            timeData = getTimeData();
            if (timeData != null) {
                updateStatus(ThingStatus.ONLINE);
                return true;
            }
        } catch (IOException e) {
            logger.warn("Error accessing Atomclock time: {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
        }
        return false;
    }

    private String getTimeData() throws IOException {
        return new String(System.currentTimeMillis() + "");
    }

    private State getTime() {
        if (timeData != null) {
            return new DecimalType(timeData);
            // String temp = StringUtils.substringAfter(timeData, "condition");
            // temp = StringUtils.substringBetween(temp, "temp\":\"", "\"");
            // if (temp != null) {
            // return new DecimalType(temp);
            // }
        }
        return UnDefType.UNDEF;
    }
}
