package com.brinvex.brokercon.core.internal.facade;

import com.brinvex.brokercon.core.api.facade.ValidatorFacade;
import com.brinvex.brokercon.core.api.general.Validatable;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidatorFacadeImpl implements ValidatorFacade {

    private final jakarta.validation.Validator jakartaValidator;

    public ValidatorFacadeImpl(jakarta.validation.Validator jakartaValidator) {
        this.jakartaValidator = jakartaValidator;
    }

    @Override
    public <T extends Validatable> Set<ConstraintViolation<T>> validate(T validatable) {
        return jakartaValidator.validate(validatable);
    }

}
