package controler.crud;

import database.hibernate.HibernateService;
import model.Companies;
import model.Developers;
import model.Projects;
import model.Skills;
import org.thymeleaf.context.Context;
import service.DtoParsingService;
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

@SuppressWarnings({"unchecked", "ConstantConditions"})
@WebServlet(urlPatterns = {"/crud/create/developers",
        "/crud/read/developers",
        "/crud/delete/developers",
        "/crud/update/developers"})
public class DevelopersServlet extends HttpServlet {
    private static final UserViewBrowser BROWSER_VIEW = UserViewBrowser.of();
    private static final HibernateService<Developers> SERVICE = HibernateService.getInstance(ApiEntity.DEVELOPERS);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        if (url.contains("create")) {
            sendInputForm(req, resp, FormType.CREATE);
        }
        if (url.contains("read")) {
            printDevelopers(req, resp);
        }
        if (url.contains("delete")) {
            if (req.getParameter("id") != null){
                ApiResponse apiResponse = SERVICE.delete(Long.valueOf(req.getParameter("id")));
                Context context = new Context();
                context.setVariable("apiResponse", apiResponse);
                BROWSER_VIEW.sendRedirectOnPage(req,resp,"api_response", context);
                return;
            }
            sendInputForm(req, resp, FormType.DELETE);
        }
        if (url.contains("update")) {
            sendInputForm(req,resp, FormType.UPDATE);
        }
    }

    private void printDevelopers(HttpServletRequest req, HttpServletResponse resp) {
        Context context = new Context();
        context.setVariable("developers", SERVICE.readAll());
        try {
            BROWSER_VIEW.sendRedirectOnPage(req, resp, "developer_print_data", context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendInputForm(HttpServletRequest req, HttpServletResponse resp, FormType type) {
        try {
            if(type.equals(FormType.DELETE)) {
                BROWSER_VIEW.sendRedirectOnPage(req, resp, "delete_id_input", new Context());
                return;
            }
            List<Projects> projects = HibernateService.getInstance(ApiEntity.PROJECTS).readAll();
            List<Companies> companies = HibernateService.getInstance(ApiEntity.COMPANIES).readAll();
            List<Skills> skills = HibernateService.getInstance(ApiEntity.SKILLS).readAll();
            Context context = new Context();
            context.setVariable("companies", companies);
            context.setVariable("projects", projects);
            context.setVariable("skills", skills);
            if (type.equals(FormType.CREATE)){
                BROWSER_VIEW.sendRedirectOnPage(req, resp, "developer_create_form", context);
            }
            if (type.equals(FormType.UPDATE)){
                BROWSER_VIEW.sendRedirectOnPage(req, resp, "developer_update_form", context);
            }
         } catch (IOException e){
            e.printStackTrace();
         }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Context context = new Context();
        ApiResponse apiResponse = null;
        String url = req.getRequestURI();
        if (url.contains("create")) {
            apiResponse = SERVICE.save(DtoParsingService.parseRequestToDto(req, Developers.class));
        }
        if (url.contains("delete")) {
            apiResponse = SERVICE.delete(Long.valueOf(req.getParameter("id")));
        }
        if (url.contains("update")) {
            apiResponse = SERVICE.update(DtoParsingService.parseRequestToDto(req, Developers.class));
        }
        context.setVariable("apiResponse", apiResponse);
        BROWSER_VIEW.sendRedirectOnPage(req,resp,"api_response", context);
    }
}