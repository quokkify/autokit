package io.automation.model;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

import com.codeborne.selenide.BasicAuthCredentials;
import com.codeborne.selenide.Selenide;
import io.automation.annotation.PageUrl;
import io.automation.impl.Page;
import io.automation.util.UrlHelper;
import io.qameta.allure.Allure;

/**
 * Navigation class for opening pages according to PageObject class.
 */
public abstract class Navigation {

  private final String baseUrl;
  private BasicAuthCredentials basicAuthCredentials;

  protected Navigation(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  protected Navigation(String baseUrl, BasicAuthCredentials basicAuthCredentials) {
    this.baseUrl = baseUrl;
    this.basicAuthCredentials = basicAuthCredentials;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public Navigation setBasicAuthCredentials(BasicAuthCredentials basicAuthCredentials) {
    this.basicAuthCredentials = basicAuthCredentials;
    return this;
  }

  /**
   * Open page with url query parameters and formatted dynamic parts.
   *
   * <p>PageUrl: 'https://example.com/id/%s' -> 'https://example.comx/id/555?id=555'<p/>.
   *
   * @param pageClass   PageObject class
   * @param queryParams url query parameters
   * @param urlParams   dynamic url parameters
   * @return PageObject class
   */
  protected <T extends Page> T openPage(Class<T> pageClass, Map<String, Object> queryParams, Object... urlParams) {
    String fullPageUrl;
    try {
      fullPageUrl = UrlHelper.addQueryParameters(getPageUrl(pageClass, urlParams), queryParams);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return openPage(fullPageUrl, pageClass);
  }

  /**
   * Open page with formatted url dynamic parts.
   *
   * <p>PageUrl: 'https://example.com/id/%s' -> 'https://example.comx/id/555'<p/>.
   *
   * @param pageClass PageObject class
   * @param urlParams dynamic url parameters
   * @return PageObject class
   */
  protected <T extends Page> T openPage(Class<T> pageClass, Object... urlParams) {
    String fullPageUrl = getPageUrl(pageClass, urlParams);
    return openPage(fullPageUrl, pageClass);
  }

  /**
   * Open page with url query parameters.
   *
   * <p>PageUrl: 'https://example.com' -> 'https://example.com?id=555'<p/>.
   *
   * @param pageClass   PageObject class
   * @param queryParams url query parameters
   * @return PageObject class
   */
  protected <T extends Page> T openPage(Class<T> pageClass, Map<String, Object> queryParams) {
    String fullPageUrl;
    try {
      fullPageUrl = UrlHelper.addQueryParameters(getPageUrl(pageClass), queryParams);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return openPage(fullPageUrl, pageClass);
  }

  /**
   * Open page using full page url.
   *
   * @param fullPageUrl full page url as {@link String}
   * @return page class
   */
  protected void openPage(String fullPageUrl) {
    if (Objects.nonNull(basicAuthCredentials)) {
      try {
        Selenide.open(UrlHelper.getPageUrlWithCredentials(
            fullPageUrl,
            basicAuthCredentials.login,
            basicAuthCredentials.password)
        );
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    } else {
      Selenide.open(fullPageUrl);
    }
  }

  private <T extends Page> T openPage(String fullPageUrl, Class<T> pageClass) {
    String pageUrl;
    try {
      pageUrl = Objects.nonNull(basicAuthCredentials)
          ? UrlHelper.getPageUrlWithCredentials(
          fullPageUrl,
          basicAuthCredentials.login,
          basicAuthCredentials.password)
          : fullPageUrl;
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    Allure.step("Open page by url: '%s'".formatted(pageUrl));
    return Selenide.open(pageUrl, pageClass);
  }

  /**
   * Get page url with given url params.
   *
   * @param pageClass page class
   * @param urlParams url params
   * @param <T>       like {@link Page}
   * @return page url path with params
   */
  private <T extends Page> String getPageUrl(Class<T> pageClass, Object... urlParams) {
    String pagePath = pageClass.getAnnotation(PageUrl.class).value();
    return baseUrl.concat(pagePath.formatted(urlParams));
  }
}
