package database.hibernate;

import model.Skills;
import util.ApiResponse;

import java.util.List;
import java.util.function.BinaryOperator;

public class SkillsDtoService extends HibernateAbstractClass<Skills> implements HibernateService<Skills> {

    public SkillsDtoService() {
        super();
    }

    @Override
    public Skills read(Long id) {
        return doReed(Skills.class,id);
    }

    @Override
    public List<Skills> readAll() {
        return doAllRead(Skills.class);
    }

    @Override
    public ApiResponse save(Skills object) {
        return doSave(object);
    }

    @Override
    public ApiResponse delete(Long id) {
        return doDelete(Skills.class, id);
    }

    @Override
    public ApiResponse update(Skills objectForUpdate) {
        BinaryOperator<Skills> function = (mainObject, updateObject) ->
        {
            if (updateObject.getDegree() != null){
                mainObject.setDegree(updateObject.getDegree());
            }
            if (updateObject.getIndustry() != null){
                mainObject.setIndustry(updateObject.getIndustry());
            }
            return mainObject;
        };
        return doUpdate(Skills.class,objectForUpdate,function);
    }
}