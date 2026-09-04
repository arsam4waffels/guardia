# Guardia

A lightweight, annotation-based Java validation library built from scratch.  
Guardia lets you validate any object using simple annotations — no configuration, no dependencies.

```java
Guardia.of(user)
       .validate()
       .throwIfInvalid();
```

---

## Project Structure

```
src/main/java/com/guardia/
│   Guardia.java                  ← Entry point
│
├── annotation/
│   ├── NotNull.java              ← @NotNull
│   ├── MinLength.java            ← @MinLength
│   ├── MaxLength.java            ← @MaxLength
│   ├── NotEmpty.java             ← @NotEmpty
│   ├── Positive.java             ← @Positive
│   ├── Email.java                ← @Email
│   └── Range.java                ← @Range
│
├── core/
│   ├── Constraint.java           ← Links annotation to validator
│   ├── ConstraintValidator.java  ← Generic validator interface
│   ├── GuardiaContext.java       ← Fluent API context
│   └── GuardiaEngine.java        ← Reflection engine
│
├── exception/
│   ├── ValidationException.java  ← Thrown when validation fails
│   └── ValidationError.java      ← Single field error model
│
└── validator/
    ├── NotNullValidator.java
    ├── MinLengthValidator.java
    ├── MaxLengthValidator.java
    ├── NotEmptyValidator.java
    ├── PositiveValidator.java
    ├── EmailValidator.java
    └── RangeValidator.java
```

---

## Quick Start

**1. Annotate your model:**

```java
public class User {

    @NotEmpty
    @MinLength(value = 3, message = "Name is too short")
    @MaxLength(value = 50, message = "Name is too long")
    private String name;

    @NotEmpty
    @Email
    private String email;

    @Range(min = 18, max = 100, message = "Age must be between 18 and 100")
    private int age;
}
```

`@NotEmpty` rejects both `null` and blank strings, while `@MinLength` and `@MaxLength` enforce string length limits. `@Email` validates the email format, and `@Range` validates numeric bounds.

**2. Validate:**

```java
User user = new User();
user.setName("Arsam");
user.setEmail("arsam@waffels.com");
user.setAge(21);

// Throw if invalid
Guardia.of(user)
       .validate()
       .throwIfInvalid();

// Or check manually
var context = Guardia.of(user).validate();

if (!context.isValid()) {
    context.getErrors().forEach(e ->
        System.out.println("[FAIL] " + e.getFieldName() + ": " + e.getMessage())
    );
}
```

---

## Built-in Annotations

All built-in constraints target fields and are retained at runtime for reflection-based validation.

| Annotation | Value | Description |
|---|---|---|
| `@NotNull` | — | Field must not be null |
| `@NotEmpty` | — | String must not be null or blank |
| `@MinLength(value)` | `int` | String length must be at least `value` |
| `@MaxLength(value)` | `int` | String length must be at most `value` |
| `@Email` | — | String must be a valid email address |
| `@Range(min, max)` | `long` | Number must be within the inclusive range |
| `@Positive` | — | Number must be greater than zero |

### Null handling

Length constraints and `@Positive` treat `null` as valid. Use `@NotNull` or `@NotEmpty` when a value is required.

For example:

```java
@NotNull
@Positive
private Integer score;
```

This allows Guardia to distinguish between two separate rules: the value must exist, and when present, it must be positive.

---

## Adding a Custom Annotation

Guardia is designed to be extended. Adding a new constraint does not require modifying the validation engine.

**1. Create the annotation:**

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EvenValidator.class)
public @interface Even {
    String message() default "Value must be even";
}
```

**2. Create the validator:**

```java
public class EvenValidator implements ConstraintValidator<Even, Number> {

    private String message;

    @Override
    public void initialize(Even annotation) {
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(Number value) {
        if (value == null) return true;
        return value.longValue() % 2 == 0;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```

**3. Use it:**

```java
@Even
private int quantity;
```

That's it. No engine changes needed :D.

---

## How It Works

```
@NotNull, @NotEmpty, @MinLength, @MaxLength, @Email, @Range, @Positive
                              ↓
                         @Constraint
                              ↓
                  Links annotation to validator
                              ↓
                       GuardiaEngine
                              ↓
                 Scans fields via Reflection
                              ↓
                  ConstraintValidator<A,T>
                              ↓
                    ValidationError
                              ↓
              ValidationException (if invalid)
```

Each built-in annotation is linked to its validator through `@Constraint`. The engine discovers annotated fields at runtime, initializes the corresponding validator with the annotation, and collects validation failures as `ValidationError` objects.

---

## API

```java
Guardia.of(object)          // Create a validation context
       .validate()          // Run validation
       .isValid()           // true / false
       .getErrors()         // List<ValidationError>
       .throwIfInvalid()    // Throws ValidationException if invalid
       .get()               // Returns the original object
```

---

## Tech

- **Java 17+**
- **Zero dependencies**

---

## What's Next

Guardia is in early beta. I have some ideas for the future — no promises:

- `@Pattern` — regex-based validation
- Nested object validation
- Collection validation (`List<T>`, `Map<K,V>`)
- Custom error message templates
- More built-in constraints
