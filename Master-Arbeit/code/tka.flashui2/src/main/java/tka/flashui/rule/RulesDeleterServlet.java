package tka.flashui.rule;

import java.io.IOException;
import java.util.Map;

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
public class RulesDeleterServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 6401293401641761963L;
    private RuleRegistry ruleRegistry;

    public RulesDeleterServlet(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("RulesDeleterServlet called!");
        resp.setContentType("application/json");

        if (req.getParameter("toggle") != null) {
            String uid = req.getParameter("toggle");
            Boolean enabled = ruleRegistry.isEnabled(uid);
            ruleRegistry.setEnabled(uid, !enabled);
            resp.getWriter().print(new Gson().toJson("SUCCESS"));
            return;
        }

        boolean deleted = handleDelete(req);
        String result = deleted ? "SUCCESS" : "ERROR";

        resp.getWriter().print(new Gson().toJson(result));
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
        Rule removed = ruleRegistry.remove(deletes[0]);
        return removed != null;
    }
}
