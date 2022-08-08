package service;

import database.hibernate.HibernateService;
import model.Companies;
import model.Customers;
import model.Projects;
import model.Skills;
import util.ApiEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EntityParsingService {

    public static <T> T parseRequestToEntity(HttpServletRequest req, Class<T> className) throws IOException {
        try {
            Constructor<T> constructor = className.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();
            for (Field field : className.getDeclaredFields()) {
                field.setAccessible(true);
                Type type = field.getGenericType();
                if (type.getTypeName().contains("Set") ) {
                    String[] parameterValues = req.getParameterValues(field.getName());
                    if (parameterValues != null) {
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        Type genericType = parameterizedType.getActualTypeArguments()[0];
                        Set<Object> objectsSet = new HashSet<>(Collections.emptySet());
                        Arrays.stream(parameterValues).forEach(id -> objectsSet.add(parseType(id, genericType)));
                        field.set(instance, objectsSet);
                    }
                } else {
                    String parameterValue = req.getParameter(field.getName());
                    if (parameterValue == null || parameterValue.isBlank()) {
                        continue;
                    }
                    field.set(instance, parseType(parameterValue, type));
                }
            }
            return instance;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IOException("parseRequestToDto() is fail!");
    }

    @SuppressWarnings("ConstantConditions")
    private static Object parseType(Object value, Type type) {
        if (value.toString() == null) {
            return null;
        }
        Object result = null;
        if (String.class.equals(type)) {
            result = value.toString();
        } else if (Byte.class.equals(type)) {
            result = Byte.valueOf(value.toString());
        } else if (Integer.class.equals(type)) {
            result = Integer.valueOf(value.toString());
        } else if (Short.class.equals(type)) {
            result = Short.valueOf(value.toString());
        } else if (Long.class.equals(type)) {
            result = Long.valueOf(value.toString());
        } else if (Float.class.equals(type)) {
            result = Float.valueOf(value.toString());
        } else if (Double.class.equals(type)) {
            result = Double.valueOf(value.toString());
        } else if (Boolean.class.equals(type)) {
            result = Boolean.valueOf(value.toString());
        } else if (Companies.class.equals(type)) {
            result = HibernateService.getInstance(ApiEntity.COMPANIES).read(Long.valueOf(value.toString()));
        } else if (Customers.class.equals(type)) {
            result = HibernateService.getInstance(ApiEntity.CUSTOMERS).read(Long.valueOf(value.toString()));
        } else if (Projects.class.equals(type)) {
            result = HibernateService.getInstance(ApiEntity.PROJECTS).read(Long.valueOf(value.toString()));
        } else if (Skills.class.equals(type)) {
            result = HibernateService.getInstance(ApiEntity.SKILLS).read(Long.valueOf(value.toString()));
        }
        return result;
    }
}