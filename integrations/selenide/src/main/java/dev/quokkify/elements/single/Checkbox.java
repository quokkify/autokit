package dev.quokkify.elements.single;

import dev.quokkify.elements.base.Component;

import com.codeborne.selenide.Condition;

/**
 * Checkbox UI element and methods of working with it.
 */
public class Checkbox extends Component {

  /**
   * Checks that checkbox is unchecked.
   *
   * @return boolean
   */
  public boolean isUnchecked() {
    return !isChecked();
  }

  /**
   * Checks that checkbox is checked.
   *
   * @return boolean
   */
  public boolean isChecked() {
    return getSelf().is(Condition.checked);
  }

  /**
   * Set checkbox state to checked.
   */
  public void check() {
    checkOrUncheck(true);
  }

  /**
   * Set checkbox state to uncheck.
   */
  public void uncheck() {
    checkOrUncheck(false);
  }

  /**
   * Set checkbox state to checked or uncheck.
   */
  private void checkOrUncheck(boolean isChecked) {
    getSelf().setSelected(isChecked);
  }
}
