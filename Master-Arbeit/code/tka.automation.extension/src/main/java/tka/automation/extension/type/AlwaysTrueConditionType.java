/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.type;

import java.util.Collections;

import org.eclipse.smarthome.automation.Visibility;
import org.eclipse.smarthome.automation.type.ConditionType;
import org.eclipse.smarthome.automation.type.Input;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;

/**
 * The purpose of this class is to illustrate how to create {@link ConditionType}
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class AlwaysTrueConditionType extends ConditionType {

    public static final String UID = "AlwaysTrueCondition";

    public static AlwaysTrueConditionType initialize() {
        return new AlwaysTrueConditionType();
    }

    public AlwaysTrueConditionType() {
        super(UID, Collections.<ConfigDescriptionParameter>emptyList(), "Always True Condition Template",
                "Template for creation of an Always True Condition.", null, Visibility.VISIBLE,
                Collections.<Input>emptyList());
    }
}
