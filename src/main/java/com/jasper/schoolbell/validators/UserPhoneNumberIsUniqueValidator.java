package com.jasper.schoolbell.validators;

import com.jasper.schoolbell.repositories.UsersRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserPhoneNumberIsUniqueValidator implements ConstraintValidator<UserPhoneNumberIsUnique, String> {

    @Inject
    private UsersRepository repository;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return !repository.existsByPhoneNumber(phoneNumber);
    }
}
