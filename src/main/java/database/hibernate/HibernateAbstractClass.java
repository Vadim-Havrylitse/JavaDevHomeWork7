package database.hibernate;

import jakarta.persistence.EntityManager;
import model.*;
import org.flywaydb.core.Flyway;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import util.ApiResponse;
import util.PropertiesLoader;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.function.BinaryOperator;

public abstract class HibernateAbstractClass<P extends Model> implements HibernateService<P>{
    protected static final ApiResponse FAIL_API_RESPONSE = new ApiResponse(HttpServletResponse.SC_BAD_REQUEST, "Что-то пошло не так. Проверьте введенные данные.");
    protected static final ApiResponse OK_API_RESPONSE = new ApiResponse(200, "Все прошло успешно.");
    protected static final SessionFactory sessionFactory;

    static {

        try {
            Flyway flyway = Flyway.configure().dataSource(
                    PropertiesLoader.getProperty("hibernate.properties", "hibernate.connection.url"),
                    PropertiesLoader.getProperty("hibernate.properties", "hibernate.connection.username"),
                    PropertiesLoader.getProperty("hibernate.properties", "hibernate.connection.password")
            ).load();
            flyway.migrate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sessionFactory = new Configuration()
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

    @SuppressWarnings("unchecked")
    public static<T> List<?> executeSqlQuery(String sqlQuery){
        EntityManager entityManager = sessionFactory.createEntityManager();
        return (List<Object[]>) entityManager.createNativeQuery(sqlQuery).getResultList();
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
        } catch (HibernateException e){
            return FAIL_API_RESPONSE;
        }
    }

    protected ApiResponse doDelete(Class<P> className, Long id){
        try(Session session = openSession()){
            session.beginTransaction();
                session.remove(doReed(className,id));
            session.getTransaction().commit();
            return OK_API_RESPONSE;
        } catch (HibernateException e){
            return FAIL_API_RESPONSE;
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
        } catch (HibernateException e){
            return FAIL_API_RESPONSE;
        }
    }
}