/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
 * The thing handler for {@link DropboxBindingConstants#THING_TYPE_DROPBOX} things.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class TwitterHandler extends ConfigStatusThingHandler {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(TwitterHandler.class);

    /**
     * The constructor.
     *
     * @param thing
     */
    public TwitterHandler(Thing thing) {
        super(thing);
    }

    /**
     * @see org.eclipse.smarthome.core.thing.binding.BaseThingHandler#initialize()
     */
    @Override
    public void initialize() {
        logger.debug("Initializing Twitter handler.");
        super.initialize();

    }

    /**
     * @see org.eclipse.smarthome.core.thing.binding.ThingHandler#handleCommand(org.eclipse.smarthome.core.thing.ChannelUID,
     *      org.eclipse.smarthome.core.types.Command)
     */
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

    /**
     * @see org.eclipse.smarthome.config.core.status.ConfigStatusProvider#getConfigStatus()
     */
    @Override
    public Collection<ConfigStatusMessage> getConfigStatus() {
        logger.info("getting config status");
        Collection<ConfigStatusMessage> configStatus = new ArrayList<>();
        return configStatus;
    }

    /**
     * Sets the status of the user on twitter to a specified new status.
     *
     * @param newStatus
     */
    private void updateTwitterStatus(String newStatus) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(TwitterBindingConstants.OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterBindingConstants.OAUTH_CONSUMER_SECRET);
        String token = (String) getThing().getConfiguration().get(TwitterBindingConstants.KEY_OAUTH_TOKEN);
        String tokenSecret = (String) getThing().getConfiguration().get(TwitterBindingConstants.KEY_OAUTH_TOKEN_SECRET);
        cb.setOAuthAccessToken(token).setOAuthAccessTokenSecret(tokenSecret);
        // cb.setHttpProxyHost("proxy.materna.de").setHttpProxyPort(8080);

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
