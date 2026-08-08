package dev.quokkify.impl;

import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dev.quokkify.constant.StringConstant;

import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.ElementDescriber;
import com.codeborne.selenide.impl.ElementFinder;
import com.codeborne.selenide.impl.Plugins;
import com.codeborne.selenide.impl.WebElementSelector;
import com.codeborne.selenide.impl.WebElementSource;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Selenide page factory needs {@link WebElementSource} to initialize inner item elements.
 * The class was created by analogy with Selenide {@link ElementFinder}.
 */
public class CollectionSource extends WebElementSource {

  private final WebElementSelector elementSelector = Plugins.inject(WebElementSelector.class);
  private final ElementDescriber describer = Plugins.inject(ElementDescriber.class);
  private final Driver driver;
  private final WebElementSource parent;
  private final By criteria;
  private final Integer index;

  public CollectionSource(Driver driver, @Nullable WebElementSource parent, By criteria, int index) {
    this.driver = driver;
    this.parent = parent;
    this.criteria = criteria;
    this.index = index;
  }

  /**
   * Get web driver.
   *
   * @return driver as {@link Driver}
   */
  @Override
  public @NotNull Driver driver() {
    return driver;
  }

  /**
   * Find web element using elements selector {@link WebElementSelector}.
   *
   * @return web element as {@link WebElement}
   */
  @Override
  @CheckReturnValue
  @Nonnull
  public WebElement getWebElement() throws NoSuchElementException, IndexOutOfBoundsException {
    return elementSelector.findElement(driver, parent, criteria, index);
  }

  /**
   * Get element search criteria according to parent context.
   *
   * @return search criteria as {@link String}
   */
  @Override
  @CheckReturnValue
  @Nonnull
  public String getSearchCriteria() {
    return Objects.isNull(parent)
        ? elementCriteria()
        : String.join(StringConstant.SLASH, parent.getSearchCriteria(), elementCriteria());
  }

  /**
   * Get element search criteria without parent context.
   *
   * @return search criteria as {@link String}
   */
  @Nonnull
  private String elementCriteria() {
    return "%s[%d]".formatted(describer.selector(criteria), index);
  }
}
