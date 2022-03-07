package adapter;

import model.*;
import scale.EditOptions;

import java.io.*;
import java.util.*;

import exception.*;

public abstract class ProxyAutomobile {
	private static model.AutomobileTable automobileTable;
	private static int threadNumber; // keep track of thread numbers
	private util.FileIO fileIOUtil;
	private util.StreamIO streamIOUtil;
	public boolean threadAvailable = true;

	protected ProxyAutomobile() {
		fileIOUtil = new util.FileIO();
		streamIOUtil = new util.StreamIO();
	}

	public void init() {
		// initialize the static automobile table
		automobileTable = new AutomobileTable(64);
		threadNumber = 0;
	}

	/* Assignment 2
	 * 5/04/2018
	 * UpdateAuto Implementation */
	/** Updates an automobile option set name
	 * @param automobileKey The automobile key
	 * @param optionSetName The option set name
	 * @param nameNew The new name
	 * @return true on success and false on failure */
	public boolean updateOptionSetName(String automobileKey, String optionSetName, String nameNew) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetName(optionSetName, nameNew);
			returnValue = true;
		}
		return returnValue;
	}

	public boolean updateOptionPrice(String automobileKey, String optionSetName, String optionName, double priceNew) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetOptionPrice(optionSetName, optionName, priceNew);
			returnValue = true;
		}
		return returnValue;
	}

	public boolean updateOptionName(String automobileKey, String optionSetName, String optionName,
		String optionNameNew) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetOptionName(optionSetName, optionName, optionNameNew);
			returnValue = true;
		}
		return returnValue;
	}

	/* Assignment 2
	 * 5/04/2018
	 * CreateAuto Implementation */
	/** builds the Automobile object from a configuration file
	 * @param fileName The file name
	 * @param fileType the file type
	 *            choices: text, property
	 * @return Automobile Key on success, and null on failure */
	public String buildAuto(String fileName, String fileType) {
		String automobileKey = null;
		model.Automobile automobileObject = new model.Automobile();
		if (fileType.equals("text")) {
			// fileType = text
			try {
				fileIOUtil.addToAutomobile(fileName, automobileObject);
				automobileKey = automobileTable.insertWrapper(automobileObject);
			} catch (exception.AutoException e) {
				// return already null
			}
		} else if (fileType.equals("property")) {
			// fileType = property
			try {
				streamIOUtil.propertiesToAutomobile(streamIOUtil.fileToProperties(fileName), automobileObject);
				automobileKey = automobileTable.insertWrapper(automobileObject);
			} catch (exception.AutoException e) {
				// return already null
			}
		}
		return automobileKey;
	}

	public boolean printAuto(String automobileKey) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			returnValue = true;
			System.out.println(automobileObject.toString());
		}
		return returnValue;
	}

	/**
	 * Serializes the automobile as a file
	 * @param automobileKey The automobile key
	 * @param fileName The file name
	 * @return true on success and false on failure
	 */
	public boolean serialize(String automobileKey, String fileName) {
		boolean returnValue = false;
		model.Automobile automobileObject;
		try {
			automobileObject = automobileTable.getByKey(automobileKey);
			fileIOUtil.serialize(fileName, automobileObject);
			returnValue = true;
		} catch (exception.AutoException e) {
			// nothing
		}
		if (returnValue) {
			System.out.println("Serialized data is saved in " + fileName);
		} else {
			System.out.println("Automobile could not be serialized");
		}
		return returnValue;
	}

	public String deserialize(String fileName) {
		String returnValue = null;
		model.Automobile automobileObject;
		try {
			automobileObject = fileIOUtil.deserialize(fileName);
			returnValue = automobileTable.insertWrapper(automobileObject);
			System.out.println("Deserialized data read from " + fileName);
		} catch (AutoException e) {
			System.out.println("Automobile could not be deserialized");
		}
		return returnValue;
	}

	/* Assignment 3
	 * 5/10/2018
	 * ChooseAuto Implementation */
	/**
	 * Choose an option in an option set
	 * @param automobileKey The automobile key
	 * @param optionSetName The option set name
	 * @param optionName The option name
	 * @return true on success and false on failure
	 */
	public boolean setOptionChoice(String automobileKey, String optionSetName, String optionName) {
		boolean returnValue = false;
		model.Automobile automobileObject;
		automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetChoice(optionSetName, optionName);
			returnValue = true;
		}
		return returnValue;
	}

	public String getOptionChoice(String automobileKey, String optionSetName) {
		String returnValue = null;
		model.Automobile automobileObject;
		automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			returnValue = automobileObject.getOptionSetChoiceName(optionSetName);
		}
		return returnValue;
	}

	public Double getOptionChoicePrice(String automobileKey, String optionSetName) {
		Double returnValue = null;
		model.Automobile automobileObject;
		automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			returnValue = automobileObject.getOptionSetChoicePrice(optionSetName);
		}
		return returnValue;
	}

	/* Assignment 4
	 * 5/29/2018
	 * scale.Scaleable Implementation */
	/**
	 * Starts an operation asynchronously
	 * @param operationNumber The operation to run
	 * @param inputArguments The arguments for the operation
	 */
	public void operation(int operationNumber, String[] inputArguments) {
		/* scale.EditOptions mimics Hello.java
		 * It contains a switching statement to delegate the operation number */
		EditOptions editObtionsObject = new scale.EditOptions(this, operationNumber, threadNumber++, true,
			inputArguments);
		editObtionsObject.start();
	}

	/**
	 * Starts an operation synchronously
	 * @param operationNumber The operation to run
	 * @param inputArguments The arguments for the operation
	 */
	public void operationSynchronous(int operationNumber, String[] inputArguments) {
		/* scale.EditOptions mimics Hello.java
		 * It contains a switching statement to delegate the operation number */
		EditOptions editObtionsObject = new scale.EditOptions(this, operationNumber, threadNumber++, false,
			inputArguments);
		editObtionsObject.start();
	}

	/* Assignment 5
	 * 6/12/2018
	 * server.AutoServer Implementation */
	/**
	 * Builds an automobile from a properties file
	 * @param automobileProperties The properties object
	 * @return The automobile key if successful, null on failure
	 * @throws exception.AutoException
	 */
	public String buildAutomobileFromProperties(Properties automobileProperties) throws exception.AutoException {
		String automobileKey = null;
		model.Automobile automobileObject = new model.Automobile();
		streamIOUtil.propertiesToAutomobile(automobileProperties, automobileObject);
		automobileKey = automobileTable.insertOverwrite(automobileObject);
		return automobileKey;
	}

	public Properties propertiesFromStream(InputStream socketStreamIn) throws exception.AutoException {
		return streamIOUtil.deserializeToStream(socketStreamIn);
	}

	public String automobileFromStream(InputStream socketStreamIn) throws exception.AutoException {
		return automobileTable.insertOverwrite(fileIOUtil.deserializeFromStream(socketStreamIn));
	}

	/**
	 * Serializes an automobile to a socket stream. Does not close the stream.
	 * @param socketStreamOut The socket stream.
	 * @param automobileKey The automobile key for the automobile to serialize
	 * @throws exception.AutoException
	 */
	public void automobileToStream(OutputStream socketStreamOut, String automobileKey) throws exception.AutoException {
		fileIOUtil.serializeToStream(socketStreamOut, automobileTable.getByKey(automobileKey));
	}

	public void directoryToStream(OutputStream socketStreamOut) throws exception.AutoException {
		fileIOUtil.directorySerializeToStream(socketStreamOut, automobileTable.toDirectory());
	}

	public String getAutomobileList() {
		StringBuffer listString = new StringBuffer();
		Iterator<Map.Entry<String, Automobile>> mapIterator = automobileTable.getIterator();
		while (mapIterator.hasNext()) {
			Map.Entry<String, Automobile> mapEntry = mapIterator.next();
			// assuming nothing is null (for performance)
			listString.append("Car ID: ").append(mapEntry.getKey()).append("\tName=")
				.append(mapEntry.getValue().getYear());
			listString.append(" ").append(mapEntry.getValue().getMake()).append(" ")
				.append(mapEntry.getValue().getModel());
			listString.append("\tRetail Price=$").append(mapEntry.getValue().getPrice()).append("\n");
		}
		return listString.toString();
	}

	public model.AutomobileTable.Directory getAutomobileDirectoryMap() {
		return automobileTable.toDirectory();
	}

	public Iterator<Map.Entry<String, Automobile>> getAutomobileIterator() {
		return automobileTable.getIterator();
	}
}