package driver;

import model.Automobile;
import adapter.*;
import scale.EditOptions;

public class SynchronousDriver {

	/** The purpose of this driver is to demonstrate Synchronous operations. Each
	 * buildAutoInterface.operationSynchronous begins a new thread, but will wait on
	 * other threads before proceeding. The order of the threads will be different
	 * since we cannot control when they run, but we know they won't run at the same
	 * time. Occasionally exceptions will be thrown because some operation will run
	 * before the other. This is entirely intentional for the purpose of
	 * demonstration. */
	public static void main(String[] args) {
		/* This test driver is similar to Hello.java */
		BuildAuto buildAutoInterface = new BuildAuto();
		buildAutoInterface.init(); // only call this once
		// Build Automobile Object from a file.
		String FordZTWAutomobileKey = buildAutoInterface.buildAuto("FordZTW.txt", "text");
		// String BMW320iAutomobileKey = buildAutoInterface.buildAuto("BMW320i.txt"); //
		// unused
		if (FordZTWAutomobileKey != null) {
			// Print attributes before serialization
			buildAutoInterface.printAuto(FordZTWAutomobileKey);
			/* We will start a bunch of operation threads to demonstrate multi-threading
			 * Some operations depend on the order and silently throw an exception that will
			 * be logged */
			String input[] = { FordZTWAutomobileKey, "Color", "French Blue Clearcoat Metallic",
				"Cool California Blue" };
			String input2[] = { FordZTWAutomobileKey, "Color", "French Blue Clearcoat Metallic",
				"Sunshine Hawaiian Gold" };
			String input3[] = { FordZTWAutomobileKey, "Color", "Colors" };
			String input4[] = { FordZTWAutomobileKey, "Color", "French Blue Clearcoat Metallic" };
			buildAutoInterface.operationSynchronous(0, input); // Updates option name
			buildAutoInterface.operationSynchronous(0, input2); // Updates option name
			buildAutoInterface.operationSynchronous(1, input3); // Updates option set name
			buildAutoInterface.operationSynchronous(3, input4); // set color choice
		} else {
			System.out.println("Could not build automobile");
		}
	}

}
