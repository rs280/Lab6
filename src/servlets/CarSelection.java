/*
 * TextUtils.htmlEncode was not used because the package is not standard.
 * This omission can cause issues with special characters. Avoid special characters and non-ascii.
 */

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import adapter.BuildAuto;
import exception.AutoException;
import model.Automobile;

/** The simplest possible servlet.
 *
 * @author James Duncan Davidson */

public class CarSelection extends HttpServlet implements client.SocketClientConstants {
	private static final long serialVersionUID = 4800503148201277286L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
		Iterator<Map.Entry<String, String>> directoryIterator = null;
		StringBuffer errorMessageBuffer = new StringBuffer();
		boolean errorFlag = false;
		try {
			socketClient.openConnection();
		} catch (AutoException e) {
			errorMessageBuffer.append("Could not connect to the server with connection information: <br>");
			errorMessageBuffer.append("Host: ").append(strLocalHost).append("<br>");
			errorMessageBuffer.append("Port: ").append(iDAYTIME_PORT).append("<br><br>");
			errorMessageBuffer
				.append("If you are the server administrator, please make sure the server is running. <br>");
			errorMessageBuffer.append("The server driver is located at: <br>");
			errorMessageBuffer.append("/src/server/DefaultSocketServer<br><br>");
			errorMessageBuffer.append("The internal error message:<br>");
			errorMessageBuffer.append(e.getMessage());
			errorFlag = true;
		}

		// get automobile directory
		if (!errorFlag) {
			try {
				directoryIterator = socketClient.getAutomobileDirectoryIterator();
			} catch (AutoException e) {
				errorMessageBuffer
					.append("Could not display automobile selection because of an internal server error: <BR>");
				errorMessageBuffer.append(e.getMessage());
				errorFlag = true;
			}
			socketClient.closeSession();
			if (!directoryIterator.hasNext()) {
				errorMessageBuffer.append("There are no autombiles configured on the server. <br>");
				errorMessageBuffer.append(
					"If you are the server administrator, please use the configuration client to configure cars. <br>");
				errorMessageBuffer.append("The client driver is located at: <br>");
				errorMessageBuffer.append("/src/client/DefaultSocketClient");
				errorFlag = true;
			}
		}
		// set response
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head>");

		String title = "Car Selection";
		String header = "Automobile Selection";

		out.println("<title>" + title + "</title>");

		// style start
		out.println("<style>");
		out.println("body {");
		out.println("background-color:#f1f1f1;");
		out.println("}");
		out.println("table {");
		out.println("border-collapse: collapse;");
		out.println("}");
		out.println("table, th, td {");
		out.println("border: 1px solid black;");
		out.println("}");
		out.println(".center_column {");
		out.println("background-color:#ffffff;");
		out.println("max-width:600px;");
		out.println("margin:auto;");
		out.println("padding: 15px");
		out.println("}");
		out.println("</style>");
		// style end

		out.println("</head>");
		out.println("<body>");
		out.println("<div class=\"center_column\">");
		out.println("<h1>" + header + "</h1>");

		if (errorFlag) {
			// error
			out.println("<p style=\"color: red\">" + errorMessageBuffer.toString() + "</p>");
		} else {
			// form start
			out.println("<form method=\"get\" action=\"/Lab6/servlet/servlets.CarConfiguration\">");
			out.println("<table style=\"border\">");
			out.println("<tr><td>Year/Make/Model</td>");
			out.println("<td>");
			out.println("<select name=\"automobileKey\">");
			while (directoryIterator.hasNext()) {
				Map.Entry<String, String> directoryEntry = directoryIterator.next();
				out.println("<option value=\"" + directoryEntry.getKey() + "\">");
				out.println(escapeHtml(directoryEntry.getValue()));
				out.println("</option>");
			}
			out.println("</select>");
			out.println("</td>");
			out.println("</tr>");
			out.println("<tr><td colspan=\"2\" style=\"text-align: right\">");
			out.println("<input type=\"submit\" value=\"Select\"></td>");
			out.println("</tr>");
			out.println("</table></form>");
			// form end
		}

		out.println("</div>"); // center_column
		out.println("</body>");
		out.println("</html>");
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
