/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.gmail.automation;

/**
 * Model class, used during serialization.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class EmailParameters {

    /**
     * Who should receive the email.
     */
    private String to;

    /**
     * The subject of the email.
     */
    private String subject;

    /**
     * The message body of the email.
     */
    private String message;

    /**
     * The constructor.
     *
     * @param to
     * @param subject
     * @param message
     */
    public EmailParameters(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    /**
     * The default constructor.
     */
    public EmailParameters() {
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
