/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleProvider;
import org.eclipse.smarthome.core.common.registry.ProviderChangeListener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import tka.automation.extension.template.TwitterRuleTemplate;

/**
 * This class presents simple implementation of {@link RuleProvider} interface. It provides four rules that give ability
 * to the user to switch on the air conditioner and lights, and to lower the blinds in its home remotely.
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class FlashRulesProvider implements RuleProvider {

    public static final String CONFIG_UNIT = "unit";
    public static final String CONFIG_EXPECTED_RESULT = "expectedResult";

    static final String TWITTER_UID = "TwitterRule";

    Map<String, Rule> rules;

    @SuppressWarnings("rawtypes")
    private ServiceRegistration providerReg;
    private Collection<ProviderChangeListener<Rule>> listeners;

    /**
     * This method is used to initialize the provided rules by using templates and from scratch.
     * The configuration of the rule created by template should contain as keys all required parameter names of the
     * configuration of the template and their values.
     * In this example the UIDs of the rules is given by the provider, but can be <code>null</code>.
     * Then the RuleEngine will generate the UID for each provided rule.
     */
    public FlashRulesProvider() {

        Rule twitterRule = createTwitterRuleFromTemplate();

        rules = new HashMap<String, Rule>();
        rules.put(TWITTER_UID, twitterRule);
    }

    @Override
    public void addProviderChangeListener(ProviderChangeListener<Rule> listener) {
        if (listeners == null) {
            listeners = new ArrayList<ProviderChangeListener<Rule>>();
        }
        listeners.add(listener); // keep all listeners, interested about changing the rules to can inform them if
                                 // there is change on some rules
    }

    @Override
    public Collection<Rule> getAll() {
        return rules.values(); // adding the provided rules into RuleEngine
    }

    @Override
    public void removeProviderChangeListener(ProviderChangeListener<Rule> listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * This method is used to update the provided rules configuration.
     *
     * @param uid
     *            specifies the rule for updating by UID
     * @param template
     *            specifies the rule template by UID
     * @param config
     *            gives the new configuration of the rule
     */
    public void update(String uid, String template, Map<String, Object> config) {

        // specific for this application
        Rule oldelement = rules.get(uid);
        Rule element = new Rule(uid);
        element.setTemplateUID(template);
        element.setConfiguration(config);
        rules.put(uid, element);

        // inform all listeners, interested about changing of the rules
        for (ProviderChangeListener<Rule> listener : listeners) {
            listener.updated(this, oldelement, element);
        }
    }

    /**
     * This method is used for registration of the FlashRulesProvider as a {@link RuleProvider} service.
     *
     * @param bc
     *            is a bundle's execution context within the Framework.
     */
    public void register(BundleContext bc) {
        providerReg = bc.registerService(RuleProvider.class.getName(), this, null);
    }

    /**
     * This method is used to unregister the FlashRulesProvider service.
     */
    public void unregister() {
        providerReg.unregister();
        providerReg = null;
        rules = null;
    }

    private Rule createTwitterRuleFromTemplate() {
        Map<String, Object> config = new HashMap<String, Object>();
        Rule rule = new Rule(TWITTER_UID);
        rule.setTemplateUID(TwitterRuleTemplate.UID);
        rule.setConfiguration(config);
        return rule;
    }
}
