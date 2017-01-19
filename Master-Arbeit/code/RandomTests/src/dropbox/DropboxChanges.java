/**
 *
 */
package dropbox;

import java.util.TreeMap;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeletedMetadata;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

/**
 * @author Konstantin
 *
 */
public class DropboxChanges {

    public final static String OAUTH_CONSUMER_KEY = "czrkhe4jhjqrxkv";
    public final static String OAUTH_CONSUMER_SECRET = "cxaeslwf8paz5il";
    public final static String OAUTH_TOKEN = "82kIknP0uEIAAAAAAAAADIFarwXp0OnQ1wJDKJ7cYIPdO4BtrQ5R1EbO2J1ZM7m7";

    /**
     * @param args
     * @throws DbxException
     * @throws ListFolderErrorException
     */
    public static void main(String[] args) throws ListFolderErrorException, DbxException {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        DbxClientV2 client = new DbxClientV2(dbxRequestConfig, OAUTH_TOKEN);
        System.out.println("Dropbox Account Name: " + client.users().getCurrentAccount().getName());

        TreeMap<String, Metadata> children = new TreeMap<String, Metadata>();
        ListFolderResult result;

        String cursor = null;

        while (true) {
            if (cursor == null) {
                result = client.files().listFolder("/testfolder");
            } else {
                result = client.files().listFolderContinue(cursor);
            }
            cursor = result.getCursor();
            for (Metadata md : result.getEntries()) {
                if (md instanceof DeletedMetadata) {
                    children.remove(md.getPathLower());
                    System.out.println("Deleted: " + md.getPathLower());
                } else {
                    children.put(md.getPathLower(), md);
                    System.out.println("State: " + md.getPathLower());
                    System.out.println(md.toString());
                }
            }

            if (!result.getHasMore()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}
