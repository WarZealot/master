/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension.type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.smarthome.automation.Visibility;
import org.eclipse.smarthome.automation.type.Output;
import org.eclipse.smarthome.automation.type.TriggerType;

/**
 * The purpose of this class is to illustrate how to create {@link TriggerType}
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class TkaTriggerType extends TriggerType {

    public static final String UID = "TkaTrigger";

    public static TkaTriggerType initialize() {
        return new TkaTriggerType();
    }

    public TkaTriggerType() {
        super(UID, null, "TkaTrigger", "Template for creation of an Tka Rule Trigger.", null, Visibility.VISIBLE,
                getDefaultOutput());
    }

    private static List<Output> getDefaultOutput() {
        Output state = new Output("CurrentTime", String.class.getName(), "CurrentTime", "Indicates the current time",
                null, null, new Date().toString());
        List<Output> output = new ArrayList<Output>();
        output.add(state);
        return output;
    }
}
