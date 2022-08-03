package database.hibernate;

import model.*;
import model.utilquerymodel.SpecialFormatProjects;
import model.utilquerymodel.SumOfProjectsSalary;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import util.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public abstract class HibernateAbstractClass<P extends Model> implements HibernateService<P>{
    protected static final ApiResponse FAIL_API_RESPONSE = new ApiResponse(HttpServletResponse.SC_BAD_REQUEST, "Что-то пошло не так. Проверьте введенные данные.");
    protected static final ApiResponse OK_API_RESPONSE = new ApiResponse(200, "Все прошло успешно.");
    protected static final SessionFactory sessionFactory;

    static {
        Flyway flyway = Flyway.configure().dataSource("jdbc:mysql://localhost:3306/testgoitdb", "root", "fj159357").load();
        flyway.migrate();

        sessionFactory = new Configuration()
                .addAnnotatedClass(SumOfProjectsSalary.class)
                .addAnnotatedClass(SpecialFormatProjects.class)
                .addAnnotatedClass(Skills.class)
                .addAnnotatedClass(Projects.class)
                .addAnnotatedClass(Companies.class)
                .addAnnotatedClass(Customers.class)
                .addAnnotatedClass(Developers.class)
                .buildSessionFactory();
    }

    protected Session openSession(){
        return sessionFactory.openSession();
    }

    public static<T> List<?> executeSqlNamedQuery(String namedQuery, Class<T> className){
        try(Session session = sessionFactory.openSession()){
            return session.createNamedQuery(namedQuery, className).list();
        }
    }

    public static<T> List<?> executeSqlNamedQuery(String namedQuery, Map<String, String> parameterMap, Class<T> className){
        if (parameterMap.isEmpty()){
            return executeSqlNamedQuery(namedQuery, className);
        }
        try(Session session = sessionFactory.openSession()){
            Query<?> query = session.createNamedQuery(namedQuery, className);
            parameterMap.forEach(query::setParameter);
            return query.list();
        }
    }

    protected P doReed(Class<P> className, Long id){
        try (Session session = openSession()){
            return (session.get(className, id));
        }
    }

    protected List<P> doAllRead(Class<P> className){
        try (Session session = openSession()){
            return session.createSelectionQuery("from "
                    + className.getName(), className).list();
        }
    }

    protected ApiResponse doSave(P object){
        try (Session session = openSession()){
            Transaction transaction = session.beginTransaction();
                session.persist(object);
            transaction.commit();
            return OK_API_RESPONSE;
        }
    }

    protected ApiResponse doDelete(Class<P> className, Long id){
        try(Session session = openSession()){
            session.beginTransaction();
                session.remove(doReed(className,id));
            session.getTransaction().commit();
            return OK_API_RESPONSE;
        }
    }

    protected ApiResponse doUpdate(Class<P> className, P objectForUpdate, BinaryOperator<P> function){
        try (Session session = openSession()){
            session.beginTransaction();
                P mainObject = session.get(className, objectForUpdate.getId());
                session.evict(mainObject);
                session.merge(function.apply(mainObject, objectForUpdate));
            session.getTransaction().commit();
            return OK_API_RESPONSE;
        }
    }
}