package tka.binding.twitter.internal;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;

import tka.binding.twitter.TwitterBindingConstants;
import tka.binding.twitter.handler.TwitterHandler;

/**
 * @author ktkachuk
 *
 */
public class TwitterHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .singleton(TwitterBindingConstants.THING_TYPE_TWITTER);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(TwitterBindingConstants.THING_TYPE_TWITTER)) {
            return new TwitterHandler(thing);
        }

        return null;
    }
}
