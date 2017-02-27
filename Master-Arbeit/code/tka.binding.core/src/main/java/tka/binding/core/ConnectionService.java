/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.core;

/**
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public interface ConnectionService {

    /**
     * Can be used for OAuth authorization, for example.
     *
     * @return the URL which is used by the user to grant access to his application
     */
    public Object requestAuthorization();

    /**
     * Accepts the necessary user input.
     *
     * @param information some generic information. Could be the PIN in OAuth.
     * @return <code>true</code>, if the service can confirm that authorization was granted successfully.
     */
    public boolean authorizationGrantedCallback(Object information);

    /**
     * Gives information, whether an authorization is needed.
     *
     * @return <code>true</code>, if the connection service has authorization to act on behalf of the user
     */
    public boolean isAuthorized();

}
