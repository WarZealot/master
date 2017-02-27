/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.flashui.rule;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleRegistry;

import com.google.gson.Gson;

/**
 * This servlet provides the functionality to delete rules.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class RulesDeleterServlet extends HttpServlet {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 6401293401641761963L;

    /**
     * The rule registry.
     */
    private RuleRegistry ruleRegistry;

    /**
     * The constructor.
     *
     * @param ruleRegistry
     */
    public RulesDeleterServlet(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("RulesDeleterServlet called!");
        resp.setContentType("application/json");

        if (req.getParameter("toggle") != null) {
            String uid = req.getParameter("toggle");
            Boolean enabled = ruleRegistry.isEnabled(uid);
            ruleRegistry.setEnabled(uid, !enabled);
            resp.getWriter().print(new Gson().toJson("SUCCESS"));
            return;
        }

        boolean deleted = handleDelete(req);
        String result = deleted ? "SUCCESS" : "ERROR";

        resp.getWriter().print(new Gson().toJson(result));
    }

    /**
     * @param req
     * @return <code>true</code>, if a rule was removed
     */
    private boolean handleDelete(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();
        String[] deletes = parameters.get("delete");
        if (deletes == null || deletes.length == 0) {
            return false;
        }
        Rule removed = ruleRegistry.remove(deletes[0]);
        return removed != null;
    }
}
