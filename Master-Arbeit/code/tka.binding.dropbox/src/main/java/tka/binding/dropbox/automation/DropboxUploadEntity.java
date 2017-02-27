/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.dropbox.automation;

/**
 * A model class that is used during JSON de/serialization.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class DropboxUploadEntity {

    /**
     * The directory where the file should be saved.
     */
    private String directory;

    /**
     * The media url, from which the file should be uploaded.
     */
    private String mediaUrl;

    /**
     * Default constructor.
     */
    public DropboxUploadEntity() {
    }

    /**
     * The constructor.
     *
     * @param directory
     * @param mediaUrl
     */
    public DropboxUploadEntity(String directory, String mediaUrl) {
        this.directory = directory;
        this.mediaUrl = mediaUrl;
    }

    /**
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * @return the mediaUrl
     */
    public String getMediaUrl() {
        return mediaUrl;
    }

    /**
     * @param mediaUrl the mediaUrl to set
     */
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

}
