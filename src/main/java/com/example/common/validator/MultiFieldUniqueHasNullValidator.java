package com.example.common.validator;

import lombok.extern.log4j.Log4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j
public class MultiFieldUniqueHasNullValidator implements ConstraintValidator<MultiFieldUniqueHasNull, Object> {

    private Class clazz;
    private String uniqueFields;
    private String idField;


    @Override
    public void initialize(MultiFieldUniqueHasNull unique) {
        clazz = unique.clazz();
        uniqueFields = unique.uniqueFields();
        idField = unique.idField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        try {
            List<String> fields = Stream.of(uniqueFields.split(",")).collect(Collectors.toList());
            List<Object> params = new ArrayList<>();
            Map<String, Object> mapField = new HashMap<>();
            for (String field : fields) {
                Field fieldClass = obj.getClass().getDeclaredField(field);
                fieldClass.setAccessible(true);
                params.add(field);
                mapField.put(fieldClass.getName(), fieldClass.get(obj));
            }
            Long idFieldValue = 0L;
//            if (BeanUtils.getProperty()){
//
//            }

        } catch (Exception e) {

        }

        return false;
    }
}
