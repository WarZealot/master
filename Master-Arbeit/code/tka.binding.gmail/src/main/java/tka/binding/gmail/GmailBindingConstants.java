
package tka.binding.gmail;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;

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
    public static final String CHANNEL_EMAIL = "email";

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

    public static final ThingUID GMAIL_CONNECTION = new ThingUID("gmail", "gmailThingTypeId", "gmailconnection");

    public static final String GMAIL_ITEM_NAME = "gmail_gmailThingTypeId_gmailconnection_email";

    public static final String USERNAME = "noreply.flash.ma@gmail.com";
    public static final String PASSWORD = "a1b1c1d1";

}
