package tka.flashui.thing;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingUID;

import com.google.gson.Gson;

/**
 * @author Konstantin
 *
 */
public class ThingsDeleterServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 957446669297705448L;
    private Gson gson;
    private ThingRegistry thingRegistry;

    public ThingsDeleterServlet(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ThingsDeleterServlet.doGet()");
        resp.setContentType("application/json");

        boolean deleted = handleDelete(req);
        String result = deleted ? "SUCCESS" : "ERROR";

        resp.getWriter().print(gson.toJson(result));
    }

    /**
     * @param req
     * @return <code>true</code>, if a rule was removed
     */
    private boolean handleDelete(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();
        String[] deletes = parameters.get("delete");
        if (deletes == null || deletes.length == 0) {
            return false;
        }
        Thing removed = thingRegistry.remove(new ThingUID(deletes[0]));
        return removed != null;
    }
}
