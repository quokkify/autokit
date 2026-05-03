# common-utils/file

Classpath resource loading, file I/O utilities, and zip archive support for tests.

## Dependency

```gradle
testImplementation project(":common-utils:file")
```

## Usage

Load test data from the classpath and compare actual output against an expected file:

```java
InputStream payload = FileUtils.getNonNullResourceAsStream("data/users.json");
String path = FileUtils.getResourcePath("data/expected-report.csv");

File expected = new File(path);
File actual = generateReport();
assertThat(FileUtils.isFilesContentEquals(expected, actual)).isTrue();
```

Write results incrementally to a temp file during a test run:

```java
File results = FileUtils.createTempFile(FileUtils.FileExtension.CSV);
FileUtils.addTextToFile(results.getName(), "id,status");
FileUtils.addTextsToFile(results.getName(), List.of("1,PASS", "2,FAIL"));
```

## Key API

| Method | Returns | Notes |
|---|---|---|
| `getResourceAsStream(path)` | `InputStream` | `null` if resource missing |
| `getNonNullResourceAsStream(path)` | `InputStream` | throws if missing |
| `getResourcePath(path)` | `String` | absolute filesystem path |
| `isResourceExist(path)` | `boolean` | — |
| `addTextToFile(fileName, text)` | `void` | appends line, thread-safe |
| `addTextsToFile(fileName, list)` | `void` | appends as `[a,b]` |
| `createTempFile(extension)` | `File` | uses `FileExtension` enum |
| `isFilesContentEquals(f1, f2)` | `boolean` | byte-level comparison |
| `ZipUtils` | — | zip / unzip archive operations |
