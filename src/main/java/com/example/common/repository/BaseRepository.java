package com.example.common.repository;

import com.example.common.Constants;
import com.example.common.dto.Datatable;
import com.example.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public abstract class BaseRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public Datatable getListDataTableBySqlQuery(String sqlQuery, Map<String, Object> parameters, int page,
                                                int pageSize, Class<?> mappedClass, String sortName, String sortType) {
        Datatable dataReturn = new Datatable();
        String sqlQueryResult = " SELECT * FROM ( SELECT * FROM ( SELECT * FROM ( "
                + sqlQuery
                + " ) abc ";
        if (sortName != null) {
            Field[] fields = FieldUtils.getAllFields(mappedClass);
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
        sqlQueryResult += " ) bcd LIMIT :p_page_length offset :p_page_number ) T_TABLE_NAME, ";
        sqlQueryResult += " ( SELECT COUNT(*) totalRow FROM ( "
                + sqlQuery
                + " ) T_TABLE_TOTAL ) ";
        sqlQueryResult += "T_TABLE_NAME_TOTAL ";
        parameters.put("p_page_number", (page - 1) * pageSize);
        parameters.put("p_page_length", pageSize);
        List<?> list = getNamedParameterJdbcTemplate().query(sqlQueryResult, parameters, BeanPropertyRowMapper.newInstance(mappedClass));
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

    //Mysql dự phòng
    //    public Datatable getListDataTableBySqlQuery(String sqlQuery, Map<String, Object> parameters,
//                                                int page, int pageSize, Class<?> mappedClass, String sortName, String sortType) {
//        Datatable dataReturn = new Datatable();
//        String sqlQueryResult = " SELECT * FROM ( SELECT * FROM ( SELECT *, a.rownum indexRow FROM ( SELECT *, @rownum := @rownum + 1 as rownum FROM ( "
//                + sqlQuery
//                + " ) c , (select @rownum := 0) r ";
//        if (sortName != null) {
//            Field[] fields = FieldUtils.getAllFields(mappedClass);
//            Map<String, String> mapField = new HashMap<>();
//            for (Field field : fields) {
//                mapField.put(field.getName(), field.getName());
//            }
//            if ("asc".equalsIgnoreCase(sortType)) {
//                sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " ASC";
//            } else if ("desc".equalsIgnoreCase(sortType)) {
//                sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " DESC";
//            } else {
//                sqlQueryResult += " ORDER BY " + mapField.get(sortName);
//            }
//        }
//        sqlQueryResult += " ) a WHERE a.rownum < ((:p_page_number * :p_page_length) + 1 )) b WHERE b.indexRow >= (((:p_page_number-1) * :p_page_length) + 1) "
//                + " ) T_TABLE_NAME, ";
//        sqlQueryResult += " ( SELECT COUNT(*) totalRow FROM ( "
//                + sqlQuery
//                + " ) T_TABLE_TOTAL ) ";
//        sqlQueryResult += "T_TABLE_NAME_TOTAL ";
//        parameters.put("p_page_number", page);
//        parameters.put("p_page_length", pageSize);
//        List<?> list = getNamedParameterJdbcTemplate().query(sqlQueryResult, parameters, BeanPropertyRowMapper.newInstance(mappedClass));
//        int count = 0;
//        if (list.isEmpty()) {
//            dataReturn.setTotal(count);
//        } else {
//            try {
//                Object obj = list.get(0);
//                Field field = obj.getClass().getSuperclass().getDeclaredField("totalRow");
//                field.setAccessible(true);
//                count = Integer.parseInt(field.get(obj).toString());
//                dataReturn.setTotal(count);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        if (pageSize > 0) {
//            if (count % pageSize == 0) {
//                dataReturn.setPages(count / pageSize);
//            } else {
//                dataReturn.setPages((count / pageSize) + 1);
//            }
//        }
//        dataReturn.setData(list);
//        return dataReturn;
//    }

    //oracle
/*
    public Datatable getListDataTableBySqlQuery(String sqlQuery, Map<String, Object> parameters,
                                                int page, int pageSize, Class<?> mappedClass, String sortName, String sortType) {
        Datatable dataReturn = new Datatable();
        String sqlQueryResult = " SELECT * FROM ( SELECT * FROM ( SELECT a.*, rownum indexRow FROM ( SELECT * FROM ( "
                + sqlQuery
                + " ) ";
        if (sortName != null) {
            Field[] fields = FieldUtils.getAllFields(mappedClass);
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
        List<?> list = getNamedParameterJdbcTemplate().query(sqlQueryResult, parameters, BeanPropertyRowMapper.newInstance(mappedClass));
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
    }*/

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

    public String getSQLQueryFromFile(String resourceFolder, String fileName) {
        StringBuilder stringBuilder = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            String filePath = "sql/" + resourceFolder + "/" + fileName + ".sql";
            InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            stringBuilder = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public String getSQLFromFile(String resourceFolder, String fileName) {
        File folder;
        try {
            folder = new ClassPathResource("sql" + File.separator + resourceFolder + File.separator + fileName + ".sql").getFile();

            if (folder.isFile()) {
                String sql = new String(Files.readAllBytes(Paths.get(folder.getAbsolutePath())));
                return sql;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * Check unique with multiple field
     */
    public boolean checkUniqueWithMultiFields(Class<T> persistentClass, String idField, Long idValue,
                                              Object... params) {
        Map<String, Object> mapParams = new HashMap<>();
        String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (i % 2 == 0) {
                    if (params[i + 1] != null) {
                        boolean isNumber = false;
                        try {
                            Field field = persistentClass.getDeclaredField(String.valueOf(params[i]));
                            String returnType = field.getType().getSimpleName().toUpperCase();
                            if (Constants.TYPE_NUMBER.indexOf(returnType) >= 0) {
                                isNumber = true;
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        if (params[i + 1] instanceof String && !isNumber) {
                            sqlQuery += " AND lower(t." + params[i] + ") = :p_" + params[i] + " ";
                            mapParams.put("p_" + params[i], params[i + 1].toString().trim().toLowerCase());
                        } else {
                            sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
                            mapParams.put("p_" + params[i], params[i + 1]);
                        }
                    }
                }
            }
        }
        sqlQuery += " AND t." + idField + " <> :p_" + idField;
        Query query = entityManager.createQuery(sqlQuery);
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            try {
                String fieldName = entry.getKey().split("_")[1];
                Field field = persistentClass.getDeclaredField(fieldName);
                String returnType = field.getType().getSimpleName().toUpperCase();
                if ("Double".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Double.valueOf(String.valueOf(entry.getValue())));
                } else if ("Integer".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Integer.valueOf(String.valueOf(entry.getValue())));
                } else if ("Float".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Float.valueOf(String.valueOf(entry.getValue())));
                } else if ("Short".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Short.valueOf(String.valueOf(entry.getValue())));
                } else if ("Long".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Long.valueOf(String.valueOf(entry.getValue())));
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
                continue;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("p_" + idField, idValue);
        List<T> lst = query.getResultList();
        return lst == null || lst.size() == 0;
    }

}
