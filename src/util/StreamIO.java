package util;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import model.Automobile;
import exception.AutoException;

public class StreamIO {
	/** properties file to automobile properties object
	 * @param fileName Properties file name
	 * @return Automobile properties object
	 * @throws AutoException */
	public Properties fileToProperties(String fileName) throws AutoException {
		Properties automobileProperties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(fileName);
			// load a properties file
			automobileProperties.load(input);
		} catch (FileNotFoundException e) {
			throw new exception.AutoException(900);
		} catch (IOException e) {
			throw new exception.AutoException(901);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new exception.AutoException(902);
				}
			}
		}
		return automobileProperties;
	}

	/** sets optionSet in automobile object from a automobile properties object
	 * @param automobileProperties The automobile properties
	 * @param automobileObject The automobile object
	 * @throws AutoException */
	public void propertiesToAutomobile(Properties automobileProperties, Automobile automobileObject)
		throws AutoException {
		int optionSetObjectIndex = -1;
		Enumeration<?> enumerator = automobileProperties.propertyNames();
		while (enumerator.hasMoreElements()) {
			String optionSetName = (String) enumerator.nextElement();
			String optionSetOptions = automobileProperties.getProperty(optionSetName);
			// add an option set and process the options string
			optionSetObjectIndex = automobileObject.addOptionSet(optionSetName);
			if (optionSetObjectIndex == -1) {
				// reserved
				automobileObject.setOptionSetOptionNameReserved(optionSetName, optionSetOptions);
			} else {
				optionSetOptionsProcess(automobileObject, optionSetObjectIndex, optionSetOptions);
			}
		}
		if (automobileObject == null) {
			throw new exception.AutoException(901);
		}
	}

	/** Parse an option string into the Automobile OptionSet index
	 * @param automobileObject The automobile object
	 * @param optionSetObjectIndex The OptionSet index
	 * @param optionSetOptions The option string
	 * @throws AutoException */
	private void optionSetOptionsProcess(Automobile automobileObject, int optionSetObjectIndex, String optionSetOptions)
		throws AutoException {
		if (optionSetOptions.equals("")) {
			throw new exception.AutoException(101);
		}
		String optionName, optionPrice;
		String[] optionParts;
		// optionSet options
		if (optionSetOptions.indexOf(',') != -1 && optionSetObjectIndex != -1) {
			optionParts = optionSetOptions.split(",");
		} else {
			optionParts = new String[] { optionSetOptions };
		}
		for (String optionPart : optionParts) {
			// part not blank
			if (optionPart.trim().length() > 0) {
				if (optionPart.indexOf('/') != -1) {
					String[] optionValueParts = optionPart.split("/");
					optionName = optionValueParts[0].trim();
					optionPrice = optionValueParts[1].trim();
					if (optionName.equals("")) {
						new exception.AutoException(102, true); // warning
					}
					if (optionPrice.equals("")) {
						new exception.AutoException(103, true); // warning
					}
					try {
						automobileObject.addOptionSetOption(optionSetObjectIndex, optionName,
							Double.parseDouble(optionPrice));
					} catch (NumberFormatException e) {
						throw new exception.AutoException(105, true);
					}
				} else {
					if (optionSetObjectIndex >= 0) {
						automobileObject.addOptionSetOption(optionSetObjectIndex, optionPart.trim(), 0);
					} else {
						new exception.AutoException(104, true); // warning
					}
				}
			} else {
				new exception.AutoException(102, true); // warning
			}
		}
	}

	/** serializes and Automobile object object as a file
	 * @param socketStreamOut The output stream
	 * @param automobileProperties The Automobile properties
	 * @throws AutoException */
	public void serializeToStream(OutputStream socketStreamOut, Properties automobileProperties) throws AutoException {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socketStreamOut);
			out.writeObject(automobileProperties);
			// do not close stream. let caller do this.
			// out.close();
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		}
	}

	/** deserializes a file as an Automobile object
	 * @param socketStreamIn The input stream
	 * @return The Automobile properties
	 * @throws AutoException */
	public Properties deserializeToStream(InputStream socketStreamIn) throws AutoException {
		Properties automobileProperties = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socketStreamIn);
			automobileProperties = (Properties) in.readObject();
			// do not close stream. let caller do this.
			// in.close();
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		} catch (ClassNotFoundException e) {
			throw new exception.AutoException(300);
		}
		return automobileProperties;
	}

	/* print() and toString() */
	public void print() {
		System.out.print(toString());
	}

	public String toString() {
		return "StreamIO Utility";
	}
}
