# common-utils/file

Classpath resource loading and basic file I/O utilities for tests.

## Dependency

```gradle
testImplementation project(":common-utils:file")
```

## Usage

Load a classpath resource or compare file contents:

```java
InputStream stream = FileUtils.getNonNullResourceAsStream("data/payload.json");
boolean equal = FileUtils.isFilesContentEquals(expectedFile, actualFile);
```

Create a temp file and append text:

```java
File tmp = FileUtils.createTempFile(FileExtension.JSON);
FileUtils.addTextToFile(tmp.getName(), "{\"key\":\"value\"}");
```

Unpack a zip archive with `ZipUtils`:

```java
ZipUtils.unzip(zipFile, outputDir);
```
