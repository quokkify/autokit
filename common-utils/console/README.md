Manages SSH tunnels and executes remote shell commands over port-forwarded sessions in integration tests.

## Dependency

```gradle
testImplementation project(":common-utils:console")
```

## Environment variables

| Variable              | Description                              |
|-----------------------|------------------------------------------|
| `SSH_HOST_IP`         | Remote host IP address                   |
| `SSH_USER`            | SSH username                             |
| `SSH_PRIVATE_KEY_PATH`| Path to the private key file             |
| `SSH_PASSPHRASE`      | Private key passphrase (may be empty)    |
| `SSH_HOST_PORT`       | Remote SSH port (default 22)             |
| `SSH_LOCAL_PORT`      | Local forwarded port                     |
| `SSH_REMOTE_PORT`     | Remote service port to forward to        |

## Initialization in BaseTest

```java
private static Session session;
private static Shell   shell;

@BeforeClass
public static void openTunnel() throws Exception {
    SshPortForwardConfig config = new SshPortForwardConfig(
        System.getenv("SSH_HOST_IP"),
        "127.0.0.1",
        System.getenv("SSH_USER"),
        System.getenv("SSH_PRIVATE_KEY_PATH"),
        System.getenv("SSH_PASSPHRASE"),
        Integer.parseInt(System.getenv("SSH_HOST_PORT")),
        Integer.parseInt(System.getenv("SSH_LOCAL_PORT")),
        Integer.parseInt(System.getenv("SSH_REMOTE_PORT"))
    );
    session = SshUtils.createSession(config);
    SshUtils.setPortForwarding(session, config);
    shell = new Shell.Plain(new SSH(config.hostIp(), config.hostPort(),
                                   config.userName(), config.privateKeyPath()));
}

@AfterClass
public static void closeTunnel() {
    SshUtils.deletePortForwarding(session, Integer.parseInt(System.getenv("SSH_LOCAL_PORT")));
    SshUtils.closeSession(session);
}
```

## Usage in tests

```java
@Test
public void verifyServiceIsRunning() throws Exception {
    String status = SshUtils.executeCommand(shell, "systemctl status app-service");
    assertThat(status).contains("active (running)");
}

@Test
public void findErrorsInLog() throws Exception {
    String matches = SshUtils.getTextMatchesInFile(shell, "/var/log/app.log", "ERROR");
    assertThat(matches).isEmpty();
}

@Test
public void inspectRecentLogLines() throws Exception {
    String tail = SshUtils.getLastRowsFromFile(shell, "/var/log/app.log", 100);
    assertThat(tail).doesNotContain("FATAL");
}
```

## Key API

| Method                                               | Returns   | Notes                                  |
|------------------------------------------------------|-----------|----------------------------------------|
| `SshUtils.createSession(config)`                     | `Session` | Opens JSch session                     |
| `SshUtils.setPortForwarding(session, config)`        | `void`    | Binds local port to remote port        |
| `SshUtils.deletePortForwarding(session, port)`       | `void`    | Releases local port binding            |
| `SshUtils.closeSession(session)`                     | `void`    | Disconnects and frees resources        |
| `SshUtils.executeCommand(shell, command)`            | `String`  | Returns stdout of the command          |
| `SshUtils.getTextMatchesInFile(shell, path, text)`   | `String`  | grep result for `text` in remote file  |
| `SshUtils.getLastRowsFromFile(shell, path, n)`       | `String`  | tail -n result from remote file        |
| `SshUtils.executeRubyCommand(shell, command)`        | `String`  | Runs command via ruby interpreter      |
