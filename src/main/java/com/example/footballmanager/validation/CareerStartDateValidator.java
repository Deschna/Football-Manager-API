package com.example.footballmanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CareerStartDateValidator implements ConstraintValidator<PastOrPresent, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            context.buildConstraintViolationWithTemplate("Date cannot be null").addConstraintViolation();
            return false;
        }

        LocalDate today = LocalDate.now();
        return value.isBefore(today) || value.isEqual(today);
    }
}
