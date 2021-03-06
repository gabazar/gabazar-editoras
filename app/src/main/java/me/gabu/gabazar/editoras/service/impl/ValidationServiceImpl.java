package me.gabu.gabazar.editoras.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import me.gabu.gabazar.editoras.core.exceptions.BadRequestException;
import me.gabu.gabazar.editoras.core.model.Editora;
import me.gabu.gabazar.editoras.service.ValidationService;
import me.gabu.gabazar.editoras.service.validations.Create;
import me.gabu.gabazar.editoras.service.validations.Update;
import me.gabu.gabazar.editoras.service.validations.ValidationEnum;

@Service
public class ValidationServiceImpl implements ValidationService {

    private @Autowired Validator validator;

    @Override
    public void validate(Editora editora, ValidationEnum validation) {

        Set<ConstraintViolation<Editora>> contraints = getContraints(editora, validation);

        if (!CollectionUtils.isEmpty(contraints))
            throw new BadRequestException(processaContraint(contraints));

    }

    private Set<ConstraintViolation<Editora>> getContraints(Editora editora, ValidationEnum validation) {
        switch (validation) {
        case CREATE:
            return getValidator().validate(editora, Create.class);

        case UPDATE:
        default:
            return getValidator().validate(editora, Update.class);
        }
    }

    protected Validator getValidator() {
        return validator;
    }

    private List<String> processaContraint(Set<ConstraintViolation<Editora>> contraints) {
        return contraints.stream()
                .map(violation -> String.format("[%s %s]",
                        StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                .reduce((first, second) -> second).orElse(null),
                        violation.getMessage()))
                .collect(Collectors.toList());
    }

}
