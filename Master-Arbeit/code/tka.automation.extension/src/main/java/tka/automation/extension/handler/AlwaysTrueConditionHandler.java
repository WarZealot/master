/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.handler;

import java.util.Map;

import org.eclipse.smarthome.automation.Condition;
import org.eclipse.smarthome.automation.handler.BaseModuleHandler;
import org.eclipse.smarthome.automation.handler.ConditionHandler;

/**
 * This class serves to handle the Condition types provided by this application. It is used to help the RuleEngine
 * to decide to continue with the execution of the rule or to terminate it.
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class AlwaysTrueConditionHandler extends BaseModuleHandler<Condition> implements ConditionHandler {

    public AlwaysTrueConditionHandler(Condition module) {
        super(module);
    }

    @Override
    public boolean isSatisfied(Map<String, ?> context) {
        return true;
    }

}
