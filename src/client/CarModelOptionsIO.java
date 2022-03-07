package client;

import java.io.*;
import java.net.*;

import adapter.BuildAuto;
import exception.AutoException;

public class CarModelOptionsIO {
	private util.StreamIO streamIOUtil;
	private util.FileIO fileIOUtil;
	private BufferedReader stdIn;
	private InputStream socketClientInputStream;
	private OutputStream socketClientOutputStream;
	private BufferedReader reader;
	private BufferedWriter writer;
	private DefaultSocketClient socketClient_;

	CarModelOptionsIO(DefaultSocketClient socketClient, BufferedReader stdIn_) {
		socketClient_ = socketClient;
		stdIn = stdIn_;
		fileIOUtil = new util.FileIO();
		streamIOUtil = new util.StreamIO();
	}

	public void displayMenu() {
		System.out.println("Options:");
		System.out.println("1: Upload Automobile Properties File");
		System.out.println("2: Upload Automobile Text File");
		System.out.println("3: Automobile list for configuration\n");
	}

	public void displayMenu1() {
		System.out.println("Enter the properties file path:");
		try {
			String inputString = stdIn.readLine();
			sendOutput("send properties");
			streamIOUtil.serializeToStream(socketClientOutputStream, streamIOUtil.fileToProperties(inputString));
		} catch (AutoException e) {
			System.out.println(e.getMessage());
			sendOutput("cancel properties");
		} catch (IOException e) {
			System.out.println("Error: Could not serialize");
			sendOutput("cancel properties");
		}
	}

	public void displayMenu2() {
		System.out.println("Enter the text file path:");
		try {
			String inputString = stdIn.readLine();
			sendOutput("send automobile");
			fileIOUtil.serializeToStream(socketClientOutputStream, fileIOUtil.fileToAutomobile(inputString));
		} catch (AutoException e) {
			System.out.println(e.getMessage());
			/* we send this command because the server was expecting an object
			 * stream
			 * This is obviously not an object stream and will allow the server
			 * to throw an exception */
			sendOutput("cancel properties");
		} catch (IOException e) {
			System.out.println("Error: Could not serialize");
			/* we send this command because the server was expecting an object
			 * stream
			 * This is obviously not an object stream and will allow the server
			 * to throw an exception */
			sendOutput("cancel properties");
		}
	}

	/** Allows the user to request an automobile list, enter the key, and
	 * configure the car
	 * This method is long, but breaking it up and doing more error checking is
	 * more of a summer project. */
	public void displayMenu3() {
		boolean errorFlag = false;
		System.out.println("Enter the key for the car you want to configure:");
		sendOutput("get automobile list");
		String fromServer = null;
		try {
			fromServer = receiveInput();
			System.out.println(fromServer);
		} catch (AutoException e) {
			System.out.println("Error: Could not read socket");
			sendOutput("cancel properties");
		}
		// get automobile key
		String automobileKey = "null";
		try {
			automobileKey = stdIn.readLine();
		} catch (IOException e) {
			System.out.println("Error: Could not read");
		}
		// get the automobile object
		model.Automobile automobileObject = null;
		try {
			automobileObject = socketClient_.getAutomobile(automobileKey);
		} catch (AutoException e) {
			System.out.println("Error: Could not get the automobile");
			sendOutput("cancel customization");
			errorFlag = true;
		}
		// begin option selection
		if (!errorFlag) {
			SelectCarOption selectCarOptions = new SelectCarOption(stdIn, automobileObject);
			selectCarOptions.beginSelection();
			sendOutput("pick up car");
		}
	}

	public boolean getMenuOption(String inputString) {
		boolean returnValue = true;
		switch (inputString) {
		case "1":
			displayMenu1();
			break;
		case "2":
			displayMenu2();
			break;
		case "3":
			displayMenu3();
			break;
		default:
			returnValue = false;
		}
		return returnValue;
	}

	public void openConnection(InputStream socketClientInputStream_, OutputStream socketClientOutputStream_)
		throws AutoException, Exception {
		socketClientInputStream = socketClientInputStream_;
		socketClientOutputStream = socketClientOutputStream_;
		reader = new BufferedReader(new InputStreamReader(socketClientInputStream));
		writer = new BufferedWriter(new OutputStreamWriter(socketClientOutputStream));
	}

	public void sendOutput(String strOutput) {
		try {
			// encode output to prevent transmittal errors
			strOutput = URLEncoder.encode(strOutput, "ASCII");
			writer.write(strOutput, 0, strOutput.length());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error writing");
		}
	}

	public String receiveInput() throws exception.AutoException {
		String strInput = null;
		try {
			strInput = reader.readLine();
		} catch (IOException e) {
			// Server message could not be received
			throw new exception.AutoException(1006);
		}
		// decode the input
		if (strInput != null) {
			try {
				strInput = URLDecoder.decode(strInput, "ASCII");
			} catch (UnsupportedEncodingException e) {
				// Server message could not be decoded
				throw new exception.AutoException(1007);
			}
		}
		return strInput;
	}
}
