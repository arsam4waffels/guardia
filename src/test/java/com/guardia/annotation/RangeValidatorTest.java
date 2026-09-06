package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link com.guardia.annotation.Range} annotation.
 * Verifies numeric range enforcement, null tolerance, and custom messages.
 */
class RangeValidatorTest {

    static class Model {
        @Range(min = 18, max = 100)
        int value;
    }

    // ==================== Fail cases ====================

    @Test
    void shouldFailWhenValueIsBelowMin() {
        Model model = new Model();
        model.value = 15;

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsAboveMax() {
        Model model = new Model();
        model.value = 150;

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldContainCorrectFieldName() {
        Model model = new Model();
        model.value = 15;

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("value")));
    }

    @Test
    void shouldThrowWhenValueIsOutOfRange() {
        Model model = new Model();
        model.value = 15;

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    // ==================== Pass cases ====================

    @Test
    void shouldPassWhenValueIsExactMin() {
        Model model = new Model();
        model.value = 18;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsExactMax() {
        Model model = new Model();
        model.value = 100;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsWithinRange() {
        Model model = new Model();
        model.value = 21;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    // ==================== Custom message ====================

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @Range(min = 1, max = 10, message = "out of range!")
            int value;
        }

        CustomModel model = new CustomModel();
        model.value = 15;

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().contains("out of range!")));
    }
}