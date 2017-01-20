package tka.flashui.bindings;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tka.binding.twitter.TwitterConnectionService;

/**
 * @author Konstantin
 *
 */
public class TwitterServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 957446669297705448L;
    private TwitterConnectionService connectionService;

    public TwitterServlet(TwitterConnectionService service) {
        this.connectionService = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("TwitterServlet.doGet()");
        resp.setContentType("text/plain");

        String status = req.getParameter("status");
        if (status != null) {
            resp.getWriter().print("" + connectionService.isAuthorized());
            return;
        }

        String pin = req.getParameter("pin");
        if (pin != null) {
            boolean success = connectionService.authorizationGrantedCallback(pin);
            resp.getWriter().print("" + success);
            return;
        }

        Object url = connectionService.requestAuthorization();
        resp.getWriter().print(url.toString());
    }
}
