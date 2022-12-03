package com.jasper.schoolbell.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserPhoneNumberIsUniqueValidator.class)
public @interface UserPhoneNumberIsUnique {
    String message() default "User with phone number already exists";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
