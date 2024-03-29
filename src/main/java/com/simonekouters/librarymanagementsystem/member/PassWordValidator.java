package com.simonekouters.librarymanagementsystem.member;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class PassWordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final int MIN_LENGTH = 8;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return lengthIsValid(password) && containsUppercase(password) && containsLowercase(password)
                && containsDigit(password) && containsSpecialCharacter(password);
    }

    private boolean lengthIsValid(String password) {
        return password.length() >= MIN_LENGTH;
    }

    private boolean containsUppercase(String password) {
        return !password.equals(password.toLowerCase());
    }

    private boolean containsLowercase(String password) {
        return !password.equals(password.toUpperCase());
    }

    private boolean containsDigit(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

    private boolean containsSpecialCharacter(String password) {
        String specialCharacters = "!@#$%^&*()-_+=<>?/[]{},.;:'\"\\|";
        for (char c : password.toCharArray()) {
            if (specialCharacters.contains(Character.toString(c))) {
                return true;
            }
        }
        return false;
    }
}

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassWordValidator.class)
@interface ValidPassword {
    String message() default "Password must have at least one uppercase letter, one lowercase letter, one digit, and one special character\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

