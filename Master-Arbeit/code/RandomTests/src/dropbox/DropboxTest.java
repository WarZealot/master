/**
 *
 */
package dropbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Konstantin
 *
 */
public class DropboxTest {
    public final static String OAUTH_CONSUMER_KEY = "czrkhe4jhjqrxkv";
    public final static String OAUTH_CONSUMER_SECRET = "cxaeslwf8paz5il";
    public final static String OAUTH_TOKEN = "82kIknP0uEIAAAAAAAAADIFarwXp0OnQ1wJDKJ7cYIPdO4BtrQ5R1EbO2J1ZM7m7";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        DbxClientV2 dbxClient = new DbxClientV2(dbxRequestConfig, OAUTH_TOKEN);
        System.out.println("Dropbox Account Name: " + dbxClient.users().getCurrentAccount().getName());

        listFiles(dbxClient);

        // try {
        // authDropbox(OAUTH_CONSUMER_KEY, OAUTH_CONSUMER_SECRET);
        // } catch (IOException | DbxException e) {
        // e.printStackTrace();
        // }
    }

    private static void listFiles(DbxClientV2 client) {
        try {
            ListFolderResult folderResult = client.files().listFolder("/ifttt/ifttt_target_15");
            // System.out.println(folderResult.toStringMultiline());
            List<Metadata> entries = folderResult.getEntries();
            JsonParser parser = new JsonParser();
            for (Metadata entry : entries) {
                System.out.println(entry.getName());
                String json = entry.toString();
                JsonObject root = parser.parse(json).getAsJsonObject();
                String date = root.get("server_modified").getAsString();
                System.out.println(date);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                Date result = format.parse(date);
                System.out.println(result);

            }
        } catch (DbxException | ParseException e) {
            e.printStackTrace();
        }

    }

    public static DbxClientV2 authDropbox(String dropBoxAppKey, String dropBoxAppSecret)
            throws IOException, DbxException {
        DbxAppInfo dbxAppInfo = new DbxAppInfo(dropBoxAppKey, dropBoxAppSecret);
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();
        DbxWebAuth webAuth = new DbxWebAuth(dbxRequestConfig, dbxAppInfo);
        String url = webAuth.authorize(authRequest);

        System.out.println("1. Authorize: Go to URL and click Allow : " + url);
        System.out.println("2. Auth Code: Copy authorization code and input here ");
        String dropboxAuthCode = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        DbxAuthFinish authFinish = webAuth.finishFromCode(dropboxAuthCode);
        String authAccessToken = authFinish.getAccessToken();
        System.out.println("Access token is: " + authAccessToken);
        DbxClientV2 dbxClient = new DbxClientV2(dbxRequestConfig, authAccessToken);
        System.out.println("Dropbox Account Name: " + dbxClient.users().getCurrentAccount().getName());

        return dbxClient;
    }

    public static void collectCreateTimes(DbxClientV2 client) throws Exception {
        ListFolderResult listFolder = client.files().listFolder("testfolder");
        List<Metadata> entries = listFolder.getEntries();
        for (Metadata metadata : entries) {
            System.out.println(metadata.getName());
        }
    }

}
