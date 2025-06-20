package org.example.springcrazybarbershop.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springcrazybarbershop.annotations.PhoneMatches;

import java.util.regex.Pattern;

public class PhoneMatchesValidator implements ConstraintValidator<PhoneMatches, Object> {

    private final static String PHONE_PATTERN ="^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$";

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        String phone = (String) object;
        return Pattern.matches(PHONE_PATTERN, phone);
    }
}
