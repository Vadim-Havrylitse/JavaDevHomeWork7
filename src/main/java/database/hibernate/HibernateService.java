package database.hibernate;

import model.Model;
import util.ApiEntity;
import util.ApiResponse;

import java.util.List;

public interface HibernateService<T extends Model> {
    T read(Long id);

    List<T> readAll();

    ApiResponse save(T object);

    ApiResponse delete(Long id);

    ApiResponse update(T object);

    @SuppressWarnings("rawtypes")
    static HibernateService getInstance(ApiEntity entityName) {
        switch (entityName) {
            case SKILLS:
                return new SkillsDaoService();
            case PROJECTS:
                return new ProjectsDaoService();
            case COMPANIES:
                return new CompaniesDaoService();
            case CUSTOMERS:
                return new CustomersDaoService();
            case DEVELOPERS:
                return new DevelopersDaoService();
        }
        return null;
    }
}