package dev.quokkify.reportportal.listeners;

import com.epam.reportportal.testng.BaseTestNGListener;
import com.epam.reportportal.testng.TestNGService;
import dev.quokkify.reportportal.services.ParamOverrideTestNgService;
import org.testng.ITestListener;

public class ReportPortalListener extends BaseTestNGListener implements ITestListener {

  public ReportPortalListener() {
    super(new ParamOverrideTestNgService());
  }

  public ReportPortalListener(TestNGService service) {
    super(service);
  }
}
