package dev.quokkify.elements.base;

import java.time.Duration;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.SelenidePageFactory;
import com.codeborne.selenide.impl.WebElementSource;
import dev.quokkify.constant.PollingInterval;
import dev.quokkify.constant.StringConstant;
import dev.quokkify.ex.ElementNotFoundAssertionError;
import dev.quokkify.factory.ElementDecorateProperties;
import dev.quokkify.impl.CollectionSource;
import dev.quokkify.impl.DefaultCollectionSource;
import dev.quokkify.impl.ElementsSource;
import dev.quokkify.impl.FilteredElementsSource;
import dev.quokkify.util.Waiter;
import org.openqa.selenium.WebElement;

/**
 * Class for working with UI elements collection.
 */
public class ComponentsCollection<T extends Component> extends AbstractList<T> {

  private static final Integer FIRST_ELEMENT_INDEX = 0;
  private final ElementsSource<T> elementsSource;
  private final SelenidePageFactory selenidePageFactory;
  private final ElementDecorateProperties<T> elementDecorateProperties;

  public ComponentsCollection(SelenidePageFactory selenidePageFactory,
                              ElementDecorateProperties<T> elementDecorateProperties) {
    this(selenidePageFactory, elementDecorateProperties, new DefaultCollectionSource<>(elementDecorateProperties));
  }

  private ComponentsCollection(SelenidePageFactory selenidePageFactory,
                               ElementDecorateProperties<T> elementDecorateProperties,
                               ElementsSource<T> elementsSource) {
    this.selenidePageFactory = selenidePageFactory;
    this.elementDecorateProperties = elementDecorateProperties;
    this.elementsSource = elementsSource;
  }

  /**
   * Get size of elements list.
   *
   * @return size of elements {@link Integer}
   */
  @Override
  public int size() {
    return getElements().size();
  }

  /**
   * Find UI element index by provided predicate.
   *
   * @param predicate predicate for searching element
   * @return UI element index by specific predicate as {@link Integer}
   */
  public int getIndex(Predicate<T> predicate) {
    return getIteratedElement(predicate, StringConstant.NONE.lowerCase()).elementIndex;
  }

  /**
   * Get UI element by index.
   *
   * @param index index of needed element
   * @return UI element as {@link T}
   */
  @Override
  public T get(int index) {
    T element = getElements().get(index);
    initInnerElements(element, index);
    return element;
  }

  /**
   * Find UI element by provided predicate.
   *
   * @param predicate predicate for searching element
   * @return UI element by specific predicate as {@link T}
   */
  public T get(Predicate<T> predicate) {
    return get(predicate, StringConstant.NONE.lowerCase());
  }

  /**
   * Wait until collection is not empty and find UI element by provided predicate.
   *
   * @param predicate   predicate for searching element
   * @param description predicate description
   * @return UI element by specific predicate as {@link T}
   */
  public T get(Predicate<T> predicate, String description) {
    shouldBe(CollectionCondition.sizeGreaterThan(0));
    return getElement(predicate, description);
  }

  /**
   * Find UI element by provided predicate.
   *
   * @param predicate   predicate for searching element
   * @param description predicate description
   * @return UI element by specific predicate as {@link T}
   */
  private T getElement(Predicate<T> predicate, String description) {
    return getIteratedElement(predicate, description).element;
  }

  /**
   * Find UI iterated element by provided predicate.
   *
   * @param predicate   predicate for searching element
   * @param description predicate description
   * @return UI iterated element by specific predicate as {@link IteratedElement}
   */
  private IteratedElement getIteratedElement(Predicate<T> predicate, String description) {
    List<T> elements = getElements();
    for (int index = FIRST_ELEMENT_INDEX; index < elements.size(); index++) {
      T element = elements.get(index);
      initInnerElements(element, index);
      if (predicate.test(element)) {
        return new IteratedElement(index, element);
      }
    }
    throw
        new ElementNotFoundAssertionError("No elements found by predicate. Additional info: %s".formatted(description));
  }

  /**
   * Filter UI elements by provided predicate.
   *
   * @param predicate predicate for filtering elements
   * @return UI elements collection with filtered elements as {@link ComponentsCollection}&lt;{@link T}&gt;
   */
  public ComponentsCollection<T> filter(Predicate<T> predicate) {
    List<T> elements = getElements();
    IntStream.range(FIRST_ELEMENT_INDEX, elements.size())
        .forEach(index -> initInnerElements(elements.get(index), index));
    ElementsSource<T> filteredComponentsStore = new FilteredElementsSource<>(this.elementsSource, predicate);
    return new ComponentsCollection<>(this.selenidePageFactory, this.elementDecorateProperties, filteredComponentsStore);
  }

  /**
   * Get UI elements collection status by condition.
   *
   * @param condition condition for validating status
   * @return collection status as {@link Boolean}
   */
  public boolean is(WebElementsCondition condition) {
    return condition.check(WebDriverRunner.driver(), getWebElements()).verdict() == CheckResult.Verdict.ACCEPT;
  }

