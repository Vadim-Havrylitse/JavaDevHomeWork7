package controler.crud;

import org.thymeleaf.context.Context;
import util.ApiEntity;
import view.UserViewBrowser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet(urlPatterns = "/crud", name = "MainServlet")
public class CrudServlet extends HttpServlet {
    private static final UserViewBrowser BROWSER_VIEW;
    private static final List<String> TABLES_NAME;

    static {
        TABLES_NAME = new ArrayList<>();
        for (ApiEntity entity: ApiEntity.values()){
            TABLES_NAME.add(entity.name());
        }
        BROWSER_VIEW = UserViewBrowser.of();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("table");
        if (!TABLES_NAME.contains(tableName)) {
            Context context = new Context();
            context.setVariable("tablesList", TABLES_NAME);
            BROWSER_VIEW.sendRedirectOnPage(req, resp, "MAIN_PAGE", context);
        } else {
            String url = "http://localhost:8081" +
                    req.getServletPath() +
                    "/" +
                    tableName.toLowerCase(Locale.ROOT);
            System.out.println(url);
            resp.sendRedirect(url);
        }
    }
}
