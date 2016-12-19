package tka.binding.twitter.handler;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
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

    public TwitterHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Twitter handler.");
        super.initialize();

    }

    @Override
    public void dispose() {
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
        cb.setDebugEnabled(true).setOAuthConsumerKey(TwitterBindingConstants.OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterBindingConstants.OAUTH_CONSUMER_SECRET);
        String token = (String) getThing().getConfiguration().get(TwitterBindingConstants.KEY_OAUTH_TOKEN);
        String tokenSecret = (String) getThing().getConfiguration().get(TwitterBindingConstants.KEY_OAUTH_TOKEN_SECRET);
        cb.setOAuthAccessToken(token).setOAuthAccessTokenSecret(tokenSecret);
        cb.setHttpProxyHost("proxy.materna.de").setHttpProxyPort(8080);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Status status;
        try {
            status = twitter.updateStatus(newStatus);
            logger.info("Successfully updated the status to [" + status.getText() + "].");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