  /**
   * Get UI elements collection contains status of element.
   *
   * @param predicate condition for getting status
   * @return contains status as {@link Boolean}
   */
  public boolean hasItem(Predicate<T> predicate) {
    shouldBe(CollectionCondition.sizeGreaterThan(0));
    return this.stream().anyMatch(predicate);
  }

  /**
   * Check UI elements by provided conditions.
   *
   * @param conditions conditions for checking UI elements
   * @return UI elements collection with checked elements {@link ComponentsCollection}&lt;{@link T}&gt;
   */
  public ComponentsCollection<T> shouldBe(WebElementsCondition... conditions) {
    return should(Duration.ofMillis(WebDriverRunner.driver().config().timeout()), conditions);
  }

  /**
   * Check UI elements by provided conditions.
   *
   * @param duration   timeout for assertion
   * @param conditions conditions for checking UI elements
   * @return UI elements collection with checked elements {@link ComponentsCollection}&lt;{@link T}&gt;
   */
  public ComponentsCollection<T> shouldBe(Duration duration, WebElementsCondition... conditions) {
    return should(duration, conditions);
  }

  /**
   * Check UI elements by provided conditions.
   *
   * @param conditions conditions for checking UI elements
   * @return UI elements collection with checked elements {@link ComponentsCollection}&lt;{@link T}&gt;
   */
  public ComponentsCollection<T> shouldHave(WebElementsCondition... conditions) {
    return should(Duration.ofMillis(WebDriverRunner.driver().config().timeout()), conditions);
  }

  /**
   * Check UI elements by provided conditions.
   *
   * @param duration   timeout for assertion
   * @param conditions conditions for checking UI elements
   * @return UI elements collection with checked elements {@link ComponentsCollection}&lt;{@link T}&gt;
   */
  public ComponentsCollection<T> shouldHave(Duration duration, WebElementsCondition... conditions) {
    return should(duration, conditions);
  }

  /**
   * Get UI elements texts.
   *
   * @return elements texts as {@link List}&lt;{@link String}&gt;
   */
  public List<String> getTexts() {
    return getElements().stream().map(element -> element.getSelf().getText())
        .collect(Collectors.toList());
  }

  /**
   * Get first UI element.
   *
   * @return first UI element as {@link T}
   */
  public T first() {
    return get(FIRST_ELEMENT_INDEX);
  }

  /**
   * Get last UI element.
   *
   * @return last UI element as {@link T}
   */
  public T last() {
    return get(size() - 1);
  }

  /**
   * Check UI elements by provided conditions.
   *
   * @param duration   timeout for assertion
   * @param conditions checked conditions
   * @return UI elements collection with checked elements as {@link ComponentsCollection}&lt;{@link T}&gt;
   */
  private ComponentsCollection<T> should(Duration duration, WebElementsCondition... conditions) {
    try {
      Arrays.stream(conditions).forEach(condition -> should(duration, condition));
      return this;
    } catch (Error error) {
      throw UIAssertionError.wrap(WebDriverRunner.driver(), error, duration.toMillis());
    }
  }

  /**
   * Check UI elements by provided conditions.
   *
   * @param duration  timeout for assertion
   * @param condition checked condition
   */
  private void should(Duration duration, WebElementsCondition condition) {
    Waiter.awaitCondition(() -> condition
            .check(WebDriverRunner.driver(), getWebElements())
            .verdict() == CheckResult.Verdict.ACCEPT,
        condition.toString(),
        (int) duration.toSeconds(),
        (int) PollingInterval.MILLIS_1000.duration().toMillis());
  }

  /**
   * Get UI elements list.
   *
   * @return list of UI elements {@link List}&lt;{@link WebElement}&gt;
   */
  private List<WebElement> getWebElements() {
    return getElements().stream().map(block -> block.getSelf().toWebElement())
        .collect(Collectors.toList());
  }

  /**
   * Get UI elements list.
   *
   * @return list of UI elements as {@link List}&lt;{@link T}&gt;
   */
  private List<T> getElements() {
    return this.elementsSource.getElements();
  }

  /**
   * Initialize all inner collection item elements.
   *
   * @param element      element of UI elements list collection
   * @param elementIndex index of UI element in collection
   */
  private void initInnerElements(T element, int elementIndex) {
    Driver driver = WebDriverRunner.driver();
    WebElementSource self = new CollectionSource(
        driver,
        elementDecorateProperties.searchContext(),
        elementDecorateProperties.selector(),
        elementIndex
    );
    selenidePageFactory.initElements(driver, self, element, elementDecorateProperties.genericTypes());
  }

  private class IteratedElement {

    private final Integer elementIndex;
    private final T element;

    IteratedElement(Integer elementIndex, T element) {
      this.elementIndex = elementIndex;
      this.element = element;
    }
  }
}
