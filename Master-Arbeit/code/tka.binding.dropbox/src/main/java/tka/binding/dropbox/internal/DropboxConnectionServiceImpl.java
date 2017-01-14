/**
 *
 */
package tka.binding.dropbox.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.events.Event;
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

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;

import tka.binding.dropbox.DropboxBindingConstants;
import tka.binding.dropbox.DropboxConnectionService;

/**
 * @author Konstantin
 *
 */
public class DropboxConnectionServiceImpl implements DropboxConnectionService, EventSubscriber {

    private static final ThingUID DROPBOX_CONNECTION = new ThingUID("dropbox", "dropboxThingTypeId",
            "dropboxconnection");
    private static final Set<String> SUBSCRIBED_EVENT_TYPES = new HashSet<>();
    static {
        SUBSCRIBED_EVENT_TYPES.add(ThingRemovedEvent.TYPE);
    }
    /**
     *
     */
    private ThingRegistry thingRegistry;
    private EventPublisher eventPublisher;
    private ComponentContext context;
    private BundleContext bundleContext;
    private ServiceRegistration<?> thisSubscriberService;
    private String accessToken;
    private DbxClientV2 client;
    private DbxWebAuth webAuth;

    /**
     * @see tka.binding.dropbox.DropboxConnectionService#requestAuthorization()
     */
    @Override
    public Object requestAuthorization() {
        if (accessToken != null) {
            return null;
        }
        DbxAppInfo dbxAppInfo = new DbxAppInfo(DropboxBindingConstants.OAUTH_CONSUMER_KEY,
                DropboxBindingConstants.OAUTH_CONSUMER_SECRET);
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();
        webAuth = new DbxWebAuth(dbxRequestConfig, dbxAppInfo);
        String url = webAuth.authorize(authRequest);

        return url;
    }

    @Override
    public boolean isAuthorized() {
        return accessToken != null;
    }

    @Override
    public boolean authorizationGrantedCallback(Object info) {
        if (webAuth == null) {
            return false;
        }
        String pin = (String) info;
        DbxAuthFinish authFinish;
        try {
            authFinish = webAuth.finishFromCode(pin);
            accessToken = authFinish.getAccessToken();
            createDropboxThing();
            client = buildDropboxClient();
            webAuth = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            webAuth = null;
            accessToken = null;
            client = null;
        }
        return false;
    }

    public void activate(ComponentContext context) {
        this.context = context;
        this.bundleContext = context.getBundleContext();

        System.out.println("DropboxConnectionServiceImpl.activate()");

        regsiterServices();

        Thing dropboxConnection = thingRegistry.get(DROPBOX_CONNECTION);
        if (dropboxConnection != null) {
            Configuration properties = dropboxConnection.getConfiguration();
            accessToken = (String) properties.get(DropboxBindingConstants.KEY_OAUTH_TOKEN);

            client = buildDropboxClient();
            return;
        }
    }

    private DbxClientV2 buildDropboxClient() {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        return new DbxClientV2(dbxRequestConfig, accessToken);
    }

    private void regsiterServices() {
        thisSubscriberService = bundleContext.registerService(EventSubscriber.class.getName(), this, null);
    }

    public void deactivate(ComponentContext context) {
        if (thisSubscriberService != null) {
            thisSubscriberService.unregister();
        }

        this.bundleContext = null;
        this.context = null;
    }

    private void createDropboxThing() {
        Configuration configuration = new Configuration();
        configuration.put(DropboxBindingConstants.KEY_OAUTH_TOKEN, accessToken);

        Thing thing = thingRegistry.createThingOfType(DropboxBindingConstants.THING_TYPE_DROPBOX, DROPBOX_CONNECTION,
                null, "dropboxLabel", configuration);
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
        if (e.getThing().thingTypeUID.equals(DropboxBindingConstants.THING_TYPE_DROPBOX.toString())) {
            accessToken = null;
            client = null;
            webAuth = null;
        }

    }
}
