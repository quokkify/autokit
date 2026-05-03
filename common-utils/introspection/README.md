# Introspection

Scans the classpath to find all methods carrying a specific annotation, enabling auto-discovery in test frameworks.

## Dependency

```gradle
testImplementation project(":common-utils:introspection")
```

## Usage

Retrieve every method annotated with a given annotation from a package:

```java
Set<Method> steps = ClasspathScanner.getMethodsWithAnnotationFromPackage(
        "com.example.steps",
        Step.class
);
```

Use `ReflectionUtils` for general reflection helpers such as invoking private methods or reading field values during test setup.
