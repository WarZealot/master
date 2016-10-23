package tka.binding.twitter.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;

import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }

    @Override
    public void dispose() {
        refreshJob.cancel(true);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handle command: {}", command);
        if (command instanceof StringType) {
            StringType cmd = (StringType) command;
            updateTwitterStatus(cmd.toString());
            return;
        }
        logger.info("Command {} is not supported for channel: {}", command, channelUID.getId());
    }

    @Override
    public Collection<ConfigStatusMessage> getConfigStatus() {
        logger.info("getting config status");
        Collection<ConfigStatusMessage> configStatus = new ArrayList<>();
        return configStatus;
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
        // cb.setHttpProxyHost("proxy.materna.de").setHttpProxyPort(8080);
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
