/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.flashui.rule;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleRegistry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This servlet provides the functionality to present existing rules.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class RulesProviderServlet extends HttpServlet {

    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 957446669297705448L;

    /**
     * The rule registry.
     */
    private RuleRegistry ruleRegistry;

    /**
     * The gson object.
     */
    private Gson gson;

    /**
     * The constructor.
     *
     * @param ruleRegistry
     */
    public RulesProviderServlet(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
        gson = new Gson();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("RulesProviderServlet called!");
        resp.setContentType("application/json");

        String uid = req.getParameter("uid");
        if (uid != null) {
            Rule rule = ruleRegistry.get(uid);
            resp.getWriter().print(gson.toJson(Arrays.asList(rule)));
            return;
        }

        Collection<Rule> flashRules = ruleRegistry.getByTag("flash");

        String result = buildJson(flashRules);

        // String result = gson.toJson(flashRules);
        resp.getWriter().print(result);
    }

    /**
     * Builds the json for the rules and adds some additional information.
     * 
     * @param flashRules
     * @return
     */
    private String buildJson(Collection<Rule> flashRules) {
        JsonArray root = gson.toJsonTree(flashRules).getAsJsonArray();
        for (JsonElement rule : root) {
            JsonObject ruleObj = rule.getAsJsonObject();
            String uid = ruleObj.get("uid").getAsString();

            // add enabled info
            Boolean enabled = ruleRegistry.isEnabled(uid);
            ruleObj.addProperty("enabled", enabled);

            // add status info
            String status = ruleRegistry.getStatus(uid).toString();
            ruleObj.addProperty("status", status);
        }

        return gson.toJson(root);
    }
}
