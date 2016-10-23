/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.automation.extension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.io.console.Console;
import org.eclipse.smarthome.io.console.extensions.AbstractConsoleCommandExtension;
import org.eclipse.smarthome.io.console.extensions.ConsoleCommandExtension;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import tka.automation.extension.handler.FlashHandlerFactory;
import tka.automation.extension.handler.TkaTriggerHandler;

/**
 * This class serves as a simple user interface. User can use it to configure the provided rules,
 * to trigger their execution or to discard the requested execution if it is still pending.
 * It is written like this, to allow the user to test the behavior without need to write additional code.
 * Can be replaced by GUI or another service, which can deliver the necessary information to allow the rules to work.
 *
 * @author Ana Dimova - Initial Contribution
 *
 */
public class FlashCommands extends AbstractConsoleCommandExtension {

    private static final String CMD = "flash_commands";
    private static final String DESC = "Tka Application Commands Group";

    private static final String COMMAND_TRIGGER_TKA_TRIGGER = "triggerTkaTrigger";

    private FlashHandlerFactory handlerFactory;
    @SuppressWarnings("rawtypes")
    private ServiceRegistration commandsServiceReg;

    /**
     * This method is used to initialize the commands service, provider of the rules and factory for the handlers of the
     * modules that compose the rules.
     *
     * @param context
     *            is a bundle's execution context within the Framework.
     */
    public FlashCommands(BundleContext bc, FlashRulesProvider rulesProvider, FlashHandlerFactory handlerFactory) {
        super(CMD, DESC);
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void execute(String[] args, Console console) {
        if (args.length == 0) {
            console.println(StringUtils.join(getUsages(), "\n"));
            return;
        }

        triggerTkaTrigger(args, console);
    }

    @Override
    public List<String> getUsages() {
        return Arrays.asList(new String[] { buildCommandUsage(COMMAND_TRIGGER_TKA_TRIGGER, "Just write it!") });
    }

    /**
     * This method is used to register the commands service, provider of the rules and handlers of the modules that
     * compose the rules.
     *
     * @param context
     *            is a bundle's execution context within the Framework.
     */
    public void register(BundleContext context) {
        commandsServiceReg = context.registerService(ConsoleCommandExtension.class.getName(), this, null);
    }

    /**
     * This method is used to unregister the commands service, provider of the rules and handlers of the modules that
     * compose the rules.
     */
    public void unregister() {
        if (commandsServiceReg != null) {
            commandsServiceReg.unregister();
        }
        commandsServiceReg = null;
        handlerFactory = null;
    }

    /**
     * This method is used to schedule the execution of the provided rules. It gives ability to provide external data,
     * that can affect the execution of the rules.
     *
     * @param params
     *            provides external data, that can affect the execution of the rules.
     * @param console
     *            provides the output of the command.
     */
    private void triggerTkaTrigger(String[] params, Console console) {

        // parsing command parameter values
        if (params.length < 1) {
            console.println("Missing required parameters");
            return;
        }
        if (params[0] == null || (!params[0].equalsIgnoreCase(COMMAND_TRIGGER_TKA_TRIGGER))) {
            return;
        }

        // initialize the output of the trigger of the rule FlashRulesProvider.AC_UID
        Map<String, Object> context = new HashMap<String, Object>();

        // causes the execution of the rule FlashRulesProvider.AC_UID
        TkaTriggerHandler handler = handlerFactory.getTriggerHandler(FlashRulesProvider.TWITTER_UID);
        System.out.println("Handler is: " + handler);
        if (handler != null) {
            handler.trigger(context);
        }

        console.println("SUCCESS");
    }

}
