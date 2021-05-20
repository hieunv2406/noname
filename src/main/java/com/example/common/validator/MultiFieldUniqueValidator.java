package com.example.common.validator;

import com.example.common.repository.CommonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author owl
 */
@Slf4j
public class MultiFieldUniqueValidator implements ConstraintValidator<MultiFieldUnique, Object> {
    private Class clazz;
    private String uniqueFields;
    private String idField;
    private CommonRepository commonRepository;

    public MultiFieldUniqueValidator(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public void initialize(MultiFieldUnique constraintAnnotation) {
        clazz = constraintAnnotation.clazz();
        uniqueFields = constraintAnnotation.uniqueFields();
        idField = constraintAnnotation.idField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        try {
            List<String> fields = Stream.of(uniqueFields.split(",")).collect(Collectors.toList());
            List<Object> params = new ArrayList<>();
            for (String field : fields) {
                Field fieldClass = obj.getClass().getDeclaredField(field);
                fieldClass.setAccessible(true);
                params.add(field);
                params.add(fieldClass.get(obj));
            }
            Long idFieldValue = 0l;
            if (BeanUtils.getProperty(obj, idField) != null) {
                idFieldValue = Long.valueOf(BeanUtils.getProperty(obj, idField));
            }
            return commonRepository
                    .checkUniqueWithMultiFields(clazz, idField, idFieldValue, params.toArray());
        } catch (IllegalAccessException e) {
            log.error("Accessor method is not available for class : {}, exception : {}",
                    obj.getClass().getName(), e);
        } catch (InvocationTargetException e) {
            log.error("An exception occurred while accessing class : {}, exception : {}",
                    obj.getClass().getName(), e);
        } catch (NoSuchMethodException e) {
            log.error("Method is not present on class : {}, exception : {}",
                    obj.getClass().getName(), e);
        } catch (NoSuchFieldException e) {
            log.error("Field is not present on class : {}, exception : {}",
                    obj.getClass().getName(), e);
        }
        return false;
    }
}
