/**
 *
 */
package tka.binding.dropbox.internal;

import java.util.TreeMap;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeletedMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.google.gson.Gson;

import tka.binding.dropbox.DropboxBindingConstants;
import tka.binding.twitter.internal.TwitterEvent;

/**
 * @author Konstantin
 *
 */
public class DropboxChangesTracker implements Runnable {

    private static final Gson GSON = new Gson();
    private final Logger logger = LoggerFactory.getLogger(DropboxChangesTracker.class);
    private final DbxClientV2 client;
    private final EventPublisher eventPublisher;
    private TreeMap<String, Metadata> children;
    private String cursor;

    public DropboxChangesTracker(DbxClientV2 client, EventPublisher eventPublisher) {
        this.client = client;
        this.eventPublisher = eventPublisher;
        cursor = null;
        children = new TreeMap<String, Metadata>();
    }

    @Override
    public void run() {
        try {
            ListFolderResult result;

            if (cursor == null) {
                // result = client.files().listFolder("");
                result = client.files().listFolderBuilder("").withRecursive(true).start();
            } else {
                result = client.files().listFolderContinue(cursor);
            }
            cursor = result.getCursor();
            for (Metadata md : result.getEntries()) {
                if (md instanceof DeletedMetadata) {
                    children.remove(md.getPathLower());
                    publishEvent(DropboxBindingConstants.TOPIC_DELETED, GSON.toJson(md));
                } else {
                    if (children.containsKey(md.getPathLower())) {
                        publishEvent(DropboxBindingConstants.TOPIC_CHANGED, GSON.toJson(md));
                    } else {
                        publishEvent(DropboxBindingConstants.TOPIC_ADDED, GSON.toJson(md));
                    }
                    children.put(md.getPathLower(), md);
                }
            }
        } catch (Exception e) {
            logger.error("Could not poll changes in dropbox.");
        }
    }

    private void publishEvent(final String topic, final String payload) {
        Event event = new Event() {
            @Override
            public String getType() {
                return TwitterEvent.TYPE;
            }

            @Override
            public String getTopic() {
                return topic;
            }

            @Override
            public String getSource() {
                return DropboxBindingConstants.SOURCE;
            }

            @Override
            public String getPayload() {
                return payload;
            }
        };
        eventPublisher.post(event);
    }
}
