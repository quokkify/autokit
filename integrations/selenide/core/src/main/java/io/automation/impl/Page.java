package io.automation.impl;

import io.automation.annotation.PageTitle;
import io.automation.annotation.PageUrl;
import io.automation.formatter.RegexFormatter;
import io.automation.parser.RegexParser;
import io.automation.service.Browser;

/**
 * Interface for Ui page class.
 */
public interface Page {

  /**
   * Get title of page.
   *
   * @param args dynamic parts in url
   * @return title as {@link String}
   */
  default String getTitle(Object... args) {
    return getTitle().formatted(args);
  }

  /**
   * Get title of page.
   *
   * @return title as {@link String}
   */
  default String getTitle() {
    return getClass().getAnnotation(PageTitle.class).value();
  }

  /**
   * Get url pattern with dynamic parts.
   *
   * @param args dynamic parts in url
   * @return pattern as {@link String}
   */
  default String getUrlPattern(Object... args) {
    return RegexFormatter.formatToAllMatchPatternWithPrefix(getUrl(args));
  }

  /**
   * Get url pattern.
   *
   * @return pattern as {@link String}
   */
  default String getUrlPattern() {
    return RegexFormatter.formatToAllMatchPatternWithPrefix(RegexFormatter.formatDigitsFormattedParts(getUrl()));
  }

  /**
   * Get url with dynamic parts.
   *
   * @param args dynamic parts in url
   * @return url with dynamic parts as {@link String}
   */
  default String getUrl(Object... args) {
    return getUrl().formatted(args);
  }

  /**
   * Get url.
   *
   * @return url as {@link String}
   */
  default String getUrl() {
    return getClass().getAnnotation(PageUrl.class).value();
  }

  /**
   * Get id from url.
   *
   * @return id from url as {@link String}
   */
  default String getIdFromUrl() {
    String pageUrl = Browser.getUrl();
    return RegexParser.parse("(?<=\\/)\\d+", pageUrl, "Can not find id value from url: %s".formatted(pageUrl));
  }
}
