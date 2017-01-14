package tka.binding.dropbox.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;

import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import tka.binding.dropbox.DropboxBindingConstants;

/**
 * @author ktkachuk
 *
 */
public class DropboxHandler extends ConfigStatusThingHandler {

    private final Logger logger = LoggerFactory.getLogger(DropboxHandler.class);

    private String timeData = null;

    ScheduledFuture<?> refreshJob;

    private BigDecimal refresh;

    public DropboxHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Dropbox handler.");
        super.initialize();

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

        // logger.info("Command {} is not supported for channel: {}", command, channelUID.getId());
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
}
