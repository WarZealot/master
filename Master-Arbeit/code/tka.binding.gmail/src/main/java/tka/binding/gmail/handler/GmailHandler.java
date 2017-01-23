package tka.binding.gmail.handler;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.smarthome.config.core.status.ConfigStatusMessage;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ConfigStatusThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ktkachuk
 *
 */
public class GmailHandler extends ConfigStatusThingHandler {

    private final Logger logger = LoggerFactory.getLogger(GmailHandler.class);

    public GmailHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Gmail handler.");
        super.initialize();

    }

    @Override
    public void dispose() {
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handle command: {}", command);
        if (command instanceof StringType) {
            StringType cmd = (StringType) command;
            // do something
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

}
