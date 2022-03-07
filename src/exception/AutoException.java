package exception;

import java.util.*;
import model.Automobile;

/*
 * enum doesn't work with integers
enum ErrorCode {
	0, 1, 2;
}
*/

public class AutoException extends Exception {
	private static final long serialVersionUID = -500882683016787795L;
	Map<Integer, String> errorMessageTable;
	private int errorNumber;
	private String errorMessage;
	model.Automobile automobileObject;
	private Log log;

	/*
	 * build the error message table and create a new log. The log method will open
	 * up the log file and append the errors and warnings. This would be more efficient with
	 * static keyword.
	 */
	public void init() {
		errorNumber = 0;
		errorMessage = "";

		log = new Log();
		errorMessageTable = new LinkedHashMap<Integer, String>(128);
		errorMessageTable.put(0, "Unknown Problem");

		errorMessageTable.put(100, "Missing name attribute for option set in the automobile text file");
		errorMessageTable.put(101, "Missing options attribute for option set in the automobile text file");
		errorMessageTable.put(102, "Missing name attribute for option in the automobile text file");
		errorMessageTable.put(103, "Missing price attribute for option in the automobile text file");
		errorMessageTable.put(104, "option set name is reserved");
		errorMessageTable.put(105, "option set option price could not be parsed");

		errorMessageTable.put(200, "Could not find the automobile text file");
		errorMessageTable.put(201, "IO problem in automobile text file");
		errorMessageTable.put(202, "too many option sets for automobile");

		errorMessageTable.put(300, "IO problem serializing automobile");
		errorMessageTable.put(301, "IO problem serializing automobile directory");
		errorMessageTable.put(302, "Automobile being serialized is a null pointer");

		errorMessageTable.put(400, "IO problem deserializing automobile");
		errorMessageTable.put(401, "IO problem deserializing automobile");

		errorMessageTable.put(500, "Automobile added conficts with another automobile");
		errorMessageTable.put(501, "Automobile could not be added to database");
		errorMessageTable.put(502, "Automobile could not be found in database");
		errorMessageTable.put(503, "Automobile database not instantiated");

		errorMessageTable.put(600, "Automobile is a null pointer");
		errorMessageTable.put(601, "Option set is a null pointer");
		errorMessageTable.put(602, "Option is a null pointer");
		errorMessageTable.put(603, "Could not get option set");

		errorMessageTable.put(700, "Missing make field in the automobile text file");
		errorMessageTable.put(701, "Missing model field in the automobile text file");
		errorMessageTable.put(702, "Missing package field in the automobile text file");
		errorMessageTable.put(703, "Missing type field in the automobile text file");
		errorMessageTable.put(704, "Missing year field in the automobile text file");
		errorMessageTable.put(705, "Missing retail price field in the automobile text file");
		errorMessageTable.put(706, "Missing Color set in the automobile text file");
		errorMessageTable.put(707, "Missing Transmission set in the automobile text file");
		errorMessageTable.put(708, "Missing Brakes/Traction Control set in the automobile text file");
		errorMessageTable.put(709, "Missing Side Impact Air Bags set in the automobile text file");
		errorMessageTable.put(710, "Missing Power Moonroof set in the automobile text file");
		
		errorMessageTable.put(800, "Index out of bounds for option set option choice");
		errorMessageTable.put(801, "Null pointer exception for option set option choice");
		errorMessageTable.put(802, "Name empty for option set name");
		errorMessageTable.put(803, "Index out of bounds for option set option");
		
		errorMessageTable.put(900, "Could not find the automobile properties file");
		errorMessageTable.put(901, "Could not parse automobile properties file");
		errorMessageTable.put(902, "Could not close automobile properties file");
		
		errorMessageTable.put(1000, "Automobile directory could not be received");
		errorMessageTable.put(1001, "Could not initialize the car selection menu because of an IO exception");
		errorMessageTable.put(1002, "Could not initialize the car selection menu because of a general exception");
		errorMessageTable.put(1003, "Could not connect to the server");
		errorMessageTable.put(1004, "Could not get the IO streams from the socket");
		errorMessageTable.put(1005, "Automobile could not be received");
		errorMessageTable.put(1006, "Server message could not be received");
		errorMessageTable.put(1007, "Server message could not be decoded");
	}

	/* unknown exception */
	public AutoException() {
		super();
		init();
	}

	public AutoException(int errorNumber_) {
		super();
		init();
		this.errorNumber = errorNumber_;
		try {
			this.errorMessage = errorMessageTable.get(this.errorNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String printMessage = "#" + this.errorNumber + ": " + this.errorMessage;
		log.error(printMessage);
	}

	public AutoException(int errorNumber_, boolean warning) {
		init();
		this.errorNumber = errorNumber_;
		try {
			this.errorMessage = errorMessageTable.get(this.errorNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String printMessage = "#" + this.errorNumber + ": " + this.errorMessage;
		log.warning(printMessage);
	}

	/*
	 * use to pass the automobile object to the fix method without convoluting the
	 * argument list
	 */
	public void setAutomobile(model.Automobile automobileObject_) {
		automobileObject = automobileObject_;
	}
	
	public String getMessage() {
		return this.errorMessage;
	}

	/*
	 * This is my attempt at self-healing software
	 */
	public String fixString(int errorNumber) {
		String returnValue = null;
		switch (errorNumber) {
		case 500:
			FixAutomobileTable fixAutomobileTable = new FixAutomobileTable();
			returnValue = fixAutomobileTable.code500(automobileObject);
		}
		return returnValue;
	}
}
