package tka.flashui.thing;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;

import com.google.gson.Gson;

/**
 * @author Konstantin
 *
 */
public class ThingsProviderServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 957446669297705448L;
    private Gson gson;
    private ThingRegistry thingRegistry;

    public ThingsProviderServlet(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ThingsProviderServlet.doGet()");
        resp.setContentType("application/json");

        Collection<Thing> things = thingRegistry.getAll();
        String result = gson.toJson(things);
        System.out.println(result);
        resp.getWriter().print(result);
    }
}
