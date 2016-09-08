/**
 *
 */
package tka.demo;

import java.util.List;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;

/**
 * @author ktkachuk
 *
 */
public class Utils {

    public static void toString(Thing thing) {
        System.out.println("Thing: " + thing);
        if (thing == null) {
            return;
        }
        System.out.println("ThingUID: " + thing.getUID());
        System.out.println("ThingTypeUID: " + thing.getThingTypeUID());
        System.out.println("Label: " + thing.getLabel());
        System.out.println("Channels: " + thing.getChannels());
        System.out.println("Configuration: " + thing.getConfiguration());
        System.out.println("Properties: " + thing.getProperties());
        System.out.println("StatusInfo: " + thing.getStatusInfo());
        System.out.println("--------------------------------------------");

        toString(thing.getHandler());
        toString(thing.getChannels());
    }

    private static void toString(List<Channel> list) {
        for (Channel channel : list) {
            toString(channel);
        }
    }

    public static void toString(Channel channel) {
        System.out.println("Channel: " + channel);
        System.out.println("UID: " + channel.getUID());
        System.out.println("Description: " + channel.getDescription());
        System.out.println("AcceptedItemType: " + channel.getAcceptedItemType());
        System.out.println("Properties: " + channel.getProperties());
        System.out.println("---------");
    }

    public static void toString(ThingHandler handler) {
        System.out.println("Handler: " + handler);
        if (handler == null) {
            return;
        }
        System.out.println("HandlerClass: " + handler.getClass().getSimpleName());
        System.out.println("--------------------------------------------");
    }

    public static void toString(Item item) {
        System.out.println("Item: " + item);
        if (item == null) {
            return;
        }
        System.out.println("Name: " + item.getName());
        System.out.println("Category: " + item.getCategory());
        System.out.println("Type: " + item.getType());
        System.out.println("AcceptedCommandTypes: " + item.getAcceptedCommandTypes());
        System.out.println("--------------------------------------------");
    }

}
