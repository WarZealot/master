/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.type;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.automation.Visibility;
import org.eclipse.smarthome.automation.type.ActionType;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter.Type;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameterBuilder;

/**
 * @author Konstantin
 *
 */
public class DropboxActionType extends ActionType {

    public static final String UID = "DropboxAction";
    public static final String CONFIG_ITEM_NAME = "itemName";
    public static final String CONFIG_DIRECTORY = "directory";

    public DropboxActionType() {
        super(UID, getConfigParameters(), "Dropbox Action Template", "Template for creation of a Dropbox Action.", null,
                Visibility.VISIBLE, null, null);
    }

    private static List<ConfigDescriptionParameter> getConfigParameters() {
        final ConfigDescriptionParameter device = ConfigDescriptionParameterBuilder.create(CONFIG_ITEM_NAME, Type.TEXT)
                .withRequired(true).withReadOnly(true).withMultiple(false).withLabel("Device")
                .withDescription("Device description").build();
        final ConfigDescriptionParameter directory = ConfigDescriptionParameterBuilder
                .create(CONFIG_DIRECTORY, Type.TEXT).withRequired(true).withReadOnly(true).withMultiple(false)
                .withLabel("Directory").withDescription("Directory with trailing /").build();

        List<ConfigDescriptionParameter> list = new ArrayList<>();
        list.add(device);
        list.add(directory);
        return list;
    }
}
