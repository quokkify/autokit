package dev.quokkify.ex;

import com.codeborne.selenide.ex.UIAssertionError;

/**
 * Custom element not found exception.
 */
public class ElementNotFoundAssertionError extends UIAssertionError {

  public ElementNotFoundAssertionError(String message) {
    super(message);
  }
}
