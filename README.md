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
    ├── EmailValidator.java
    └── RangeValidator.java
```

---

## Quick Start

**1. Annotate your model:**

```java
public class User {

    @NotNull
    @MinLength(value = 3, message = "Name is too short")
    private String name;

    @NotNull
    @Email
    private String email;

    @Range(min = 18, max = 100, message = "Age must be between 18 and 100")
    private int age;
}
```

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

| Annotation | Target | Description |
|---|---|---|
| `@NotNull` | Any | Field must not be null |
| `@MinLength(value)` | String | Minimum character length |
| `@Email` | String | Must be a valid email address |
| `@Range(min, max)` | Number | Value must be within range |

---

## Adding a Custom Annotation

Guardia is designed to be extended. Adding a new constraint never requires touching the engine.

**1. Create the annotation:**

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveValidator.class)
public @interface Positive {
    String message() default "Value must be positive";
}
```

**2. Create the validator:**

```java
public class PositiveValidator implements ConstraintValidator<Positive, Number> {

    private String message;

    @Override
    public void initialize(Positive annotation) {
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(Number value) {
        if (value == null) return true;
        return value.doubleValue() > 0;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```

**3. Use it:**

```java
@Positive
private int price;
```

That's it. No engine changes needed :D.

---

## How It Works

```
@NotNull, @Email, @Range         Annotations mark fields with rules
         ↓
    @Constraint                  Links each annotation to its validator
         ↓
    GuardiaEngine                Scans fields via Reflection at runtime
         ↓
  ConstraintValidator<A,T>       Generic interface — one per annotation
         ↓
    ValidationError              Captures field name, message, rejected value
         ↓
  ValidationException            Thrown with the full list of errors
```

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

- `@MaxLength` — maximum character length
- `@Pattern` — regex-based validation
- `@NotEmpty` — not null and not blank
- Nested object validation
- Collection validation (`List<T>`, `Map<K,V>`)
- Custom error message templates
---

> Built by [Arsam](https://github.com/arsam) as a deep dive into Java internals.