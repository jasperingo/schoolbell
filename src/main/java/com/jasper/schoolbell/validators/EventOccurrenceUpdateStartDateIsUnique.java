package com.jasper.schoolbell.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventOccurrenceUpdateStartDateIsUniqueValidator.class)
public @interface EventOccurrenceUpdateStartDateIsUnique {
    String message() default "This event start date, clashes with another event start date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
