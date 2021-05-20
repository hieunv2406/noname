package com.example.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author owl
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = MultiFieldUniqueValidator.class)
public @interface MultiFieldUnique {

    String message() default "{common.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class clazz();

    String uniqueFields() default "";

    String idField() default "";
}
