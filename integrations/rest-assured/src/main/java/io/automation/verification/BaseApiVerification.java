package io.automation.verification;

import java.util.List;

import io.automation.helper.ResponseHelper;
import io.automation.model.JsonValidation;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;

/**
 * Base validation steps for Api tests.
 */
public abstract class BaseApiVerification<T extends BaseApiVerification<T>> implements ApiVerification {

  protected abstract T self();

  /**
   * Verify responses status code.
   *
   * @param responses  {@link ValidatableResponse}
   * @param statusCode expected status code as {@link Integer}
   * @return verification api steps chain
   */
  @Step("Verify responses statusrfr code")
  public T verifyResponseStatusCode(List<ValidatableResponse> responses, int statusCode) {
    SoftAssertions.assertSoftly(softly ->
        responses.forEach(response -> softly.assertThat(response.extract().statusCode())
            .as("Status code is incorrect")
            .isEqualTo(statusCode)));
    return self();
  }

  /**
   * Verify response status code.
   *
   * @param response           {@link ValidatableResponse}
   * @param expectedStatusCode expected status code as {@link Integer}
   * @return verification api steps chain
   */
  @Step("Verify response status code")
  public T verifyResponseStatusCode(ValidatableResponse response, int expectedStatusCode) {
    response.statusCode(expectedStatusCode);
    return self();
  }

  /**
   * Verify responses body.
   *
   * @param responses          {@link ValidatableResponse}
   * @param expectedBodyString expected body as {@link String}
   * @return verification api steps chain
   */
  @Step("Verify responses body")
  public T verifyResponseBody(List<ValidatableResponse> responses, String expectedBodyString) {
    SoftAssertions.assertSoftly(softly ->
        responses.forEach(response -> softly.assertThat(ResponseHelper.extractBodyAsString(response))
            .as("Body code is incorrect")
            .isEqualTo(expectedBodyString)));
    return self();
  }

  /**
   * Verify response body.
   *
   * @param response           {@link ValidatableResponse}
   * @param expectedBodyString expected body as {@link String}
   * @return verification api steps chain
   */
  @Step("Verify responses body")
  public T verifyResponseBody(ValidatableResponse response, String expectedBodyString) {
    response.assertThat().body(Matchers.is(expectedBodyString));
    return self();
  }

  /**
   * Verify response json schema.
   *
   * @param response   {@link ValidatableResponse}
   * @param jsonSchema expected json schema as {@link JsonValidation}
   * @return verification api steps chain
   */
  @Step("Verify response schema")
  public T verifyResponseSchema(ValidatableResponse response, JsonValidation jsonSchema) {
    ResponseHelper.validateSchema(response, jsonSchema);
    return self();
  }
}
