
package tka.binding.dropbox;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * @author ktkachuk
 *
 */
public class DropboxBindingConstants {

    public static final String BINDING_ID = "dropbox";

    // List all Thing Type UIDs, related to the Dropbox Binding
    public final static ThingTypeUID THING_TYPE_DROPBOX = new ThingTypeUID(BINDING_ID, "dropbox");

    // List all channels
    public static final String CHANNEL_FOLDER = "folder";

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_DROPBOX);

    public final static String OAUTH_CONSUMER_KEY = "czrkhe4jhjqrxkv";
    public final static String OAUTH_CONSUMER_SECRET = "cxaeslwf8paz5il";
    public static final String KEY_OAUTH_TOKEN = "oauthToken";

    public final static String TOPIC = "flash/dropbox/";
    // public final static String TOPIC_STATUS_CHANGED = TOPIC + "status";
    public final static String TOPIC_DELETED = TOPIC + "deleted";
    public final static String TOPIC_ADDED = TOPIC + "added";
    public final static String TOPIC_CHANGED = TOPIC + "status";

    public final static String SOURCE = "flash/dropbox";
}
