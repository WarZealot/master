/**
 *
 */
package twitter;

import java.util.Date;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author ktkachuk
 *
 */
public class MainTwitter4J {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("http.proxyHost", "proxy.materna.de");
        System.setProperty("http.proxyPort", "8080");

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey("9F8YmBxVk6o7XZfGNLgQieREN")
                .setOAuthConsumerSecret("avJ03ScGJwMCR3GmZ1FkFtbDiGk7eJcts9R61Hy3BvXgVMPie7")
                .setOAuthAccessToken("3524637135-tnqDTgMAFNPcUd11JgacIxz5rqeQFUW8PobKNyi")
                .setOAuthAccessTokenSecret("TDpDdNX5cB83wilP213DGQ8UNsgMvdK9R8oJEFqfu6MRF");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        String latestStatus = "Currently writing a test program using Twitter API: " + new Date();
        Status status;
        try {
            status = twitter.updateStatus(latestStatus);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
