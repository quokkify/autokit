package io.automation.elements.single;

import java.io.File;

import io.automation.elements.base.Component;

/**
 * Link UI element and methods of working with it.
 */
public class Link extends Component {

  public File uploadFile(File file) {
    return getSelf().uploadFile(file);
  }
}
