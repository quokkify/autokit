package io.automation.test;

import io.automation.listener.lifecycle.SuiteListener;
import org.testng.annotations.Listeners;

@Listeners({SuiteListener.class})
abstract class BaseDatabaseTest {
}
