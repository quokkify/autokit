# common-utils/console

Execute shell commands and manage SSH tunnels over remote hosts in integration tests.

## Dependency

```gradle
testImplementation project(":common-utils:console")
```

## Usage

Open a port-forwarded SSH session and run a remote command:

```java
Session session = SshUtils.createSession(sshPortForwardConfig);
SshUtils.setPortForwarding(session, sshPortForwardConfig);

String output = SshUtils.executeCommand(shell, "systemctl status app");
```

Inspect remote log files without copying them locally:

```java
String matches = SshUtils.getTextMatchesInFile(shell, "/var/log/app.log", "ERROR");
String tail    = SshUtils.getLastRowsFromFile(shell, "/var/log/app.log", 50);
```
