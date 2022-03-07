package exception;

import model.Automobile;

public class FixAutomobileTable {
	FixAutomobileTable() {

	}

	public String code500(model.Automobile automobileObject) {
		// add an integer to the end to prevent a conflict
		StringBuilder keyNew = new StringBuilder();
		keyNew.append(automobileObject.getMake()).append("-").append(automobileObject.getModel()).append("-")
			.append(automobileObject.getYear()).append("-1");
		return keyNew.toString();
	}
}
