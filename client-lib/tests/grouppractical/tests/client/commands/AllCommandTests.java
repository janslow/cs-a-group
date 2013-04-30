package grouppractical.tests.client.commands;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CommandParserCaseTest.class, ConnectCommandTest.class,
	RDistanceCommandTest.class, RRotateCommandTest.class, RStopCommandTest.class,
	RStatusCommandTest.class, RLockCommandTest.class, RUnlockCommandTest.class,
	MPositionCommandTest.class, MInitializeCommandTest.class })
public class AllCommandTests {

}
