package tka.flashui.rule;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleRegistry;

import com.google.gson.Gson;

/**
 * @author Konstantin
 *
 */
public class RulesProviderServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 957446669297705448L;
    private RuleRegistry ruleRegistry;
    private Gson gson;

    public RulesProviderServlet(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("RulesProviderServlet called!");
        resp.setContentType("application/json");

        String uid = req.getParameter("uid");
        if (uid != null) {
            Rule rule = ruleRegistry.get(uid);
            resp.getWriter().print(gson.toJson(Arrays.asList(rule)));
            return;
        }

        Collection<Rule> flashRules = ruleRegistry.getByTag("flash");
        String result = gson.toJson(flashRules);
        resp.getWriter().print(result);
    }
}
