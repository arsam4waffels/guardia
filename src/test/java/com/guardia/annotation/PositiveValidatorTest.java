package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link com.guardia.annotation.Positive} annotation.
 * Verifies rejection of zero and negative numbers, and null tolerance.
 */
class PositiveValidatorTest {

    static class Model {
        @Positive
        int value;
    }

    // ==================== Fail cases ====================

    @Test
    void shouldFailWhenValueIsZero() {
        Model model = new Model();
        model.value = 0;

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsNegative() {
        Model model = new Model();
        model.value = -1;

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsLargeNegative() {
        Model model = new Model();
        model.value = -999;

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldContainCorrectFieldName() {
        Model model = new Model();
        model.value = -1;

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("value")));
    }

    @Test
    void shouldThrowWhenValueIsNegative() {
        Model model = new Model();
        model.value = -1;

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    // ==================== Pass cases ====================

    @Test
    void shouldPassWhenValueIsOne() {
        Model model = new Model();
        model.value = 1;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsLargePositive() {
        Model model = new Model();
        model.value = 999;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    // ==================== Custom message ====================

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @Positive(message = "must be positive!")
            int value;
        }

        CustomModel model = new CustomModel();
        model.value = -1;

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().equals("must be positive!")));
    }
}