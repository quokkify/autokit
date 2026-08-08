package dev.quokkify.elements.single;

import java.io.File;

import dev.quokkify.elements.base.Component;

/**
 * Button UI element and methods of working with it.
 */
public class Button extends Component {

  /**
   * Click button to download file.
   *
   * @return File
   */
  public File download() {
    return getSelf().download();
  }

  public File uploadFile(File file) {
    return getSelf().uploadFile(file);
  }
}
