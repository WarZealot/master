package tka.binding.dropbox.handler;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;

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
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.google.gson.Gson;

import tka.binding.dropbox.DropboxBindingConstants;
import tka.binding.dropbox.DropboxUploadEntity;

/**
 * @author ktkachuk
 *
 */
public class DropboxHandler extends ConfigStatusThingHandler {

    private final Logger logger = LoggerFactory.getLogger(DropboxHandler.class);

    private String timeData = null;

    ScheduledFuture<?> refreshJob;

    private BigDecimal refresh;

    private DbxClientV2 client;

    private static final Gson GSON = new Gson();

    public DropboxHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Dropbox handler.");
        super.initialize();

        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        client = new DbxClientV2(dbxRequestConfig,
                (String) getThing().getConfiguration().get(DropboxBindingConstants.KEY_OAUTH_TOKEN));

        if (refresh == null) {
            refresh = new BigDecimal(30);
        }
    }

    @Override
    public void dispose() {
        if (refreshJob != null) {
            refreshJob.cancel(true);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handle command: {}", command);
        printCurrentAccount();

        if (command instanceof StringType) {
            DropboxUploadEntity uploadEntity = GSON.fromJson(command.toString(), DropboxUploadEntity.class);

            try {
                URL url = new URL(uploadEntity.getMediaUrl());
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

    @Override
    public Collection<ConfigStatusMessage> getConfigStatus() {
        logger.info("getting config status");
        Collection<ConfigStatusMessage> configStatus = new ArrayList<>();
        return configStatus;
    }

    /**
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

    private void uploadFile(InputStream in, String path) throws UploadErrorException, DbxException, IOException {
        FileMetadata metadata = client.files().uploadBuilder(path).uploadAndFinish(in);
        logger.info("Uploaded file to dropbox:  " + path);
    }
}
