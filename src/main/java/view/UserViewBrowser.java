package view;

import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserViewBrowser {
    void sendRedirectOnMainPage(HttpServletRequest req, HttpServletResponse resp) throws IOException;
    static UserViewBrowser of(){
        return new UserViewBrowserImpl();
    }
    void sendRedirectOnPage(HttpServletRequest req, HttpServletResponse resp, String nameHtmlFile, Context context) throws IOException;
}