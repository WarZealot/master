/**
 *
 */
package tka.binding.twitter.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFactory;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;

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
 * @author Konstantin
 *
 */
public class TwitterConnectionServiceImpl implements TwitterConnectionService, EventSubscriber {

    private static final ThingUID TWITTER_CONNECTION = new ThingUID("twitter", "twitterThingTypeId",
            "twitterconnection");
    private static final Set<String> SUBSCRIBED_EVENT_TYPES = new HashSet<>();
    static {
        SUBSCRIBED_EVENT_TYPES.add(ThingRemovedEvent.TYPE);
    }
    /**
     *
     */
    private AccessToken accessToken = null;
    private RequestToken requestToken;
    private Twitter twitter;
    private ThingRegistry thingRegistry;
    private EventPublisher eventPublisher;
    private ComponentContext context;
    private TwitterStream twitterStream;
    private twitter4j.conf.Configuration configuration;
    private TwitterEventFactory twitterEventFactory;
    private BundleContext bundleContext;
    private ServiceRegistration<?> eventFactoryService;
    private ServiceRegistration<?> thisSubscriberService;

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

    @Override
    public boolean isAuthorized() {
        return accessToken != null;
    }

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

    private void createTwitterStream() {
        twitterStream = new TwitterStreamFactory(configuration).getInstance();
        twitterStream.addListener(new TwitterUserStreamLIstener(eventPublisher));
        twitterStream.user();
    }

    public void activate(ComponentContext context) {
        this.context = context;
        this.bundleContext = context.getBundleContext();

        regsiterServices();

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

    private twitter4j.conf.Configuration buildTwitterConfiguration() {
        ConfigurationBuilder twitterConfigurationBuilder = new ConfigurationBuilder();
        twitterConfigurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterBindingConstants.OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterBindingConstants.OAUTH_CONSUMER_SECRET);
        twitterConfigurationBuilder.setHttpProxyHost("proxy.materna.de").setHttpProxyPort(8080);

        if (accessToken != null) {
            twitterConfigurationBuilder.setOAuthAccessToken(accessToken.getToken())
                    .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
        }
        return twitterConfigurationBuilder.build();
    }

    private void regsiterServices() {
        HashSet<String> set = new HashSet<String>();
        set.add(TwitterEvent.TYPE);
        twitterEventFactory = new TwitterEventFactory(set);
        eventFactoryService = bundleContext.registerService(EventFactory.class.getName(), twitterEventFactory, null);

        thisSubscriberService = bundleContext.registerService(EventSubscriber.class.getName(), this, null);
    }

    public void deactivate(ComponentContext context) {
        if (eventFactoryService != null) {
            eventFactoryService.unregister();
        }
        if (thisSubscriberService != null) {
            thisSubscriberService.unregister();
        }

        this.bundleContext = null;
        this.context = null;
    }

    private void createTwitterThing() {
        Configuration configuration = new Configuration();
        configuration.put(TwitterBindingConstants.KEY_OAUTH_TOKEN, accessToken.getToken());
        configuration.put(TwitterBindingConstants.KEY_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());

        Thing thing = thingRegistry.createThingOfType(TwitterBindingConstants.THING_TYPE_TWITTER, TWITTER_CONNECTION,
                null, "twitterLabel", configuration);
        thingRegistry.add(thing);

    }

    public void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    public void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return SUBSCRIBED_EVENT_TYPES;
    }

    @Override
    public EventFilter getEventFilter() {
        return null;
    }

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
