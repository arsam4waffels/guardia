package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotBlankValidatorTest {

    static class Model {
        @NotBlank
        String value;
    }

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
    void shouldFailWhenValueIsWhitespace() {
        Model model = new Model();
        model.value = "\t\n";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueHasContent() {
        Model model = new Model();
        model.value = "  hello  ";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldThrowWhenValueIsBlank() {
        Model model = new Model();
        model.value = " ";

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

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @NotBlank(message = "field is required")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = " ";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().equals("field is required")));
    }
}
