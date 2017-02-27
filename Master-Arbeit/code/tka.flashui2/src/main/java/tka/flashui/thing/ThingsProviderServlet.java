/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.flashui.thing;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;

import com.google.gson.Gson;

/**
 * This servlet provides the functionality to present existing rules.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class ThingsProviderServlet extends HttpServlet {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 957446669297705448L;

    /**
     * The gson object.
     */
    private Gson gson;

    /**
     * The thing registry.
     */
    private ThingRegistry thingRegistry;

    /**
     * The constructor.
     * 
     * @param thingRegistry
     */
    public ThingsProviderServlet(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
        gson = new Gson();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ThingsProviderServlet.doGet()");
        resp.setContentType("application/json");

        Collection<Thing> things = thingRegistry.getAll();
        String result = gson.toJson(things);
        System.out.println(result);
        resp.getWriter().print(result);
    }
}
