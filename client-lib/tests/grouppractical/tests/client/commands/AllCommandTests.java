package grouppractical.tests.client.commands;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CommandParserCaseTest.class,
	RDistanceCommandTest.class, RRotateCommandTest.class, RSpeedCommandTest.class, RStopCommandTest.class, RStatusCommandTest.class,
	MListenerCommandTest.class, MPositionCommandTest.class, MInitializeCommandTest.class })
public class AllCommandTests {

}
