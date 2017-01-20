package tka.binding.dropbox.internal;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;

import tka.binding.dropbox.DropboxBindingConstants;
import tka.binding.dropbox.handler.DropboxHandler;

/**
 * @author ktkachuk
 *
 */
public class DropboxHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .singleton(DropboxBindingConstants.THING_TYPE_DROPBOX);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(DropboxBindingConstants.THING_TYPE_DROPBOX)) {
            return new DropboxHandler(thing);
        }

        return null;
    }
}
