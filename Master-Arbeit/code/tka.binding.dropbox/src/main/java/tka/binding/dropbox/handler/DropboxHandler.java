/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.dropbox.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.UploadErrorException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tka.binding.dropbox.DropboxBindingConstants;
import tka.binding.dropbox.automation.DropboxUploadEntity;

/**
 * The thing handler for {@link DropboxBindingConstants#THING_TYPE_DROPBOX} things.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class DropboxHandler extends ConfigStatusThingHandler {

    /**
     * The json parser.
     */
    private static final JsonParser JSON_PARSER = new JsonParser();

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(DropboxHandler.class);

    /**
     * The dropbox client.
     */
    private DbxClientV2 client;

    /**
     * The GSON object.
     */
    private static final Gson GSON = new Gson();

    /**
     * @param thing
     */
    public DropboxHandler(Thing thing) {
        super(thing);
    }

    /**
     * @see org.eclipse.smarthome.core.thing.binding.BaseThingHandler#initialize()
     */
    @Override
    public void initialize() {
        logger.debug("Initializing Dropbox handler.");
        super.initialize();

        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        client = new DbxClientV2(dbxRequestConfig,
                (String) getThing().getConfiguration().get(DropboxBindingConstants.KEY_OAUTH_TOKEN));
    }

    /**
     * @see org.eclipse.smarthome.core.thing.binding.ThingHandler#handleCommand(org.eclipse.smarthome.core.thing.ChannelUID,
     *      org.eclipse.smarthome.core.types.Command)
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handle command: {}", command);
        printCurrentAccount();

        if (command instanceof StringType) {
            DropboxUploadEntity uploadEntity = GSON.fromJson(command.toString(), DropboxUploadEntity.class);

            try {
                String mediaUrl = uploadEntity.getMediaUrl();
                if (mediaUrl.contains("pathLower") && mediaUrl.contains("serverModified")) {
                    InputStream fileInputStream = getFileInputStream(mediaUrl);
                    String filename = JSON_PARSER.parse(mediaUrl).getAsJsonObject().get("name").getAsString();
                    uploadFile(fileInputStream, uploadEntity.getDirectory() + filename);
                    return;
                }

                URL url = new URL(mediaUrl);
                String filename = FilenameUtils.getName(url.getPath());
                InputStream stream = url.openStream();
                uploadFile(stream, uploadEntity.getDirectory() + filename);
            } catch (IOException | DbxException e) {
                logger.info("Command {} has invalid configuration/context parameters.", command);
            }
            return;
        }

        logger.info("Command {} is not supported for channel: {}", command, channelUID.getId());
    }

    /**
     * Gets an input stream from a JSON that contains a dropbox path.
     *
     * @param dropboxJson
     * @return
     * @throws DownloadErrorException
     * @throws DbxException
     */
    private InputStream getFileInputStream(String dropboxJson) throws DownloadErrorException, DbxException {
        JsonObject root = JSON_PARSER.parse(dropboxJson).getAsJsonObject();
        String path = root.get("pathLower").getAsString();
        return client.files().download(path).getInputStream();
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
     * Helper method, prints the status.
     *
     * @param newStatus
     */
    private void printCurrentAccount() {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        DbxClientV2 dbxClient = new DbxClientV2(dbxRequestConfig,
                (String) getThing().getConfiguration().get(DropboxBindingConstants.KEY_OAUTH_TOKEN));
        try {
            logger.info("Dropbox Account Name: " + dbxClient.users().getCurrentAccount().getName());
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uploads a file to the specified location.
     *
     * @param in
     * @param path
     * @throws UploadErrorException
     * @throws DbxException
     * @throws IOException
     */
    private void uploadFile(InputStream in, String path) throws UploadErrorException, DbxException, IOException {
        client.files().uploadBuilder(path).uploadAndFinish(in);
        logger.info("Uploaded file to dropbox:  " + path);
    }
}
