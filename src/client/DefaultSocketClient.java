package client;

import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.net.URLEncoder;
import java.net.URLDecoder;

import adapter.BuildAuto;
import exception.AutoException;
import model.Automobile;

import java.io.*;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants {
	private BufferedReader stdIn_;
	private Socket socketClient;
	private InputStream socketClientInputStream;
	private OutputStream socketClientOutputStream;
	private BufferedReader reader;
	private BufferedWriter writer;
	private util.StreamIO streamIOUtil;
	private util.FileIO fileIOUtil;
	private String strHost;
	private int iPort;
	private CarModelOptionsIO carOptionsMenu;

	/** Constructor
	 * @param strHost The connection host address
	 * @param iPort The connection port */
	public DefaultSocketClient(String strHost, int iPort) {
		setPort(iPort);
		setHost(strHost);
		fileIOUtil = new util.FileIO();
		streamIOUtil = new util.StreamIO();
	}

	/** The threaded client */
	public void run() {
		try {
			openConnection();
			handleSession();
			closeSession();
		} catch (AutoException e) {
			if (DEBUG) {
				System.out.println("Host: " + strHost);
				System.out.println("Port: " + iPort);
				System.out.println(e.getMessage());
			}
		}
	}

	public void setStandardIn(BufferedReader stdIn) {
		stdIn_ = stdIn;
	}

	public void openConnection() throws AutoException {
		try {
			socketClient = new Socket(strHost, iPort);
		} catch (IOException socketError) {
			// Could not connect to the server
			throw new exception.AutoException(1003);
		}
		try {
			socketClientInputStream = socketClient.getInputStream();
			socketClientOutputStream = socketClient.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(socketClientInputStream));
			writer = new BufferedWriter(new OutputStreamWriter(socketClientOutputStream));
		} catch (Exception e) {
			// Could not get the IO streams from the socket
			throw new exception.AutoException(1004);
		}
	}

	public void initCarOptionsMenu() throws AutoException {
		try {
			carOptionsMenu = new CarModelOptionsIO(this, stdIn_);
			carOptionsMenu.openConnection(socketClientInputStream, socketClientOutputStream);
		} catch (IOException e) {
			// Could not initialize the car selection menu because of an IO exception
			throw new exception.AutoException(1001);
		} catch (Exception e) {
			// Could not initialize the car selection menu because of a general exception
			throw new exception.AutoException(1002);
		}
	}

	public void handleSession() {
		try {
			initCarOptionsMenu();
		} catch (AutoException e) {
			if (DEBUG)
				System.out.println(e.getMessage());
		}

		String strInput = "";
		String fromServer = "";
		if (DEBUG)
			System.out.println("Handling session with " + strHost + ":" + iPort);
		try {
			// block on client command
			carOptionsMenu.displayMenu();
			while ((strInput = stdIn_.readLine()) != null) {
				if (carOptionsMenu.getMenuOption(strInput)) {
					// block on server response
					fromServer = receiveInput();
					handleInput(fromServer);
				}
				carOptionsMenu.displayMenu();
			}
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("client unexpectedly closed");
		} catch (AutoException e) {
			if (DEBUG)
				System.out.println(e.getMessage());
		}
	}

	public void sendOutput(String strOutput) {
		try {
			// encode output to prevent transmittal errors
			strOutput = URLEncoder.encode(strOutput, "ASCII");
			writer.write(strOutput, 0, strOutput.length());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("Error writing to " + strHost);
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

	public Iterator<Map.Entry<String, String>> getAutomobileDirectoryIterator() throws exception.AutoException {
		String strOutput = "get automobile directory";
		String fromServer;
		Iterator<Map.Entry<String, String>> mapIterator = null;
		try {
			sendOutput(strOutput);
			// read the status message
			fromServer = receiveInput();
			if (fromServer.equals("failed")) {
				// Automobile directory could not be received
				throw new exception.AutoException(1000);
			} else {
				model.AutomobileTable.Directory automobileDirectory = fileIOUtil
					.directoryDeserializeFromStream(socketClientInputStream);
				mapIterator = automobileDirectory.map.entrySet().iterator();
			}
		} catch (AutoException e) {
			// Automobile directory could not be received
			throw new exception.AutoException(1000);
		}
		return mapIterator;
	}

	public model.Automobile getAutomobile(String automobileKey) throws exception.AutoException {
		String strOutput = "begin customization";
		String fromServer;
		model.Automobile automobileObject = null;
		try {
			sendOutput(strOutput);
			sendOutput(automobileKey);
			// read the status message
			fromServer = receiveInput();
			if (fromServer.equals("failed")) {
				// Automobile could not be received
				throw new exception.AutoException(1005);
			} else {
				automobileObject = fileIOUtil.deserializeFromStream(socketClientInputStream);
			}
		} catch (AutoException e) {
			// Automobile could not be received
			throw new exception.AutoException(1005);
		}
		return automobileObject;
	}

	public void handleInput(String strInput) {
		// unescape new lines
		strInput = strInput.replace("\\n", "\n");
		System.out.println(strInput);
	}

	public void closeSession() {
		try {
			writer = null;
			reader = null;
			socketClient.close();
		} catch (IOException e) {
			if (DEBUG)
				System.err.println("Error closing socket to " + strHost);
		}
	}

	public void setHost(String strHost) {
		this.strHost = strHost;
	}

	public void setPort(int iPort) {
		this.iPort = iPort;
	}

	public static void main(String arg[]) {
		/* debug main; does daytime on local host */
		String strLocalHost = "";
		try {
			strLocalHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.err.println("Unable to find local host");
		}
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		DefaultSocketClient socketClient = new DefaultSocketClient(strLocalHost, iDAYTIME_PORT);
		socketClient.setStandardIn(stdIn);
		socketClient.start();
	}

}// class DefaultSocketClient
