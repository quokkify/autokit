package io.automation.filter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom api log filter for Rest Assured.
 */
public abstract class ApiLogFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ApiLogFilter.class);

  protected static final int MAX_BODY_LENGTH_FOR_LOG_TO_CONSOLE = 5000;
  private static final boolean PRETTY_PRINT = true;
  private final Set<String> blacklistedHeaders = Collections.emptySet();

  protected Response processFilter(FilterableRequestSpecification requestSpec, Response response) {
    LOG.debug("REQUEST:");
    logRequest(requestSpec);
    final int responseBodyLength = response.getBody().asString().length();
    LOG.debug("RESPONSE:");
    logResponse(response, LogDetail.HEADERS);
    logResponse(response, LogDetail.COOKIES);
    logResponse(response, LogDetail.STATUS);
    if (responseBodyLength != 0 && responseBodyLength <= MAX_BODY_LENGTH_FOR_LOG_TO_CONSOLE) {
      logResponse(response, LogDetail.BODY);
    } else if (responseBodyLength == 0) {
      LOG.debug("Response body is empty");
    } else {
      LOG.debug("Response body length is longer then {} symbols.", MAX_BODY_LENGTH_FOR_LOG_TO_CONSOLE);
    }
    LOG.debug("REQUEST TIME: {} seconds", response.getTime() / 1000.0);
    return response;
  }

  private void logRequest(FilterableRequestSpecification requestSpec) {
    logEntity(fakePrintStream ->
        RequestPrinter.print(
            requestSpec,
            requestSpec.getMethod(),
            requestSpec.getURI(),
            LogDetail.ALL,
            blacklistedHeaders,
            fakePrintStream,
            PRETTY_PRINT)
    );
  }

  private void logResponse(Response response, LogDetail logDetail) {
    LOG.debug("{}:\n", StringUtils.capitalize(logDetail.name().toLowerCase()));
    logEntity(fakePrintStream ->
        ResponsePrinter.print(
            response,
            response,
            fakePrintStream,
            logDetail,
            PRETTY_PRINT,
            blacklistedHeaders)
    );
  }

  private void logEntity(Function<PrintStream, String> function) {
    ByteArrayOutputStream fakeOutputStream = new ByteArrayOutputStream();
    try (PrintStream fakePrintStream = new PrintStream(fakeOutputStream, false, StandardCharsets.UTF_8)) {
      LOG.debug(function.apply(fakePrintStream));
    }
  }
}
