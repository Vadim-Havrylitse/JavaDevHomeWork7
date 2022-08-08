package database.hibernate;

import model.Projects;
import util.ApiResponse;

import java.util.List;
import java.util.function.BinaryOperator;

public class ProjectsDaoService extends HibernateAbstractClass<Projects> {
    @Override
    public Projects read(Long id) {
        return doReed(Projects.class, id);
    }

    @Override
    public List<Projects> readAll() {
        return doAllRead(Projects.class);
    }

    @Override
    public ApiResponse save(Projects object) {
        return doSave(object);
    }

    @Override
    public ApiResponse delete(Long id) {
        return doDelete(Projects.class, id);
    }

    @Override
    public ApiResponse update(Projects object) {
        BinaryOperator<Projects> function = (x1, x2) -> {
            if (x2.getName() != null){
                x1.setName(x2.getName());
            }
            if (x2.getBudget() != null){
                x1.setBudget(x2.getBudget());
            }
            if (x2.getReleaseDate() != null){
                x1.setReleaseDate(x2.getReleaseDate());
            }
            if (x2.getCompany() != null){
                x1.setCompany(x2.getCompany());
            }
            if (x2.getCustomer() != null){
                x1.setCustomer(x2.getCustomer());
            }
          return x1;
        };
        return doUpdate(Projects.class, object, function);
    }
}
