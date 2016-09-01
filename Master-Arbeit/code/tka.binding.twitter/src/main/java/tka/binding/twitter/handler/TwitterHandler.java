package tka.binding.twitter.handler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
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

import tka.binding.twitter.TwitterBindingConstants;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author ktkachuk
 *
 */
public class TwitterHandler extends ConfigStatusThingHandler {

    private final Logger logger = LoggerFactory.getLogger(TwitterHandler.class);

    private String timeData = null;

    ScheduledFuture<?> refreshJob;

    private BigDecimal refresh;

    public TwitterHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Twitter handler.");
        super.initialize();

        if (refresh == null) {
            refresh = new BigDecimal(30);
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
                updateTwitterStatus("Refresh status: " + new Date());
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
                    case TwitterBindingConstants.CHANNEL_STATUS:
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
            logger.debug("Communication error occurred while getting Twitter information.", e);
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
            logger.warn("Error accessing Twitter time: {}", e.getMessage());
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
        }
        return UnDefType.UNDEF;
    }

    /**
     * @param newStatus
     */
    private void updateTwitterStatus(String newStatus) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey("9F8YmBxVk6o7XZfGNLgQieREN")
                .setOAuthConsumerSecret("avJ03ScGJwMCR3GmZ1FkFtbDiGk7eJcts9R61Hy3BvXgVMPie7")
                .setOAuthAccessToken("3524637135-tnqDTgMAFNPcUd11JgacIxz5rqeQFUW8PobKNyi")
                .setOAuthAccessTokenSecret("TDpDdNX5cB83wilP213DGQ8UNsgMvdK9R8oJEFqfu6MRF");
        cb.setHttpProxyHost("proxy.materna.de").setHttpProxyPort(8080);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Status status;
        try {
            status = twitter.updateStatus(newStatus);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
