package com.jasper.schoolbell.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventIdExistsValidator.class)
public @interface EventIdExists {
    String message() default "This event id is invalid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
