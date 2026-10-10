package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositiveOrZeroValidatorTest {

    static class Model {
        @PositiveOrZero
        Integer value;
    }

    @Test
    void shouldPassWhenValueIsZero() {
        Model model = new Model();
        model.value = 0;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsPositive() {
        Model model = new Model();
        model.value = 42;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsNegative() {
        Model model = new Model();
        model.value = -1;

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldAllowNull() {
        Model model = new Model();
        model.value = null;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldThrowWhenValueIsNegative() {
        Model model = new Model();
        model.value = -10;

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @PositiveOrZero(message = "must not be negative")
            Integer value = -1;
        }

        var context = Guardia.of(new CustomModel()).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(error -> error.getMessage().equals("must not be negative")));
    }
}
