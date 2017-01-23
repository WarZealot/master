
package tka.binding.gmail;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * @author ktkachuk
 *
 */
public class GmailBindingConstants {

    public static final String BINDING_ID = "gmail";

    // List all Thing Type UIDs, related to the Gmail Binding
    public final static ThingTypeUID THING_TYPE_GMAIL = new ThingTypeUID(BINDING_ID, "gmail");

    // List all channels
    public static final String CHANNEL_STATUS = "inbox";

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_GMAIL);

    public final static String OAUTH_CONSUMER_KEY = "";
    public final static String OAUTH_CONSUMER_SECRET = "";

    public static final String KEY_OAUTH_TOKEN_SECRET = "";
    public static final String KEY_OAUTH_TOKEN = "";

    public final static String SOURCE = "flash/gmail";
    private final static String TOPIC = SOURCE + "/";
    public final static String TOPIC_STATUS_CHANGED = TOPIC + "inbox";
    public final static String TOPIC_MEDIA = TOPIC + "media";
    public final static String TOPIC_MESSAGE = TOPIC + "message";

}
