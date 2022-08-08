package controler.crud;

import database.hibernate.HibernateService;
import model.Companies;
import model.Customers;
import model.Projects;
import org.thymeleaf.context.Context;
import service.EntityParsingService;
import util.ApiEntity;
import util.ApiResponse;
import util.FormType;
import view.UserViewBrowser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns =  {"/crud/create/projects",
        "/crud/read/projects",
        "/crud/delete/projects",
        "/crud/update/projects"})
@SuppressWarnings({"unchecked", "rowtype", "ConstantConditions"})
public class ProjectsServlet extends HttpServlet {
    private static final UserViewBrowser BROWSER_VIEW = UserViewBrowser.of();
    private static final HibernateService<Projects> SERVICE = HibernateService.getInstance(ApiEntity.PROJECTS);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        if (url.contains("create")) {
            sendInputForm(req, resp, FormType.CREATE);
        }
        if (url.contains("read")) {
            printProjects(req, resp);
        }
        if (url.contains("delete")) {
            if (req.getParameter("id") != null) {
                ApiResponse apiResponse = SERVICE.delete(Long.valueOf(req.getParameter("id")));
                Context context = new Context();
                context.setVariable("apiResponse", apiResponse);
                BROWSER_VIEW.sendRedirectOnPage(req, resp, "api_response", context);
                return;
            }
            sendInputForm(req,resp, FormType.DELETE);
        }
        if (url.contains("update")) {
            sendInputForm(req,resp, FormType.UPDATE);
        }
    }

    private void printProjects(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Context context = new Context();
        context.setVariable("projectsList", SERVICE.readAll());
        try {
            BROWSER_VIEW.sendRedirectOnPage(req, resp, "project_print_data", context);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());
        }
    }

    private void sendInputForm(HttpServletRequest req, HttpServletResponse resp, FormType type) throws IOException {
        try {
            if (type.equals(FormType.DELETE)) {
                BROWSER_VIEW.sendRedirectOnPage(req, resp, "delete_id_input", new Context());
                return;
            }
            List<Companies> companiesList = HibernateService.getInstance(ApiEntity.COMPANIES).readAll();
            List<Customers> customersList = HibernateService.getInstance(ApiEntity.CUSTOMERS).readAll();
            Context context = new Context();
            context.setVariable("companiesList", companiesList);
            context.setVariable("customersList", customersList);
            if (type.equals(FormType.CREATE)){
                BROWSER_VIEW.sendRedirectOnPage(req, resp, "project_create_form", context);
            }
            if (type.equals(FormType.UPDATE)){
                BROWSER_VIEW.sendRedirectOnPage(req, resp, "project_update_form", context);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Context context = new Context();
        ApiResponse apiResponse = null;
        String url = req.getRequestURI();
        if (url.contains("create")) {
            apiResponse = SERVICE.save(EntityParsingService.parseRequestToEntity(req, Projects.class));
        }
        if (url.contains("delete")) {
            apiResponse = SERVICE.delete(Long.valueOf(req.getParameter("id")));
        }
        if (url.contains("update")) {
            apiResponse = SERVICE.update(EntityParsingService.parseRequestToEntity(req, Projects.class));
        }
        context.setVariable("apiResponse", apiResponse);
        BROWSER_VIEW.sendRedirectOnPage(req,resp,"api_response", context);
    }
}