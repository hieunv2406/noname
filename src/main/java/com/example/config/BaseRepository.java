package com.example.config;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Datatable getListDataTableBySqlQuery(String sqlQuery, Map<String, Object> parameters,
                                                int page, int pageSize, Class<?> mappedClass, String sortName, String sortType) {
        Date startTime = new Date();
        Datatable dataReturn = new Datatable();
        String sqlQueryResult = " SELECT * FROM ( SELECT * FROM ( SELECT a.*, rownum indexRow FROM ( SELECT * FROM ( "
                + sqlQuery
                + " ) ";
        if (sortName != null) {
            Field[] fields = fieldUtils.getAllFields(mappedClass);
            Map<String, String> mapField = new HashMap<>();
            for (Field field : fields) {
                mapField.put(field.getName(), field.getName());
            }
            if ("asc".equalsIgnoreCase(sortType)) {
                sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " ASC";
            } else if ("desc".equalsIgnoreCase(sortType)) {
                sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " DESC";
            } else {
                sqlQueryResult += " ORDER BY " + mapField.get(sortName);
            }
        }
        sqlQueryResult += " ) a WHERE rownum < ((:p_page_number * :p_page_length) + 1 )) WHERE indexRow >= (((:p_page_number-1) * :p_page_length) + 1) "
                + " ) T_TABLE_NAME, ";
        sqlQueryResult += " ( SELECT COUNT(*) totalRow FROM ( "
                + sqlQuery
                + " ) T_TABLE_TOTAL ) ";
        sqlQueryResult += "T_TABLE_NAME_TOTAL ";
        parameters.put("p_page_number", page);
        parameters.put("p_page_length", pageSize);
        List<?> list = getNamedParameterJdbcTemplateNormal().query(sqlQueryResult, parameters, BeanPropertyRowMapper.newInstance(mappedClass));
        int count = 0;
        if (list.isEmpty()) {
            dataReturn.setTotal(count);
        } else {
            try {
                Object obj = list.get(0);
                Field field = obj.getClass().getSuperclass().getDeclaredField("totalRow");
                field.setAccessible(true);
                count = Integer.parseInt(field.get(obj).toString());
                dataReturn.setTotal(count);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (pageSize > 0) {
            if (count % pageSize == 0) {
                dataReturn.setPages(count / pageSize);
            } else {
                dataReturn.setPages((count / pageSize) + 1);
            }
        }
        dataReturn.setData(list);
        return dataReturn;
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll(Class<T> persistentClass) {
        String sqlQuery = " SELECT t FROM " + persistentClass.getSimpleName() + " t";
        return entityManager.createQuery(sqlQuery).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByMultilParam(Class<T> persistentClass, Object... params) {
        Map<String, Object> mapParams = new HashMap<>();
        String sqlQuery = " SELECT t FROM " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (i % 2 == 0) {
                    sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
                    mapParams.put("p_" + params[i], params[i + 1]);
                }
            }
        }
        Query query = entityManager.createQuery(sqlQuery);
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

}
