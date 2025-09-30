package io.automation.helper;

import io.automation.model.CatFactPojo;
import io.automation.model.ReqresUserPojo;
import io.automation.service.MockApiService;

import io.automation.util.JsonConverter;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;

public class MockApiHelper {

  private static final MockApiService MOCK_API_SERVICE = new MockApiService();

  private MockApiHelper() {
  }

  public static CatFactPojo getCatFact() {
    return JsonConverter.fromString(
        getCatFact(HttpStatus.SC_OK).extract().asString(),
        CatFactPojo.class);
  }

  private static ValidatableResponse getCatFact(int expectedStatusCode) {
    return getCatFactResponse().statusCode(expectedStatusCode);
  }

  public static ReqresUserPojo.Response createUser() {
    ReqresUserPojo.Request requestPojo = new ReqresUserPojo.Request(
        RandomStringUtils.randomAlphabetic(10),
        RandomStringUtils.randomAlphabetic(8)
    );
    ValidatableResponse validatableResponse = MOCK_API_SERVICE.createUser(requestPojo)
        .statusCode(HttpStatus.SC_CREATED);
    return JsonConverter.fromString(
        validatableResponse.extract().asString(),
        ReqresUserPojo.Response.class);
  }

  public static ValidatableResponse getCatFactResponse() {
    return MOCK_API_SERVICE.getFact();
  }
}
