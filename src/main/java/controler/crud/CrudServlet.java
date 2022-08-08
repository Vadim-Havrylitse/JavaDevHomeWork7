package controler.crud;

import view.UserViewBrowser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/crud", name = "MainServlet")
public class CrudServlet extends HttpServlet {
    private static final UserViewBrowser BROWSER_VIEW;

    static {
        BROWSER_VIEW = UserViewBrowser.of();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BROWSER_VIEW.sendRedirectOnMainPage(req, resp);
    }
}
