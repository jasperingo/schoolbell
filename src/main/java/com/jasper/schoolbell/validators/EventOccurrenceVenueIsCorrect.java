package com.jasper.schoolbell.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventOccurrenceVenueIsCorrectValidator.class)
public @interface EventOccurrenceVenueIsCorrect {
    String message() default "Fill the link field for virtual venue and the address field for physical venue";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    class MyPayload implements ValidationPayload {
        @Override
        public String getName() {
            return "venue";
        }
    }
}
