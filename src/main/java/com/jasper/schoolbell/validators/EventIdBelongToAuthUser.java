package com.jasper.schoolbell.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventIdBelongToAuthUserValidator.class)
public @interface EventIdBelongToAuthUser {
    String message() default "You are not the host of the event with this ID";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
