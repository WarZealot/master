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
import org.eclipse.smarthome.automation.type.ActionType;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;

/**
 * @author Konstantin
 *
 */
public class TwitterActionType extends ActionType {

    public static final String UID = "TwitterAction";

    public static TwitterActionType initialize() {
        return new TwitterActionType();
    }

    public TwitterActionType() {
        super(UID, Collections.<ConfigDescriptionParameter>emptyList(), "Twitter Action Template",
                "Template for creation of a Twitter Action.", null, Visibility.VISIBLE, null, null);
    }
}
