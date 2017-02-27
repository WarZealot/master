/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.flashui.rule;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleRegistry;
import org.eclipse.smarthome.automation.parser.Parser;

/**
 * This servlet provides the functionality to import new rules.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class RulesImporterServlet extends HttpServlet {

    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 7143308933003157037L;

    /**
     * The rule registry.
     */
    private RuleRegistry ruleRegistry;

    /**
     * The parser.
     */
    private Parser<Rule> parser;

    /**
     * The constructor.
     *
     * @param ruleRegistry
     * @param parser
     */
    public RulesImporterServlet(RuleRegistry ruleRegistry, Parser<Rule> parser) {
        this.ruleRegistry = ruleRegistry;
        this.parser = parser;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        Enumeration<String> names = req.getParameterNames();
        if (!names.hasMoreElements()) {
            resp.getWriter().print("ERROR");
            return;
        }
        String json = names.nextElement();
        InputStreamReader reader = new InputStreamReader(IOUtils.toInputStream(json));
        try {
            Set<Rule> rules = parser.parse(reader);
            for (Rule rule : rules) {
                if (!rule.getTags().contains("flash")) {
                    rule.getTags().add("flash");
                }

                System.out.println("Tags: " + rule.getTags());
                if (ruleRegistry.get(rule.getUID()) != null) {
                    ruleRegistry.update(rule);
                } else {
                    ruleRegistry.add(rule);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().print("ERROR");
            return;
        }
        resp.getWriter().print("SUCCESS");
    }

}
