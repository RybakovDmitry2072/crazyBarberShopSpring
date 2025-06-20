package org.example.springcrazybarbershop.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.springcrazybarbershop.validators.PhoneMatchesValidator;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Constraint(validatedBy = PhoneMatchesValidator.class)
@Retention(RUNTIME)
@Documented
public @interface PhoneMatches {
    String message() default "Пожалуйста, укажите действительный номер телефона.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
