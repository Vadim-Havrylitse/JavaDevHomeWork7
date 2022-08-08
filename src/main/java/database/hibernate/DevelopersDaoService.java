package database.hibernate;

import org.hibernate.Session;
import model.Developers;
import util.ApiResponse;
import model.Projects;
import model.Skills;

import java.util.List;
import java.util.function.BinaryOperator;

public class DevelopersDaoService extends HibernateAbstractClass<Developers>{
    @Override
    public Developers read(Long id) {
        try (Session session = openSession()){
            Developers developers = session.get(Developers.class, id);
            for (Skills skills:developers.getSkills());
            for (Projects pr:developers.getProjects());
            return developers;
        }
    }

    @Override
    public List<Developers> readAll() {
        try (Session session = openSession()){
            session.beginTransaction();
            List<Developers> devList = session.createQuery("SELECT a FROM Developers a", Developers.class).getResultList();
            devList.forEach(el -> {
                for (Skills skills:el.getSkills());
                for (Projects pr:el.getProjects());
            });
            session.getTransaction().commit();
            return devList;
        }
    }

    @Override
    public ApiResponse save(Developers object) {
        return doSave(object);
    }

    @Override
    public ApiResponse delete(Long id) {
        return doDelete(Developers.class, id);
    }

    @Override
    public ApiResponse update(Developers object) {
        BinaryOperator<Developers> function = (x1, x2) -> {
            if (x2.getName() != null){
                x1.setName(x2.getName());
            }
            if (x2.getAge() != null){
                x1.setAge(x2.getAge());
            }
            if (x2.getSex() != null){
                x1.setSex(x2.getSex());
            }
            if (x2.getSalary() != null){
                x1.setSalary(x2.getSalary());
            }
            if (x2.getSurname() != null){
                x1.setSurname(x2.getSurname());
            }
            if (x2.getCompany() != null){
                x1.setCompany(x2.getCompany());
            }
            if (x2.getProjects() != null){
                x1.setProjects(x2.getProjects());
            }
            if (x2.getSkills() != null){
                x1.setSkills(x2.getSkills());
            }
            return x1;
        };
        return doUpdate(Developers.class, object, function);
    }
}
