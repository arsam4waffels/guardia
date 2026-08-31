package com.guardia.core;

import com.guardia.exception.ValidationError;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GuardiaEngine {
    public List<ValidationError> collectErrors(Object object) {
        Class<?> clazz = object.getClass();
        List<ValidationError> errors = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    Constraint constraint = annotation.annotationType()
                            .getAnnotation(Constraint.class);
                    if (constraint == null) continue;
                    for (Class<? extends ConstraintValidator<?, ?>> validatorClass
                            : constraint.validatedBy()) {
                        ConstraintValidator validator =
                                validatorClass.getDeclaredConstructor().newInstance();
                        validator.initialize(annotation);
                        if (!validator.isValid(value)) {
                            errors.add(new ValidationError(
                                    field.getName(),
                                    validator.getMessage(),
                                    value
                            ));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Guardia engine error", e);
            } finally {
                field.setAccessible(false);
            }
        }
        return errors;
    }
}