/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.flashui.bindings;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The weather servlet provides the ability to add additional weather things to the system.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class WeatherServlet extends HttpServlet {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 275417185547581863L;

    /**
     * The thing registry.
     */
    private ThingRegistry thingRegistry;

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(WeatherServlet.class);

    /**
     * The constructor.
     *
     * @param thingRegistry
     */
    public WeatherServlet(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WeatherServlet.doGet()");
        resp.setContentType("text/plain");

        String location = req.getParameter("location");
        int refresh = 0;
        try {
            refresh = Integer.parseInt(req.getParameter("refresh"));
        } catch (Exception e) {
            logger.debug("Could not read refresh period from request. Leaving default refresh=0");
        }

        createThingForLocation(location, refresh);
        resp.getWriter().print("SUCCESS");
    }

    /**
     * The location key.
     */
    public static final String CONFIG_LOCATION = "location";

    /**
     * The refresh key.
     */
    public static final String CONFIG_REFRESH = "refresh";

    /**
     * The thing type uid.
     */
    public final static ThingTypeUID THING_TYPE_WEATHER = new ThingTypeUID("weather", "weather");

    /**
     * Creates a weather thing with the specified configuration parameters.
     * 
     * @param location
     * @param refreshInSeconds
     */
    private void createThingForLocation(String location, int refreshInSeconds) {
        Configuration configuration = new Configuration();
        configuration.put(CONFIG_LOCATION, location);
        configuration.put(CONFIG_REFRESH, refreshInSeconds);

        ThingUID thingUID = new ThingUID("weather", "weatherThingTypeId", location);

        Thing thing = thingRegistry.createThingOfType(THING_TYPE_WEATHER, thingUID, null, location.toUpperCase(),
                configuration);
        thingRegistry.add(thing);
    }

}
