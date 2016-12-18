
package tka.binding.twitter;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * @author ktkachuk
 *
 */
public class TwitterBindingConstants {

    public static final String BINDING_ID = "twitter";

    // List all Thing Type UIDs, related to the Twitter Binding
    public final static ThingTypeUID THING_TYPE_TWITTER = new ThingTypeUID(BINDING_ID, "twitter");

    // List all channels
    public static final String CHANNEL_STATUS = "status";

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_TWITTER);

    public final static String OAUTH_CONSUMER_KEY = "9F8YmBxVk6o7XZfGNLgQieREN";
    public final static String OAUTH_CONSUMER_SECRET = "avJ03ScGJwMCR3GmZ1FkFtbDiGk7eJcts9R61Hy3BvXgVMPie7";

    public final static String TOPIC_STATUS_CHANGED = "flash/twitter/status";
    public final static String SOURCE = "flash/twitter";
}
