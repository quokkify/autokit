package io.automation.service;

import io.automation.config.app.InnerTestsConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class SetupApiService extends ApiService {

  protected RequestSpecification setup() {
    return RestAssured.given().spec(getRequestSpecification(InnerTestsConfig.BASE_API_URL, ContentType.JSON));
  }
}
