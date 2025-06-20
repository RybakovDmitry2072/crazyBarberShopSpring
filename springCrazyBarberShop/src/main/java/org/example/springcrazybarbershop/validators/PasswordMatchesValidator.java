package org.example.springcrazybarbershop.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springcrazybarbershop.annotations.PasswordMatches;
import org.example.springcrazybarbershop.dto.form.auth.SignUpForm;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        SignUpForm form = (SignUpForm) obj;
        boolean isValid = form.getPassword().equals(form.getMatchingPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("matchingPassword")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
