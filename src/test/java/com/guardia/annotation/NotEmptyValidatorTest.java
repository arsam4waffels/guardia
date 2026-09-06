package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link com.guardia.annotation.NotEmpty} annotation.
 * Verifies rejection of null, empty, and whitespace-only strings.
 */
class NotEmptyValidatorTest {

    static class Model {
        @NotEmpty
        String value;
    }

    // ==================== Fail cases ====================

    @Test
    void shouldFailWhenValueIsNull() {
        Model model = new Model();
        model.value = null;

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsEmpty() {
        Model model = new Model();
        model.value = "";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsBlank() {
        Model model = new Model();
        model.value = "   ";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsTab() {
        Model model = new Model();
        model.value = "\t";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldThrowWhenValueIsEmpty() {
        Model model = new Model();
        model.value = "";

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    @Test
    void shouldContainCorrectFieldName() {
        Model model = new Model();
        model.value = "";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("value")));
    }

    // ==================== Pass cases ====================

    @Test
    void shouldPassWhenValueIsNotEmpty() {
        Model model = new Model();
        model.value = "Arsam";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueHasContent() {
        Model model = new Model();
        model.value = "  hello  ";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    // ==================== Custom message ====================

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @NotEmpty(message = "field is empty!")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = "";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().equals("field is empty!")));
    }
}