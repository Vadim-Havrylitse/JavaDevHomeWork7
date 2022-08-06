package controler;

import database.hibernate.HibernateAbstractClass;
import database.hibernate.HibernateService;
import model.Developers;
import model.Projects;
import model.Skills;
import org.thymeleaf.context.Context;
import util.ApiEntity;
import view.UserViewBrowser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/util_query")
public class UtilQueryServlet extends HttpServlet {

    private static final UserViewBrowser BROWSER_VIEW;
    private static final HibernateService<Developers> developersService;
    private static final HibernateService<Projects> projectsService;
    private static final HibernateService<Skills> skillsService;

    static {
        BROWSER_VIEW = UserViewBrowser.of();
        //noinspection unchecked
        skillsService = HibernateService.getInstance(ApiEntity.SKILLS);
        //noinspection unchecked
        developersService = HibernateService.getInstance(ApiEntity.DEVELOPERS);
        //noinspection unchecked
        projectsService = HibernateService.getInstance(ApiEntity.PROJECTS);
//        NAMED_QUERY_FOR_LIST_ALL_MIDDLE_DEV = "SELECT dv.* " +
//                "FROM Developers dv " +
//                "LEFT JOIN developers_skills dvsk " +
//                "ON dvsk.developers_id = dv.id " +
//                "LEFT JOIN skills sk " +
//                "ON dvsk.skills_id = sk.id " +
//                "WHERE sk.degree = 'Middle';";
//        NAMED_QUERY_FOR_LIST_ALL_JAVA_DEV = "SELECT dev.* " +
//                "FROM Developers dev " +
//                "LEFT JOIN developers_skills devsk " +
//                "ON devsk.developers_id = dev.id " +
//                "LEFT JOIN skills sk " +
//                "ON devsk.skills_id = sk.id " +
//                "WHERE sk.industry = 'Java'; ";
//        NAMED_QUERY_FOR_LIST_ALL_DEV_IN_SOME_PROJECT = "SELECT dev.* " +
//                "FROM developers_projects devpr " +
//                "LEFT JOIN developers dev " +
//                "ON devpr.developers_id = dev.id " +
//                "WHERE devpr.projects_id = %s ";
//        NAMED_QUERY_FOR_SUM_SALARY_IN_SOME_PROJECT = "SELECT projects.name AS name, SUM(salary) AS sum" +
//                "FROM projects pr" +
//                "LEFT JOIN developers_projects devpr" +
//                "ON pr.id = devpr.projects_id " +
//                "LEFT JOIN developers dev" +
//                "ON devpr.developers_id = dev.id " +
//                "WHERE pr.id = %s "+
//                "GROUP BY pr.name";
//        NAMED_QUERY_FOR_LIST_ALL_PROJECT_WITH_SPEC_FORMAT = "SELECT projects.release_date AS release, projects.name AS name, COUNT(developers_id) AS count " +
//                "FROM projects pr " +
//                "LEFT JOIN developers_projects devpr " +
//                "ON devpr.developers_id = pr.id " +
//                "GROUP BY pr.id";
    }

    @SuppressWarnings({"ConstantConditions"})
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Context context = new Context();
        if(req.getParameter("sumDev") != null){
            long idProject = Long.parseLong(req.getParameter("sumDev"));
            Projects actualProject = projectsService.read(idProject);
            List<Developers> developers = developersService.readAll();
            Integer sumDevSalary = developers.stream()
                    .filter(developer -> developer.getProjects().contains(actualProject))
                    .map(Developers::getSalary)
                    .mapToInt(Integer::intValue)
                    .sum();
            List<String> labelList = List.of("PROJECT", "SUM DEV'S SALARY");
            List<List<String>> valuesList = new ArrayList<>();
            valuesList.add(List.of(actualProject.getName(), String.valueOf(sumDevSalary)));
            context.setVariable("labelList", labelList);
            context.setVariable("valuesList", valuesList);
            BROWSER_VIEW.sendRedirectOnPage(req, resp, "util_query_print", context);
            return;
        }
        if (req.getParameter("projectDev") != null){
            long idProject = Long.parseLong(req.getParameter("projectDev"));
            Projects actualProject = projectsService.read(idProject);
            List<Developers> resultDevelopers = developersService.readAll().stream()
                    .filter(developer -> developer.getProjects().contains(actualProject))
                    .collect(Collectors.toList());
            context.setVariable("label", "Список разработчиков в проекте " + actualProject.getName() + ":");
            context.setVariable("developers", resultDevelopers);
            BROWSER_VIEW.sendRedirectOnPage(req, resp, "developer_print_data", context);
            return;
        }
        if(req.getParameter("action") != null){
            switch (req.getParameter("action")){
                case "javaDev":
                    List<Developers> javaDevsResult = developersService.readAll().stream().filter(dev -> {
                        List<String> collect = dev.getSkills().stream()
                                .map(Skills::getIndustry)
                                .collect(Collectors.toList());
                        return collect.contains("Java");
                    }).collect(Collectors.toList());
                    context.setVariable("label", "Список всех JAVA разработчиков:");
                    context.setVariable("developers", javaDevsResult);
                    BROWSER_VIEW.sendRedirectOnPage(req, resp, "developer_print_data", context);
                    return;
                case "middleDev":
                    List<Developers> middleDevsResult = developersService.readAll().stream()
                            .filter(dev -> dev.getSkills().stream()
                                    .map(Skills::getDegree)
                                    .collect(Collectors.toList())
                                    .contains("Middle")
                    ).collect(Collectors.toList());
                    context.setVariable("label", "Список всех Middle разработчиков:");
                    context.setVariable("developers", middleDevsResult);
                    BROWSER_VIEW.sendRedirectOnPage(req, resp, "developer_print_data", context);
                    return;
                case "projectFormat":
                    Iterator<Projects> iterator = projectsService.readAll().iterator();
                    List<Developers> developers = developersService.readAll();
                    List<String> labelList = List.of("RELEASE DATA", "PROJECT", "COUNT OF DEV");
                    List<List<String>> valuesList = new ArrayList<>();
                    while (iterator.hasNext()){
                        Projects next = iterator.next();
                        long count = developers.stream()
                                .filter(dev -> dev.getProjects().contains(next))
                                .count();
                        System.out.println("next = " + next);
                        valuesList.add(List.of(
                                next.getReleaseDate() == null ? "":next.getReleaseDate(),
                                next.getName(),
                                String.valueOf(count)));
                    }
                    context.setVariable("labelList", labelList);
                    context.setVariable("valuesList", valuesList);
                    BROWSER_VIEW.sendRedirectOnPage(req, resp, "util_query_print", context);
                    return;
            }
        }
        BROWSER_VIEW.sendRedirect(resp, "сrud");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BROWSER_VIEW.sendRedirect(resp, "сrud");
    }
}