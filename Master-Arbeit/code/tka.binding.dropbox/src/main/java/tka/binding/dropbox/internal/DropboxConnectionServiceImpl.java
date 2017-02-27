/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.dropbox.internal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.events.ThingRemovedEvent;
import org.osgi.service.component.ComponentContext;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;

import tka.binding.core.AbstractConnectionService;
import tka.binding.dropbox.DropboxBindingConstants;
import tka.binding.dropbox.DropboxConnectionService;

/**
 * This class offers authorization in the dropbox.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class DropboxConnectionServiceImpl extends AbstractConnectionService
        implements DropboxConnectionService, EventSubscriber {

    /**
     * The unique uid of the single instance of the dropbox thing.
     */
    private static final ThingUID DROPBOX_CONNECTION = new ThingUID("dropbox", "dropboxThingTypeId",
            "dropboxconnection");

    /**
     * The oauth access token.
     */
    private String accessToken;

    /**
     * The drobox client.
     */
    private DbxClientV2 client;

    /**
     * Used during authorization process.
     */
    private DbxWebAuth webAuth;

    /**
     * The scheduled check of the state of the dropbox.
     */
    private ScheduledFuture<?> scheduledFuture;

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
        if (webAuth == null) {
            return false;
        }
        String pin = (String) info;
        DbxAuthFinish authFinish;
        try {
            authFinish = webAuth.finishFromCode(pin);
            accessToken = authFinish.getAccessToken();
            createDropboxThing();
            buildDropboxClient();
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

    /**
     * @see tka.binding.core.AbstractConnectionService#activate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);

        System.out.println("DropboxConnectionServiceImpl.activate()");

        Thing dropboxConnection = thingRegistry.get(DROPBOX_CONNECTION);
        if (dropboxConnection != null) {
            Configuration properties = dropboxConnection.getConfiguration();
            accessToken = (String) properties.get(DropboxBindingConstants.KEY_OAUTH_TOKEN);

            buildDropboxClient();
            return;
        }
    }

    /**
     * Builds a dropbox client.
     */
    private void buildDropboxClient() {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        client = new DbxClientV2(dbxRequestConfig, accessToken);
        registerChangeListener();
    }

    /**
     * @see tka.binding.core.AbstractConnectionService#deactivate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Creates a Thing of the type {@link DropboxBindingConstants#THING_TYPE_DROPBOX}
     */
    private void createDropboxThing() {
        Configuration configuration = new Configuration();
        configuration.put(DropboxBindingConstants.KEY_OAUTH_TOKEN, accessToken);

        Thing thing = thingRegistry.createThingOfType(DropboxBindingConstants.THING_TYPE_DROPBOX, DROPBOX_CONNECTION,
                null, "dropboxLabel", configuration);
        thingRegistry.add(thing);
    }

    /**
     * Registers the change listener.
     */
    private void registerChangeListener() {
        DropboxChangesTracker tracker = new DropboxChangesTracker(client, eventPublisher);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = executor.scheduleWithFixedDelay(tracker, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * Unregisters the change listener.
     */
    private void unregisterChangeListener() {
        scheduledFuture.cancel(true);
    }

    /**
     * Resets everything when the dropbox thing is removed from the system.
     *
     * @see org.eclipse.smarthome.core.events.EventSubscriber#receive(org.eclipse.smarthome.core.events.Event)
     */
    @Override
    public void receive(Event event) {
        ThingRemovedEvent e = (ThingRemovedEvent) event;
        if (e.getThing().thingTypeUID.equals(DropboxBindingConstants.THING_TYPE_DROPBOX.toString())) {
            accessToken = null;
            client = null;
            webAuth = null;
            unregisterChangeListener();
        }

    }

}
