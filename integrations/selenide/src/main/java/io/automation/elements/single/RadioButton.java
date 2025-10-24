package io.automation.elements.single;

import io.automation.elements.base.Component;

/**
 * RadioButton UI element and methods of working with it.
 */
public class RadioButton extends Component {

  /**
   * Select radio button.
   *
   * @param value – value of radio button to select
   */
  public void selectRadio(String value) {
    getSelf().selectRadio(value);
  }
}
