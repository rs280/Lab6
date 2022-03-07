package JSP;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

import exception.AutoException;

public class CarPrice implements client.SocketClientConstants {
	private model.Automobile automobileObject_;
	private StringBuffer errorMessageBuffer_;
	private boolean errorFlag_;

	public void processRequest(HttpServletRequest request) {
		// Start the client
		String strLocalHost = "";
		try {
			strLocalHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.err.println("Unable to find local host");
		}
		client.DefaultSocketClient socketClient = new client.DefaultSocketClient(strLocalHost, iDAYTIME_PORT);
		// Connect to server similar to assignment 5
		// DO NOT socketClient.start() OR A THREAD WILL START
		automobileObject_ = null;
		errorMessageBuffer_ = new StringBuffer();
		errorFlag_ = false;
		try {
			socketClient.openConnection();
		} catch (AutoException e) {
			errorMessageBuffer_.append("Could not connect to the server with connection information: <br>");
			errorMessageBuffer_.append("Host: ").append(strLocalHost).append("<br>");
			errorMessageBuffer_.append("Port: ").append(iDAYTIME_PORT).append("<br><br>");
			errorMessageBuffer_
				.append("If you are the server administrator, please make sure the server is running. <br>");
			errorMessageBuffer_.append("The server driver is located at: <br>");
			errorMessageBuffer_.append("/src/server/DefaultSocketServer<br><br>");
			errorMessageBuffer_.append("The internal error message:<br>");
			errorMessageBuffer_.append(e.getMessage());
			errorFlag_ = true;
		}
		// get automobile key
		String automobileKey = request.getParameter("automobileKey");
		if (automobileKey == null) {
			errorMessageBuffer_
				.append("Could not display automobile configuration because no automobile key was given.<BR>");
			errorMessageBuffer_.append("Please go back to the automobile selection and try again: <BR>");
			errorMessageBuffer_.append("<a href=\"/Lab6/servlet/servlets.CarSelection\">Automobile selection</a>");
			errorFlag_ = true;
		} else {
			// decodes url parameter data
			try {
				automobileKey = URLDecoder.decode(automobileKey, "ASCII");
			} catch (UnsupportedEncodingException e) {
				errorMessageBuffer_.append("Could not decode the URL parameters.<BR>");
				errorFlag_ = true;
			}
		}
		// get automobile
		if (!errorFlag_) {
			try {
				automobileObject_ = socketClient.getAutomobile(automobileKey);
			} catch (AutoException e) {
				errorMessageBuffer_
					.append("Could not display automobile configuration because of an internal server error: <BR>");
				errorMessageBuffer_.append(e.getMessage());
				errorMessageBuffer_.append("<br><br>automobileKey: ");
				errorMessageBuffer_.append(automobileKey);
				errorFlag_ = true;
			}
			socketClient.closeSession();
		}
		// set the choices
		if (!errorFlag_) {
			int i, n;
			n = automobileObject_.length();
			// option set
			for (i = 0; i < n; i++) {
				String optionSetName = automobileObject_.getOptionSetName(i);
				String optionSetOptionChoiceName = request.getParameter(optionSetName);
				if (optionSetOptionChoiceName != null) {
					automobileObject_.setOptionSetChoice(optionSetName, optionSetOptionChoiceName);
				}
			}
		}
	}

	public model.Automobile getAutomobile() {
		return automobileObject_;
	}

	public String getErrorMessage() {
		return errorMessageBuffer_.toString();
	}

	public boolean isError() {
		return errorFlag_;
	}

	public String escapeHtml(String stringInput) {
		if (stringInput == null) {
			stringInput = null;
		} else {
			// quick and dirty sanitizing
			stringInput = stringInput.replace("\"", "&quot;");
			stringInput = stringInput.replace("<", "&lt;");
			stringInput = stringInput.replace(">", "&gt;");
		}
		return stringInput;
	}
}
