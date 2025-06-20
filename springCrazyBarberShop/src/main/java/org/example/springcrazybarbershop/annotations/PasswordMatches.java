package org.example.springcrazybarbershop.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.springcrazybarbershop.validators.PasswordMatchesValidator;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Пароли не совпадают";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
