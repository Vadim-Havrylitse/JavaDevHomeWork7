package database.hibernate;

import model.Customers;
import util.ApiResponse;

import java.util.List;
import java.util.function.BinaryOperator;

public class CustomersDaoService extends HibernateAbstractClass<Customers> implements HibernateService<Customers>{
    @Override
    public Customers read(Long id) {
        return doReed(Customers.class, id);
    }

    @Override
    public List<Customers> readAll() {
        return doAllRead(Customers.class);
    }

    @Override
    public ApiResponse save(Customers object) {
        return doSave(object);
    }

    @Override
    public ApiResponse delete(Long id) {
        return doDelete(Customers.class, id);
    }

    @Override
    public ApiResponse update(Customers objectForUpdate) {
        BinaryOperator<Customers> function = (x1, x2) ->{
            if(x2.getName() != null){
                x1.setName(x2.getName());
            }
            if(x2.getSurname() != null){
                x1.setSurname(x2.getSurname());
            }
            return x1;
        };
        return doUpdate(Customers.class, objectForUpdate,function);
    }
}