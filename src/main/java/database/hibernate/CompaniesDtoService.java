package database.hibernate;

import model.Companies;
import util.ApiResponse;

import java.util.List;
import java.util.function.BinaryOperator;

public class CompaniesDtoService extends HibernateAbstractClass<Companies> implements HibernateService<Companies> {
    @Override
    public Companies read(Long id) {
        return doReed(Companies.class, id);
    }

    @Override
    public List<Companies> readAll() {
        return doAllRead(Companies.class);
    }

    @Override
    public ApiResponse save(Companies object) {
        return doSave(object);
    }

    @Override
    public ApiResponse delete(Long id) {
        return doDelete(Companies.class, id);
    }

    @Override
    public ApiResponse update(Companies object) {
        BinaryOperator<Companies> function = (x1, x2) -> {
            if (x2.getCity() != null){
                x1.setCity(x2.getCity());
            }
            if (x2.getName() != null){
                x1.setName(x2.getName());
            }
            if (x2.getCountry() != null){
                x1.setCountry(x2.getCountry());
            }
            return x1;
        };
        return doUpdate(Companies.class, object, function);
    }
}
