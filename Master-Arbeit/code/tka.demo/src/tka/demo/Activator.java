package tka.demo;

import java.util.Collection;
import java.util.Date;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleRegistry;
import org.eclipse.smarthome.automation.Visibility;
import org.eclipse.smarthome.automation.type.ModuleType;
import org.eclipse.smarthome.automation.type.ModuleTypeProvider;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.events.ItemCommandEvent;
import org.eclipse.smarthome.core.items.events.ItemEventFactory;
import org.eclipse.smarthome.core.library.items.StringItem;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.types.Command;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import tka.binding.atomclock.AtomclockBindingConstants;
import tka.binding.twitter.TwitterBindingConstants;

public class Activator implements BundleActivator {

    private static BundleContext context;
    private ThingRegistry thingRegistry;
    private RuleRegistry ruleRegistry;
    private EventPublisher eventPublisher;
    private ItemRegistry itemRegistry;
    private ModuleTypeProvider typeProvider;

    static BundleContext getContext() {
        return context;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Thread.sleep(5000);
        Activator.context = bundleContext;
        System.out.println("Activator.start()");

        // thing registry
        ServiceReference<ThingRegistry> reference1 = context.getServiceReference(ThingRegistry.class);
        thingRegistry = context.getService(reference1);

        // rule registry
        // ServiceReference<RuleRegistry> reference2 = context.getServiceReference(RuleRegistry.class);
        // ruleRegistry = context.getService(reference2);

        // event publisher
        ServiceReference<EventPublisher> reference3 = context.getServiceReference(EventPublisher.class);
        eventPublisher = context.getService(reference3);

        // item registry
        ServiceReference<ItemRegistry> reference4 = context.getServiceReference(ItemRegistry.class);
        itemRegistry = context.getService(reference4);

        // module type provider
        // ServiceReference<ModuleTypeProvider> reference5 = context
        // .getServiceReference(WelcomeHomeModuleTypeProvider.class);
        // typeProvider = context.getService(reference5);

        doSomething();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

    /**
     * Does something useful.
     */
    private void doSomething() {
        // createThings();
        // listThings();
        // createItems();
        // listItems();
        //
        manipulateMyTwitter();
        // createRules();

        useModuleTypeProvider();
        doSomethingOther();
    }

    private void doSomethingOther() {

    }

    private void useModuleTypeProvider() {
        if (typeProvider == null) {
            return;
        }
        Collection<ModuleType> types = typeProvider.getModuleTypes(null);
        for (ModuleType type : types) {
            System.out.println(type);
            System.out.println("UID: " + type.getUID());
            System.out.println("Label: " + type.getLabel());
            System.out.println("-------------------");
        }
    }

    private void createItems() {
        Item item2 = itemRegistry.get("MyCoolStringItem");
        if (item2 != null) {
            return;
        }
        Item item = new StringItem("MyCoolStringItem");
        itemRegistry.add(item);
    }

    private void manipulateMyTwitter() {
        // Thing twitter = thingRegistry.get(new ThingUID("twitter:mycheaptwitter:coolname"));
        postEvents();
    }

    private void listThings() {
        Collection<Thing> all = thingRegistry.getAll();
        System.out.println("Totoal things: " + all.size());
        for (Thing thing : all) {
            Utils.toString(thing);
        }
    }

    private void listItems() {
        Collection<Item> all = itemRegistry.getAll();
        System.out.println("Totoal items: " + all.size());
        for (Item item : all) {
            Utils.toString(item);
        }

    }

    /**
     * Create some things.
     */
    private void createThings() {
        Configuration configuration = new Configuration();
        Thing exists = thingRegistry.get(new ThingUID("atomclock", "atomclock", "myclock"));
        if (exists == null) {
            Thing thing = thingRegistry.createThingOfType(AtomclockBindingConstants.THING_TYPE_ATOMCLOCK,
                    new ThingUID("atomclock", "atomclock", "myclock"), null, "someLabel", configuration);
            thingRegistry.add(thing);
        }
        exists = thingRegistry.get(new ThingUID("twitter", "mycheaptwitter", "percode"));
        if (exists == null) {
            Thing thing2 = thingRegistry.createThingOfType(TwitterBindingConstants.THING_TYPE_TWITTER,
                    new ThingUID("twitter", "mycheaptwitter", "percode"), null, "twitterLabel", configuration);
            thingRegistry.add(thing2);
        }
    }

    private void postEvents() {
        // ThingAddedEvent addedEvent = ThingEventFactory.createAddedEvent(thing);
        // eventPublisher.post(addedEvent);

        Command command = new StringType("MyStringTypeCodeCommand: " + new Date());
        ItemCommandEvent event = ItemEventFactory.createCommandEvent("twitter_mycheaptwitter_percode_status", command);
        eventPublisher.post(event);
    }

    /**
     * Create rules.
     */
    private void createRules() {
        Rule rule = new Rule("myCustomSuperRule");
        rule.setName("SuperRuleName");

        rule.setTriggers(null);
        rule.setActions(null);
        rule.setConditions(null);
        rule.setVisibility(Visibility.VISIBLE);
        ruleRegistry.add(rule);
    }
}
