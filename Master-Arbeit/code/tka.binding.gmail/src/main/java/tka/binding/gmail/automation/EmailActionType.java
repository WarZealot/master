/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.gmail.automation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.automation.Visibility;
import org.eclipse.smarthome.automation.type.ActionType;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter.Type;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameterBuilder;

/**
 * The email action type.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class EmailActionType extends ActionType {

    /**
     * The unique identifier of this type.
     */
    public static final String UID = "EmailAction";

    /**
     * The name of the configuration parameter.
     */
    public static final String CONFIG_SUBJECT = "subject";

    /**
     * The name of the configuration parameter.
     */
    public static final String CONFIG_TO = "to";

    /**
     * The name of the configuration parameter.
     */
    public static final String CONFIG_MESSAGE = "message";

    /**
     * The constructor.
     */
    public EmailActionType() {
        super(UID, getConfigParameters(), "Twitter Action Template", "Template for creation of a Twitter Action.", null,
                Visibility.VISIBLE, null, null);
    }

    /**
     * Builds the configuration parameters.
     * 
     * @return the config parameters.
     */
    private static List<ConfigDescriptionParameter> getConfigParameters() {
        final ConfigDescriptionParameter device = ConfigDescriptionParameterBuilder.create(CONFIG_SUBJECT, Type.TEXT)
                .withRequired(true).withReadOnly(true).withMultiple(false).withLabel("Subject")
                .withDescription("Subject description").build();
        final ConfigDescriptionParameter to = ConfigDescriptionParameterBuilder.create(CONFIG_TO, Type.TEXT)
                .withRequired(true).withReadOnly(true).withMultiple(false).withLabel("To").build();
        final ConfigDescriptionParameter message = ConfigDescriptionParameterBuilder.create(CONFIG_MESSAGE, Type.TEXT)
                .withRequired(true).withReadOnly(true).withMultiple(false).withLabel("Message").build();

        List<ConfigDescriptionParameter> list = new ArrayList<>();
        list.add(to);
        list.add(device);
        list.add(message);
        return list;
    }
}
