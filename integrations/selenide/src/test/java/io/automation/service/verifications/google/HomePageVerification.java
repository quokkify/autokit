package io.automation.service.verifications.google;

import io.automation.model.Verification;
import io.automation.page.google.HomePage;
import io.automation.service.steps.google.HomePageSteps;

public class HomePageVerification extends Verification<HomePageSteps, HomePageVerification, HomePage> {

  public HomePageVerification(HomePageSteps steps, HomePage page) {
    super(steps, page);
  }
}
