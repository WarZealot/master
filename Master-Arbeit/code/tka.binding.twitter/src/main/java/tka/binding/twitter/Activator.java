/**
 *
 */
package tka.binding.twitter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Konstantin
 *
 *         Currently tracks tweets on twitter.
 */
public class Activator implements BundleActivator {

    private BundleContext context;
    private EventPublisher eventPublisher;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        System.out.println("Twitter Activator running...");

        // event publisher
        ServiceReference<EventPublisher> reference3 = context.getServiceReference(EventPublisher.class);
        eventPublisher = context.getService(reference3);

        trackTwitter();
    }

    private void trackTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey("9F8YmBxVk6o7XZfGNLgQieREN")
                .setOAuthConsumerSecret("avJ03ScGJwMCR3GmZ1FkFtbDiGk7eJcts9R61Hy3BvXgVMPie7")
                .setOAuthAccessToken("3524637135-tnqDTgMAFNPcUd11JgacIxz5rqeQFUW8PobKNyi")
                .setOAuthAccessTokenSecret("TDpDdNX5cB83wilP213DGQ8UNsgMvdK9R8oJEFqfu6MRF");
        // cb.setHttpProxyHost("proxy.materna.de").setHttpProxyPort(8080);
        TwitterFactory tf = new TwitterFactory(cb.build());
        final Twitter twitter = tf.getInstance();

        Runnable statusTracker = new Runnable() {
            long previous = -1;

            @Override
            public void run() {
                try {
                    ResponseList<Status> timeline = twitter.getUserTimeline();
                    long time = timeline.get(0).getCreatedAt().getTime();
                    if (previous == -1) {
                        previous = time;
                    } else if (time != previous) {
                        previous = time;
                        Event event = new Event() {
                            @Override
                            public String getType() {
                                return "TwitterEventType";
                            }

                            @Override
                            public String getTopic() {
                                return "statusChanged";
                            }

                            @Override
                            public String getSource() {
                                return "web";
                            }

                            @Override
                            public String getPayload() {
                                return "no payload";
                            }
                        };
                        eventPublisher.post(event);
                    }
                } catch (TwitterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(statusTracker, 0, 10, TimeUnit.SECONDS);

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context = null;
    }

}
