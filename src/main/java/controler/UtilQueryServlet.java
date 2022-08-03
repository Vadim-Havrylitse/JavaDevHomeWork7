package controler;

import database.hibernate.HibernateAbstractClass;
import model.Developers;
import model.Projects;
import model.utilquerymodel.SpecialFormatProjects;
import model.utilquerymodel.SumOfProjectsSalary;
import org.thymeleaf.context.Context;
import view.UserViewBrowser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@WebServlet("/util_query")
public class UtilQueryServlet extends HttpServlet {

    private static final UserViewBrowser BROWSER_VIEW;
    private static final String NAMED_QUERY_FOR_SUM_SALARY_IN_SOME_PROJECT;
    private static final String NAMED_QUERY_FOR_LIST_ALL_DEV_IN_SOME_PROJECT;
    private static final String NAMED_QUERY_FOR_LIST_ALL_JAVA_DEV;
    private static final String NAMED_QUERY_FOR_LIST_ALL_MIDDLE_DEV;
    private static final String NAMED_QUERY_FOR_LIST_ALL_PROJECT_WITH_SPEC_FORMAT;

    static {
        BROWSER_VIEW = UserViewBrowser.of();
        NAMED_QUERY_FOR_LIST_ALL_PROJECT_WITH_SPEC_FORMAT = "spec_format_projects";
        NAMED_QUERY_FOR_LIST_ALL_MIDDLE_DEV = "all_middle_dev";
        NAMED_QUERY_FOR_LIST_ALL_JAVA_DEV = "all_java_dev";
        NAMED_QUERY_FOR_LIST_ALL_DEV_IN_SOME_PROJECT = "all_dev_in_some_projects";
        NAMED_QUERY_FOR_SUM_SALARY_IN_SOME_PROJECT = "sum_salary";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Context context = new Context();
        if(req.getParameter("sumDev") != null){
            Map<String, String> parameterMap = Map.of("projectId", req.getParameter("sumDev"));
            BROWSER_VIEW.sendRedirectOnPage(req, resp,"util_query_print",
                    printTableResult(NAMED_QUERY_FOR_SUM_SALARY_IN_SOME_PROJECT, parameterMap, Projects.class));
        }
        if (req.getParameter("projectDev") != null){
            Map<String, String> parameterMap = Map.of("projectId", req.getParameter("projectDev"));
            BROWSER_VIEW.sendRedirectOnPage(req, resp,"util_query_print",
                    printTableResult(NAMED_QUERY_FOR_LIST_ALL_DEV_IN_SOME_PROJECT, parameterMap, Developers.class));
        }
        if(req.getParameter("action") != null){
            switch (req.getParameter("action")){
                case "javaDev":
                    context = printTableResult(NAMED_QUERY_FOR_LIST_ALL_JAVA_DEV, Developers.class);
                    break;
                case "middleDev":
                    context = printTableResult(NAMED_QUERY_FOR_LIST_ALL_MIDDLE_DEV, Developers.class);
                    break;
                case "projectFormat":
                    context = printTableResult(NAMED_QUERY_FOR_LIST_ALL_PROJECT_WITH_SPEC_FORMAT, Projects.class);
                    break;
            }
            BROWSER_VIEW.sendRedirectOnPage(req, resp, "util_query_print", context);
            return;
        }
        BROWSER_VIEW.sendRedirect(resp, "сrud");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BROWSER_VIEW.sendRedirectOnPage(req, resp, "util_query", new Context());
    }

    private Context printTableResult(String namedSqlQuery, Class<?> classResult){
        List<?> objects = HibernateAbstractClass.executeSqlNamedQuery(namedSqlQuery, classResult);
        return printTableResult(objects,classResult);
    }

    private Context printTableResult(String namedSqlQuery, Map<String, String> parameterMap, Class<?> classResult){
        List<?> objects = HibernateAbstractClass.executeSqlNamedQuery(namedSqlQuery, parameterMap, classResult);
        return printTableResult(objects, classResult);
    }

    private Context printTableResult(List<?> objects, Class<?> classResult){
        List<String> labelList = getLabels(objects.get(0), classResult);
        List<List<String>> valuesList = getValues(objects, classResult);
        Context context = new Context();
        context.setVariable("labelList", labelList);
        context.setVariable("valuesList", valuesList);
        return context;
    }

    private List<List<String>> getValues(List<?> objects, Class<?> classResult) {
        List<List<String>> result = new ArrayList<>();
        Field[] fields = classResult.getDeclaredFields();
        Iterator<?> iterator = objects.iterator();
        while (iterator.hasNext()){
            List<String> resultInnerList = new ArrayList<>();
            Object next = iterator.next();
            for (Field field : fields){
                try {
                    field.setAccessible(true);
                    if(field.get(next) != null){
                        resultInnerList.add(field.get(next).toString());
                    }
                    result.add(resultInnerList);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
            }
        }
        return result;
    }

    public List<String> getLabels(Object objectResult, Class<?> classResult) {
        List<String> result = new ArrayList<>();
        for (Field field:classResult.getDeclaredFields()){
            field.setAccessible(true);
            try{
                if (field.get(objectResult) != null){
                    result.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(false);
            }
        }
        return result;
    }
}