package io.automation.http;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.net.httpserver.HttpServer;
import io.automation.util.JsonConverter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HttpCoreClientTest {

  private HttpServer server;
  private int port;

  @BeforeClass
  public void startServer() throws Exception {
    server = HttpServer.create(new InetSocketAddress(0), 0);
    server.createContext("/api/echo", exchange -> {
      String method = exchange.getRequestMethod();
      String path = exchange.getRequestURI().getPath();
      String query = exchange.getRequestURI().getRawQuery();
      String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

      String response = "method=" + method + "\n"
          + "path=" + path + "\n"
          + "query=" + (query == null ? "" : query) + "\n"
          + "body=" + body;

      byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream out = exchange.getResponseBody()) {
        out.write(bytes);
      }
    });
    server.createContext("/api/json", exchange -> {
      String response = "{\"ok\":true,\"id\":123}";
      byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream out = exchange.getResponseBody()) {
        out.write(bytes);
      }
    });
    server.start();
    port = server.getAddress().getPort();
  }

  @AfterClass(alwaysRun = true)
  public void stopServer() {
    if (server != null) {
      server.stop(0);
    }
  }

  @Test
  public void getWithQueryParams() {
    HttpCoreClient client = HttpClientFactory.builder()
        .baseUri("http://localhost:" + port)
        .basePath("api")
        .requestTimeout(Duration.ofSeconds(2))
        .build();

    Map<String, Object> params = new LinkedHashMap<>();
    params.put("a", 1);
    params.put("b", "two");

    HttpResponseData response = client.get("echo", params);

    assertEquals(response.statusCode(), 200);
    assertTrue(response.body().contains("path=/api/echo"));
    assertTrue(response.body().contains("query="));
    assertTrue(response.body().contains("a=1"));
    assertTrue(response.body().contains("b=two"));
  }

  @Test
  public void postWithBody() {
    HttpCoreClient client = HttpClientFactory.builder()
        .baseUri("http://localhost:" + port)
        .basePath("api")
        .requestTimeout(Duration.ofSeconds(2))
        .build();

    String payload = "{\"hello\":\"world\"}";
    HttpResponseData response = client.post("echo", payload);

    assertEquals(response.statusCode(), 200);
    assertTrue(response.body().contains("method=POST"));
    assertTrue(response.body().contains("path=/api/echo"));
    assertTrue(response.body().contains("body=" + payload));
  }

  @Test
  public void jsonDeserialization() {
    HttpCoreClient client = HttpClientFactory.builder()
        .baseUri("http://localhost:" + port)
        .basePath("api")
        .requestTimeout(Duration.ofSeconds(2))
        .build();

    HttpResponseData response = client.get("json");
    Map<String, Object> payload = JsonConverter.fromString(response.body(), new TypeReference<>() {
    });

    assertEquals(payload.get("ok"), Boolean.TRUE);
    assertEquals(payload.get("id"), 123);
  }
}
