package io.automation.provider;

import io.automation.config.ConfigRegistry;
import io.automation.config.ResourcesConfiguration;
import io.automation.constant.StringConstant;
import io.automation.util.FileUtils;

/**
 * Used for resources, that can be located in local resource folder.
 */
public interface LocalResource {

  ResourcesConfiguration CONFIG = ConfigRegistry.get(ResourcesConfiguration.class);

  /**
   * Fetch local resource path if local resource folder exist. Otherwise, return provided resource path.
   *
   * @param resourcePath resource path
   * @return local resource path as {@link String}
   */
  default String fetchLocalPath(String resourcePath) {
    String localResourcePath = String.join(StringConstant.SLASH, CONFIG.localResourcesFolder(), resourcePath);
    return FileUtils.isResourceExist(localResourcePath)
        ? localResourcePath
        : resourcePath;
  }
}
