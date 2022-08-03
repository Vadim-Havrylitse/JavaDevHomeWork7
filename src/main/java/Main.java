import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.hibernate.HibernateAbstractClass;
import model.Developers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper json = new ObjectMapper();
//        List<?> objects = HibernateAbstractClass.doSqlQuery(sql, Developers.class);
//        System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(objects));

        List<?> objects2 = HibernateAbstractClass.executeSqlNamedQuery("all_middle_dev", Developers.class);
        System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(objects2));

        List<String> labels = getLabels(Developers.class, objects2.get(0));

        System.out.println(labels);

    }

    public static List<String> getLabels(Class<?> classResult, Object objectResult) {
        List<String> result = new ArrayList<>();
        for (Field field:classResult.getDeclaredFields()){
            field.setAccessible(true);
            try{
                if (field.get(objectResult) != null){
                    result.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(false);
            }
        }
        return result;
    }
}
