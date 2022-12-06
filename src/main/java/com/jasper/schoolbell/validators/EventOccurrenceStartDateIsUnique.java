package com.jasper.schoolbell.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventOccurrenceStartDateIsUniqueValidator.class)
public @interface EventOccurrenceStartDateIsUnique {
    String message() default "This event start date, clashes with another event start date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    class MyPayload implements ValidationPayload {
        @Override
        public String getName() {
            return "startedAt";
        }
    }
}
