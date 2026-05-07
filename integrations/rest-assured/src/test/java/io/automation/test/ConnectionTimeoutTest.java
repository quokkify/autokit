package io.automation.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import io.automation.annotation.SingleThread;
import io.automation.annotation.TestGroup;
import io.automation.service.SlowApiService;
import org.apache.http.NoHttpResponseException;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ConnectionTimeoutTest {

  private ServerSocket slowServer;
  private SlowApiService slowApiService;

  @BeforeClass
  public void startSlowServer() throws IOException {
    slowServer = new ServerSocket(0);
    int port = slowServer.getLocalPort();
    Thread serverThread = new Thread(() -> {
      while (!slowServer.isClosed()) {
        try {
          Socket socket = slowServer.accept();
          Thread.sleep(30_000);
          socket.close();
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
          break;
        } catch (IOException ignored) {
        }
      }
    });
    serverThread.setDaemon(true);
    serverThread.start();
    slowApiService = new SlowApiService("http://localhost:" + port, 1_000);
  }

  @AfterClass
  public void stopSlowServer() throws IOException {
    slowServer.close();
  }

  @SingleThread
  @TestGroup("API")
  @Test(description = "Verify connection is closed when MAX_RESPONSE_TIME_SECONDS is exceeded")
  public void testConnectionClosedOnTimeout() {
    long startMs = System.currentTimeMillis();
    Assertions.assertThatThrownBy(() -> slowApiService.getResource("/test"))
        .satisfiesAnyOf(
            e -> Assertions.assertThat(e).isInstanceOf(SocketTimeoutException.class),
            e -> Assertions.assertThat(e).isInstanceOf(NoHttpResponseException.class)
      );
    long elapsedMs = System.currentTimeMillis() - startMs;
    Assertions.assertThat(elapsedMs)
        .as("Connection should be closed within 5 seconds")
        .isLessThan(5_000L);
  }
}
