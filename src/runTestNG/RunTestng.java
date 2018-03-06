package runTestNG;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class RunTestng {

	public static void runTestNG() {

		TestNG runner = new TestNG();
		List<String> suitefiles = new ArrayList<String>();
		suitefiles.add("testng.xml");
		runner.setTestSuites(suitefiles);
		runner.run();
	}
}
