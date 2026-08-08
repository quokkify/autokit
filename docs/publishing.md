# Publishing Q4J to Maven Central

Q4J publishes signed Java modules under the Maven group `io.github.quokkify`.
Java source packages use the independent namespace `dev.quokkify`.

## One-time Central Portal setup

1. Sign in to the [Central Portal](https://central.sonatype.com/) with a GitHub identity that administers the `quokkify` organization.
2. Add the `io.github.quokkify` namespace manually. Sonatype does **not** auto-provision organization namespaces: when Portal provides a verification key, create the required temporary public repository under `github.com/quokkify/<verification-key>`, complete verification, and then remove that repository.
3. Generate a Central Portal user token.
4. Create a GPG signing key and distribute its public key to a public keyserver.
5. Create a protected GitHub environment named `maven-central`.
6. Add these repository or environment secrets:
   - `MAVEN_CENTRAL_USERNAME`
   - `MAVEN_CENTRAL_PASSWORD`
   - `SIGNING_IN_MEMORY_KEY`
   - `SIGNING_IN_MEMORY_KEY_ID` (optional)
   - `SIGNING_IN_MEMORY_KEY_PASSWORD` (optional for an unencrypted key)

The Central username and password are the generated publishing token values, not the interactive login credentials. Store the complete ASCII-armored private key in `SIGNING_IN_MEMORY_KEY`.

## Release flow

`.github/workflows/publish-maven-central.yml` runs for a non-prerelease GitHub release and can also be started manually for an existing tag. It:

1. checks out the immutable release tag;
2. verifies that the tag and `version.txt` agree;
3. builds sources, Javadocs, POM metadata, and signatures;
4. uploads all publishable Q4J modules as one Maven Central deployment;
5. releases the validated deployment to the public repository.

A failed or interrupted publish must be inspected in Central Portal before retrying. Maven Central coordinates are immutable; never reuse a version after a successful publication.

## Local publication validation

The publication model and generated POMs can be inspected without Central credentials. Local builds use `<version.txt>-SNAPSHOT`; the release workflow is the only supported path that supplies a stable version:

```bash
./gradlew generatePomFileForMavenPublication --no-daemon --console=plain
```

A real Central upload additionally requires the five secrets listed above. Do not place credentials or signing material in `gradle.properties` inside the repository.
