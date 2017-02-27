/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.gmail.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import tka.binding.gmail.GmailBindingConstants;
import tka.binding.gmail.automation.EmailParameters;

/**
 * The thing handler for {@link GmailBindingConstants#THING_TYPE_GMAIL} things.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class GmailHandler extends ConfigStatusThingHandler {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(GmailHandler.class);

    /**
     * The constructor.
     *
     * @param thing
     */
    public GmailHandler(Thing thing) {
        super(thing);
    }

    /**
     * @see org.eclipse.smarthome.core.thing.binding.BaseThingHandler#initialize()
     */
    @Override
    public void initialize() {
        logger.debug("Initializing Gmail handler.");
        super.initialize();

    }

    /**
     * @see org.eclipse.smarthome.core.thing.binding.ThingHandler#handleCommand(org.eclipse.smarthome.core.thing.ChannelUID,
     *      org.eclipse.smarthome.core.types.Command)
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handle command: {}", command);
        if (command instanceof StringType) {
            StringType cmd = (StringType) command;
            EmailParameters parameters = new Gson().fromJson(cmd.toString(), EmailParameters.class);
            try {
                sendEmail(parameters);
            } catch (MessagingException e) {
                logger.error("Could not send email", e);
            }
            return;
        }
        logger.info("Command {} is not supported for channel: {}", command, channelUID.getId());
    }

    /**
     * @see org.eclipse.smarthome.config.core.status.ConfigStatusProvider#getConfigStatus()
     */
    @Override
    public Collection<ConfigStatusMessage> getConfigStatus() {
        logger.info("getting config status");
        Collection<ConfigStatusMessage> configStatus = new ArrayList<>();
        return configStatus;
    }

    /**
     * Sends an email with the specified parameters.
     * 
     * @param parameters
     * @throws AddressException
     * @throws MessagingException
     */
    private void sendEmail(EmailParameters parameters) throws AddressException, MessagingException {
        // Step1
        // System.out.println("\n 1st ===> setup Mail Server Properties..");
        Properties mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        // System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        // System.out.println("\n\n 2nd ===> get Mail Session..");
        Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.setFrom(new InternetAddress(GmailBindingConstants.USERNAME));
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(parameters.getTo()));

        // generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@crunchify.com"));
        generateMailMessage.setSubject(parameters.getSubject());
        String emailBody = parameters.getMessage();
        generateMailMessage.setContent(emailBody, "text/html");
        // System.out.println("Mail Session has been created successfully..");

        // Step3
        // System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", GmailBindingConstants.USERNAME, GmailBindingConstants.PASSWORD);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

}
