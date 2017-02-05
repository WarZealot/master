package tka.flashui.bindings;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Konstantin
 *
 */
public class WeatherServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 275417185547581863L;
    private ThingRegistry thingRegistry;

    private final Logger logger = LoggerFactory.getLogger(WeatherServlet.class);

    public WeatherServlet(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WeatherServlet.doGet()");
        resp.setContentType("text/plain");

        String location = req.getParameter("location");
        int refresh = 0;
        try {
            refresh = Integer.parseInt(req.getParameter("refresh"));
        } catch (Exception e) {
            logger.debug("Could not read refresh period from request. Leaving default refresh=0");
        }

        createThingForLocation(location, refresh);
        resp.getWriter().print("SUCCESS");
    }

    public static final String CONFIG_LOCATION = "location";
    public static final String CONFIG_REFRESH = "refresh";
    public final static ThingTypeUID THING_TYPE_WEATHER = new ThingTypeUID("weather", "weather");

    private void createThingForLocation(String location, int refreshInSeconds) {
        Configuration configuration = new Configuration();
        configuration.put(CONFIG_LOCATION, location);
        configuration.put(CONFIG_REFRESH, refreshInSeconds);

        ThingUID thingUID = new ThingUID("weather", "weatherThingTypeId", location);

        Thing thing = thingRegistry.createThingOfType(THING_TYPE_WEATHER, thingUID, null, location.toUpperCase(),
                configuration);
        thingRegistry.add(thing);
    }

}
