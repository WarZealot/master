/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.twitter.internal;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.service.component.ComponentContext;

import tka.binding.core.AbstractConnectionService;
import tka.binding.twitter.TwitterBindingConstants;
import tka.binding.twitter.TwitterConnectionService;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class setups the connection to the twitter account of the user.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class TwitterConnectionServiceImpl extends AbstractConnectionService
        implements TwitterConnectionService, EventSubscriber {

    /**
     * The uid of the twitter thing.
     */
    private static final ThingUID TWITTER_CONNECTION = new ThingUID("twitter", "twitterThingTypeId",
            "twitterconnection");
    /**
     * The oauth access token.
     */
    private AccessToken accessToken = null;

    /**
     * The request token.
     */
    private RequestToken requestToken;

    /**
     * The twitter client.
     */
    private Twitter twitter;

    /**
     * The twitter stream.
     */
    private TwitterStream twitterStream;

    /**
     * The twitter configuration.
     */
    private twitter4j.conf.Configuration configuration;

    /**
     * @see tka.binding.twitter.TwitterConnectionService#requestAuthorization()
     */
    @Override
    public Object requestAuthorization() {
        if (accessToken != null) {
            return null;
        }
        twitter = new TwitterFactory(configuration).getInstance();

        try {
            requestToken = twitter.getOAuthRequestToken();
            return requestToken.getAuthorizationURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @see tka.binding.core.ConnectionService#isAuthorized()
     */
    @Override
    public boolean isAuthorized() {
        return accessToken != null;
    }

    /**
     * @see tka.binding.core.ConnectionService#authorizationGrantedCallback(java.lang.Object)
     */
    @Override
    public boolean authorizationGrantedCallback(Object info) {
        if (requestToken == null || twitter == null) {
            return false;
        }
        String pin = (String) info;
        try {
            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
            configuration = buildTwitterConfiguration();
            createTwitterThing();
            createTwitterStream();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Starts listening to changes in the account of the user.
     */
    private void createTwitterStream() {
        twitterStream = new TwitterStreamFactory(configuration).getInstance();
        twitterStream.addListener(new TwitterUserStreamLIstener(eventPublisher));
        twitterStream.user();
    }

    /**
     * @see tka.binding.core.AbstractConnectionService#activate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);

        Thing twitterConnection = thingRegistry.get(TWITTER_CONNECTION);
        if (twitterConnection != null) {
            Configuration properties = twitterConnection.getConfiguration();
            String token = (String) properties.get(TwitterBindingConstants.KEY_OAUTH_TOKEN);
            String tokenSecret = (String) properties.get(TwitterBindingConstants.KEY_OAUTH_TOKEN_SECRET);

            accessToken = new AccessToken(token, tokenSecret);
            configuration = buildTwitterConfiguration();
            createTwitterStream();
            return;
        }
        configuration = buildTwitterConfiguration();
    }

    /**
     * @return the default twitter configurtion used when requesting an oauth token.
     */
    private twitter4j.conf.Configuration buildTwitterConfiguration() {
        ConfigurationBuilder twitterConfigurationBuilder = new ConfigurationBuilder();
        twitterConfigurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterBindingConstants.OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterBindingConstants.OAUTH_CONSUMER_SECRET);
        // twitterConfigurationBuilder.setHttpProxyHost("proxy.materna.de").setHttpProxyPort(8080);

        if (accessToken != null) {
            twitterConfigurationBuilder.setOAuthAccessToken(accessToken.getToken())
                    .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
        }
        return twitterConfigurationBuilder.build();
    }

    /**
     * @see tka.binding.core.AbstractConnectionService#deactivate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Creates a new twitter thing.
     */
    private void createTwitterThing() {
        Configuration configuration = new Configuration();
        configuration.put(TwitterBindingConstants.KEY_OAUTH_TOKEN, accessToken.getToken());
        configuration.put(TwitterBindingConstants.KEY_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());

        Thing thing = thingRegistry.createThingOfType(TwitterBindingConstants.THING_TYPE_TWITTER, TWITTER_CONNECTION,
                null, "twitterLabel", configuration);
        thingRegistry.add(thing);
    }

    /**
     * @see org.eclipse.smarthome.core.events.EventSubscriber#receive(org.eclipse.smarthome.core.events.Event)
     */
    @Override
    public void receive(Event event) {
        ThingRemovedEvent e = (ThingRemovedEvent) event;
        if (e.getThing().thingTypeUID.equals(TwitterBindingConstants.THING_TYPE_TWITTER.toString())) {
            accessToken = null;
            configuration = buildTwitterConfiguration();
            twitterStream.clearListeners();
            twitterStream.shutdown();
            twitterStream = null;
            twitter = null;
        }
    }
}
