/*
 * TextUtils.htmlEncode was not used because the package is not standard.
 * This omission can cause issues with special characters. Avoid special characters and non-ascii.
 */

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.net.URLDecoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import exception.AutoException;

/** The simplest possible servlet.
 *
 * @author James Duncan Davidson */

public class CarConfiguration extends HttpServlet implements client.SocketClientConstants {
	private static final long serialVersionUID = 3770853422742103849L;

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
		model.Automobile automobileObject = null;
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
		// get automobile key
		String automobileKey = request.getParameter("automobileKey");
		if (automobileKey == null) {
			errorMessageBuffer
				.append("Could not display automobile configuration because no automobile key was given.<BR>");
			errorMessageBuffer.append("Please go back to the automobile selection and try again: <BR>");
			errorMessageBuffer.append("<a href=\"/Lab6/servlet/servlets.CarSelection\">Automobile selection</a>");
			errorFlag = true;
		} else {
			// decodes url parameter data
			try {
				automobileKey = URLDecoder.decode(automobileKey, "ASCII");
			} catch (UnsupportedEncodingException e) {
				errorMessageBuffer.append("Could not decode the URL parameters.<BR>");
				errorFlag = true;
			}
		}
		// get automobile
		if (!errorFlag) {
			try {
				automobileObject = socketClient.getAutomobile(automobileKey);
			} catch (AutoException e) {
				errorMessageBuffer
					.append("Could not display automobile configuration because of an internal server error: <BR>");
				errorMessageBuffer.append(e.getMessage());
				errorMessageBuffer.append("<br><br>automobileKey: ");
				errorMessageBuffer.append(automobileKey);
				errorFlag = true;
			}
			socketClient.closeSession();
		}

		// set response
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head>");

		String title = "Car Configuration";
		String header = "Basic Car Choice";

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
			out.println("<form method=\"get\" action=\"/Lab6/jsp/CarPrice.jsp\">");
			out.println("<input type=\"hidden\" name=\"automobileKey\" value=\"" + automobileKey + "\">");
			out.println("<table>");
			out.println("<tr><td>Year</td>");
			out.println("<td>" + escapeHtml(automobileObject.getYear()) + "</td>");
			out.println("</tr>");
			out.println("<tr><td>Make</td>");
			out.println("<td>" + escapeHtml(automobileObject.getMake()) + "</td>");
			out.println("</tr>");
			out.println("<tr><td>Model</td>");
			out.println("<td>" + escapeHtml(automobileObject.getModel()) + "</td>");
			out.println("</tr>");

			int i, k, n, n1;
			n = automobileObject.length();
			// option set
			for (i = 0; i < n; i++) {
				out.println("<tr><td>" + escapeHtml(automobileObject.getOptionSetName(i)) + "</td>");
				n1 = automobileObject.getOptionSetLength(i);
				out.println("<td><select name=\"" + escapeHtml(automobileObject.getOptionSetName(i)) + "\">");
				for (k = 0; k < n1; k++) {
					// sanitize the value
					out.println("<option value=\"" + escapeHtml(automobileObject.getOptionSetOptionName(i, k)) + "\">");
					out.println(escapeHtml(automobileObject.getOptionSetOptionName(i, k)));
					out.println(" ($" + automobileObject.getOptionSetOptionPrice(i, k) + ")</option>");
				}
				out.println("</select></td></tr>");
			}

			out.println("<tr><td colspan=\"2\" style=\"text-align: right\">");
			out.println("<a href=\"/Lab6/servlet/servlets.CarSelection\">back</a>");
			out.println("<input type=\"submit\" value=\"Send Request\"></td>");
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
