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
                return new SkillsDtoService();
            case PROJECTS:
                return new ProjectsDtoService();
            case COMPANIES:
                return new CompaniesDtoService();
            case CUSTOMERS:
                return new CustomersDtoService();
            case DEVELOPERS:
                return new DevelopersDtoService();
        }
        return null;
    }
}