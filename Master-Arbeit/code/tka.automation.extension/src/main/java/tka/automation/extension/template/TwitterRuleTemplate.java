/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.automation.Action;
import org.eclipse.smarthome.automation.Condition;
import org.eclipse.smarthome.automation.Trigger;
import org.eclipse.smarthome.automation.Visibility;
import org.eclipse.smarthome.automation.template.RuleTemplate;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;

import tka.automation.extension.type.AlwaysTrueConditionType;
import tka.automation.extension.type.TkaTriggerType;
import tka.automation.extension.type.TwitterActionType;

public class TwitterRuleTemplate extends RuleTemplate {

    public static final String UID = "TwitterRuleTemplate";
    public static final String CONFIG_TARGET_TEMPERATURE = "targetTemperature";
    public static final String CONFIG_OPERATION = "operation";
    public static final String TRIGGER_ID = "AirConditionerRuleTrigger";

    public static TwitterRuleTemplate initialize() {

        // initialize triggers
        List<Trigger> triggers = new ArrayList<Trigger>();
        triggers.add(new Trigger(TRIGGER_ID, TkaTriggerType.UID, null));

        // initialize conditions
        // here the tricky part is the giving a value to the condition configuration parameter.
        Map<String, Object> conditionConfig = new HashMap<String, Object>();

        // here the tricky part is the referring into the condition input - trigger output.
        // The syntax is a similar to the JUEL syntax.
        Map<String, String> conditionInputs = new HashMap<String, String>();

        Condition alwaysTrueCondition = new Condition("AlwaysTrueCondition", AlwaysTrueConditionType.UID,
                conditionConfig, conditionInputs);

        List<Condition> conditions = new ArrayList<Condition>();
        conditions.add(alwaysTrueCondition);

        // initialize actions - here the tricky part is the referring into the action configuration parameter - the
        // template configuration parameter. The syntax is a similar to the JUEL syntax.
        Map<String, String> actionConfig = new HashMap<String, String>();

        List<Action> actions = new ArrayList<Action>();
        actions.add(new Action("TwitterAction", TwitterActionType.UID, actionConfig, null));

        // initialize configDescriptions
        List<ConfigDescriptionParameter> configDescriptions = new ArrayList<ConfigDescriptionParameter>();

        // initialize tags
        Set<String> tags = new HashSet<String>();
        tags.add("Tka");
        tags.add("Twitter");

        // create the template
        return new TwitterRuleTemplate(tags, triggers, conditions, actions, configDescriptions);
    }

    public TwitterRuleTemplate(Set<String> tags, List<Trigger> triggers, List<Condition> conditions,
            List<Action> actions, List<ConfigDescriptionParameter> configDescriptions) {
        super(UID, "Tka Rule Template", "Template for creation of a tka rule ", tags, triggers, conditions, actions,
                configDescriptions, Visibility.VISIBLE);
    }
}
