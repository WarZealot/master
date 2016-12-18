package tka.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.automation.Action;
import org.eclipse.smarthome.automation.Condition;
import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleProvider;
import org.eclipse.smarthome.automation.RuleRegistry;
import org.eclipse.smarthome.automation.Trigger;
import org.eclipse.smarthome.automation.Visibility;
import org.eclipse.smarthome.automation.template.Template;
import org.eclipse.smarthome.automation.template.TemplateProvider;
import org.eclipse.smarthome.automation.type.ModuleType;
import org.eclipse.smarthome.automation.type.ModuleTypeProvider;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFactory;
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
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

    private BundleContext context;
    private ThingRegistry thingRegistry;
    private RuleRegistry ruleRegistry;
    private EventPublisher eventPublisher;
    private ItemRegistry itemRegistry;
    private ModuleTypeProvider typeProvider;
    private TemplateProvider templateProvider;
    private RuleProvider ruleProvider;
    private MyDemoEventFactory myEventFactory;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Delaying start...");
        // Thread.sleep(5000);
        context = bundleContext;
        System.out.println("Activator.start()");

        // thing registry
        ServiceReference<ThingRegistry> reference1 = context.getServiceReference(ThingRegistry.class);
        thingRegistry = context.getService(reference1);

        // rule registry
        ServiceReference<RuleRegistry> reference2 = context.getServiceReference(RuleRegistry.class);
        ruleRegistry = context.getService(reference2);

        // event publisher
        ServiceReference<EventPublisher> reference3 = context.getServiceReference(EventPublisher.class);
        eventPublisher = context.getService(reference3);

        // item registry
        ServiceReference<ItemRegistry> reference4 = context.getServiceReference(ItemRegistry.class);
        itemRegistry = context.getService(reference4);

        // module type provider
        ServiceReference<ModuleTypeProvider> reference5 = context.getServiceReference(ModuleTypeProvider.class);
        typeProvider = context.getService(reference5);

        ServiceReference<TemplateProvider> reference6 = context.getServiceReference(TemplateProvider.class);
        templateProvider = context.getService(reference6);

        ServiceReference<RuleProvider> reference7 = context.getServiceReference(RuleProvider.class);
        ruleProvider = context.getService(reference7);

        HashSet<String> set = new HashSet<String>();
        set.add(MyEvent.TYPE);
        myEventFactory = new MyDemoEventFactory(set);
        context.registerService(EventFactory.class.getName(), myEventFactory, null);

        doSomething();
    }

    private void printAllServices() throws InvalidSyntaxException {
        ServiceReference<?>[] references = context.getAllServiceReferences(null, null);
        for (ServiceReference<?> reference : references) {
            System.out.println("Reference: " + reference);
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
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
        // manipulateMyTwitter();
        // createRules();

        // useModuleTypeProvider();
        // doSomethingOther();
        // useFlashTemplates();
        // useRuleProvider();
        createDemoEvents();

    }

    private void createDemoEvents() {

        Runnable eventGenerator = new Runnable() {
            @Override
            public void run() {
                Event event;
                try {
                    event = myEventFactory.createEventByType(MyEvent.TYPE, "flash/web/mytopic", "mypayload",
                            "flash/web");
                    eventPublisher.post(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(eventGenerator, 0, 5, TimeUnit.MINUTES);
    }

    private void useRuleProvider() {
        Collection<Rule> all = ruleProvider.getAll();
        System.out.println("Rules:");
        for (Rule rule : all) {
            System.out.println(rule.getUID());
            System.out.println(rule.getName());
            System.out.println(rule.getTemplateUID());
        }
    }

    private void useFlashTemplates() {
        if (templateProvider == null) {
            System.err.println("templateProvider is NULL! This is BAD!");
            return;
        }
        System.out.println("templateProvider: " + templateProvider);
        Collection<Template> templates = templateProvider.getTemplates(null);
        System.out.println("Templates");
        for (Template template : templates) {
            System.out.println(template);
        }
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
        // if (exists == null) {
        // Thing thing = thingRegistry.createThingOfType(AtomclockBindingConstants.THING_TYPE_ATOMCLOCK,
        // new ThingUID("atomclock", "atomclock", "myclock"), null, "someLabel", configuration);
        // thingRegistry.add(thing);
        // }
        exists = thingRegistry.get(new ThingUID("twitter", "mycheaptwitter", "percode"));
        if (exists == null) {
            // Thing thing2 = thingRegistry.createThingOfType(TwitterBindingConstants.THING_TYPE_TWITTER,
            // new ThingUID("twitter", "mycheaptwitter", "percode"), null, "twitterLabel", configuration);
            // thingRegistry.add(thing2);
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

    /**
     * This method creates a rule from scratch by using trigger, condition, action, configDescriptions and
     * configuration, tags.
     *
     * @return the created rule
     */
    private Rule createTwitterRule() {
        // initialize the trigger
        String triggerId = "TkaTrigger";
        List<Trigger> triggers = new ArrayList<Trigger>();
        // triggers.add(new Trigger(triggerId, TkaTriggerType.UID, null));

        // initialize the condition - here the tricky part is the referring into the condition input - trigger output.
        // The syntax is a similar to the JUEL syntax.
        Map<String, Object> config = new HashMap<String, Object>();
        List<Condition> conditions = new ArrayList<Condition>();
        Map<String, String> inputs = new HashMap<String, String>();
        // conditions.add(new Condition("AlwaysTrueCondition", AlwaysTrueConditionType.UID, config, inputs));

        // initialize the action - here the tricky part is the referring into the action configuration parameter - the
        // template configuration parameter. The syntax is a similar to the JUEL syntax.
        config = new HashMap<String, Object>();
        List<Action> actions = new ArrayList<Action>();
        // actions.add(new Action("TwitterAction", TwitterActionType.UID, config, null));

        // create the rule
        Rule twitterRule = new Rule("TwitterRule");
        twitterRule.setTriggers(triggers);
        twitterRule.setConditions(conditions);
        twitterRule.setActions(actions);

        // initialize the tags
        Set<String> tags = new HashSet<String>();
        tags.add("twitter");

        // set the tags
        twitterRule.setTags(tags);

        return twitterRule;
    }
}
