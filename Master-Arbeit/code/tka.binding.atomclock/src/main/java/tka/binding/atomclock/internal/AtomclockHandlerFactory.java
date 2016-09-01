package tka.binding.atomclock.internal;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;

import tka.binding.atomclock.AtomclockBindingConstants;
import tka.binding.atomclock.handler.AtomclockHandler;

/**
 * @author ktkachuk
 *
 */
public class AtomclockHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .singleton(AtomclockBindingConstants.THING_TYPE_ATOMCLOCK);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(AtomclockBindingConstants.THING_TYPE_ATOMCLOCK)) {
            return new AtomclockHandler(thing);
        }

        return null;
    }
}
