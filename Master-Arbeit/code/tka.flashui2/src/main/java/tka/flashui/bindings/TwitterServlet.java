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

import tka.binding.twitter.TwitterConnectionService;

/**
 * The twitter servlet provides the ability to connect the account to flash.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class TwitterServlet extends HttpServlet {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 957446669297705448L;

    /**
     * The connection service.
     */
    private TwitterConnectionService connectionService;

    /**
     * The constructor.
     * 
     * @param service
     */
    public TwitterServlet(TwitterConnectionService service) {
        this.connectionService = service;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("TwitterServlet.doGet()");
        resp.setContentType("text/plain");

        String status = req.getParameter("status");
        if (status != null) {
            resp.getWriter().print("" + connectionService.isAuthorized());
            return;
        }

        String pin = req.getParameter("pin");
        if (pin != null) {
            boolean success = connectionService.authorizationGrantedCallback(pin);
            resp.getWriter().print("" + success);
            return;
        }

        Object url = connectionService.requestAuthorization();
        resp.getWriter().print(url.toString());
    }
}
