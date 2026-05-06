package io.automation.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class SlowApiService extends ApiService {

  private final String baseUrl;

  public SlowApiService(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public ValidatableResponse getResource(String path) {
    return get(RestAssured.given().spec(getRequestSpecification(baseUrl, ContentType.JSON)), path);
  }
}
