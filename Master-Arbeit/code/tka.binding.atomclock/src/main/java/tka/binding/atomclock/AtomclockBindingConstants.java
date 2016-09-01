
package tka.binding.atomclock;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * @author ktkachuk
 *
 */
public class AtomclockBindingConstants {

    public static final String BINDING_ID = "atomclock";

    // List all Thing Type UIDs, related to the Atomclock Binding
    public final static ThingTypeUID THING_TYPE_ATOMCLOCK = new ThingTypeUID(BINDING_ID, "atomclock");

    // List all channels
    public static final String CHANNEL_TIME = "time";

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_ATOMCLOCK);
}
