package view;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import util.ApiEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserViewBrowserImpl implements UserViewBrowser {

    @Override
    public void sendRedirectOnMainPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Context context = new Context();
        List<String> tablesNames = Arrays.stream(ApiEntity.values())
                .map(ApiEntity::name)
                .collect(Collectors.toList());
        context.setVariable("tablesList", tablesNames);
        sendRedirectOnPage(req, resp, "MAIN_PAGE", context);
    }

    @Override
    public void sendRedirectOnPage(HttpServletRequest req, HttpServletResponse resp, String nameHtmlFile, Context context) throws IOException {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(createTemplateResolver(req));
        resp.setContentType("text/html;charset=UTF-8");
        templateEngine.process(nameHtmlFile, context, resp.getWriter());
    }

    private ServletContextTemplateResolver createTemplateResolver(HttpServletRequest req){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(req.getServletContext());
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setPrefix("/WEB-INF/thymeleaf/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(true);
        return templateResolver;
    }
}