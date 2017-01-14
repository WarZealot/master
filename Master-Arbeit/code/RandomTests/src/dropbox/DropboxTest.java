/**
 *
 */
package dropbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;

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

        // try {
        // authDropbox(OAUTH_CONSUMER_KEY, OAUTH_CONSUMER_SECRET);
        // } catch (IOException | DbxException e) {
        // e.printStackTrace();
        // }
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

}
