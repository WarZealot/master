/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.flashui;

import java.util.Collection;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleRegistry;
import org.eclipse.smarthome.automation.parser.Parser;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tka.binding.dropbox.DropboxConnectionService;
import tka.binding.twitter.TwitterConnectionService;
import tka.flashui.bindings.DropboxServlet;
import tka.flashui.bindings.TwitterServlet;
import tka.flashui.bindings.WeatherServlet;
import tka.flashui.rule.RulesDeleterServlet;
import tka.flashui.rule.RulesImporterServlet;
import tka.flashui.rule.RulesProviderServlet;
import tka.flashui.thing.ThingsDeleterServlet;
import tka.flashui.thing.ThingsProviderServlet;

/**
 * The central entrance point of the flash ui.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class FlashUIApp {

    /**
     * The alias under which the resources are provided.
     */
    public static final String WEBAPP_ALIAS = "/flash";

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(FlashUIApp.class);

    /**
     * The http service.
     */
    private HttpService httpService;

    /**
     * The rule registry service.
     */
    private RuleRegistry ruleRegistry;

    /**
     * The bundle context.
     */
    private BundleContext context;

    /**
     * The parser used to parse Rules from json.
     */
    @SuppressWarnings("rawtypes")
    private ServiceReference<Parser> parserReference;

    /**
     * The thing registry.
     */
    private ThingRegistry thingRegistry;

    /**
     * The twitter connnection service.
     */
    private TwitterConnectionService twitterConnectionService;

    /**
     * The dropbox connection service.
     */
    private DropboxConnectionService dropboxConnectionService;

    /**
     * Registers the various servlets.
     *
     * @param componentContext
     */
    @SuppressWarnings("rawtypes")
    protected void activate(ComponentContext componentContext) {
        System.out.println("FlashUIApp.activate()");
        context = componentContext.getBundleContext();
        try {
            Collection<ServiceReference<Parser>> references = context.getServiceReferences(Parser.class,
                    "(" + Parser.PARSER_TYPE + "=" + Parser.PARSER_RULE + ")");
            parserReference = references.iterator().next();
            @SuppressWarnings("unchecked")
            Parser<Rule> parser = context.getService(parserReference);

            // register web resources
            httpService.registerResources(WEBAPP_ALIAS, "web", null);

            // register rule servlets
            httpService.registerServlet("/flashrulesprovider", new RulesProviderServlet(ruleRegistry), null, null);
            httpService.registerServlet("/flashrulesdeleter", new RulesDeleterServlet(ruleRegistry), null, null);
            httpService.registerServlet("/flashrulesimporter", new RulesImporterServlet(ruleRegistry, parser), null,
                    null);

            // register thing servlets
            httpService.registerServlet("/flashthingsprovider", new ThingsProviderServlet(thingRegistry), null, null);
            httpService.registerServlet("/flashthingsdeleter", new ThingsDeleterServlet(thingRegistry), null, null);

            // binding servlets
            httpService.registerServlet("/twitter", new TwitterServlet(twitterConnectionService), null, null);
            httpService.registerServlet("/dropbox", new DropboxServlet(dropboxConnectionService), null, null);
            httpService.registerServlet("/weather", new WeatherServlet(thingRegistry), null, null);

            logger.info("Started Flash UI at " + WEBAPP_ALIAS);
        } catch (Exception e) {
            logger.error("Error during servlet startup", e);
        }
    }

    /**
     * Unregisters the service.
     *
     * @param componentContext
     */
    protected void deactivate(ComponentContext componentContext) {
        httpService.unregister(WEBAPP_ALIAS);
        logger.info("Stopped Flash UI");
    }

    /**
     * Sets the service when it is available in OSGi.
     *
     * @param httpService
     */
    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    /**
     * @param httpService
     */
    public void unsetHttpService(HttpService httpService) {
        this.httpService = null;
    }

    /**
     * Sets the service when it is available in OSGi.
     *
     * @param ruleRegistry
     */
    public void setRuleRegistry(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
    }

    /**
     * Unsets the service when it is no longer available in OSGi.
     * 
     * @param ruleRegistry
     */
    public void unsetRuleRegistry(RuleRegistry ruleRegistry) {
        this.ruleRegistry = null;
    }

    /**
     * Sets the service when it is available in OSGi.
     * 
     * @param thingRegistry
     */
    public void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    /**
     * Unsets the service when it is no longer available in OSGi.
     * 
     * @param thingRegistry
     */
    public void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }

    /**
     * Sets the service when it is available in OSGi.
     * 
     * @param twitterConnectionService
     */
    public void setTwitterConnectionService(TwitterConnectionService twitterConnectionService) {
        this.twitterConnectionService = twitterConnectionService;
    }

    /**
     * Unsets the service when it is no longer available in OSGi.
     * 
     * @param twitterConnectionService
     */
    public void unsetTwitterConnectionService(TwitterConnectionService twitterConnectionService) {
        this.twitterConnectionService = null;
    }

    /**
     * Sets the service when it is available in OSGi.
     * 
     * @param dropboxConnectionService
     */
    public void setDropboxConnectionService(DropboxConnectionService dropboxConnectionService) {
        this.dropboxConnectionService = dropboxConnectionService;
    }

    /**
     * Unsets the service when it is no longer available in OSGi.
     * 
     * @param dropboxConnectionService
     */
    public void unsetDropboxConnectionService(DropboxConnectionService dropboxConnectionService) {
        this.dropboxConnectionService = null;
    }
}
