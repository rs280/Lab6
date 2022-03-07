
<%
	/**
	This file should be moved to this location for production:
	/webapps/Lab6/jsp/CarPrice.jsp
	
	TextUtils.htmlEncode was not used because the package is not standard.
	This omission can cause issues with special characters. Avoid special characters and non-ascii.
	*/
%>
<html>

<jsp:useBean id="cart" scope="page" class="JSP.CarPrice" />
<%
	cart.processRequest(request);
%>

<html>
<head>
<title>Car Pricing Summary</title>
<style>
body {
	background-color: #f1f1f1;
}

table {
	border-collapse: collapse;
}

table, th, td {
	border: 1px solid black;
}

.center_column {
	background-color: #ffffff;
	max-width: 600px;
	margin: auto;
	padding: 15px
}

.total_row {
	font-weight: bold;
}

</style>
</head>
<body>
	<div class="center_column">
		<h1>Car Pricing Summary</h1>
		<%
			if (cart.isError()) {
		%>
		<p style="color: red">
			<%
				out.print(cart.getErrorMessage());
			%>
		</p>
		<%
			} else {
		%>
		<p>Here is what you selected:</p>
		<table>
			<tbody>
				<tr>
					<td>
						<%
							out.print(cart.escapeHtml(cart.getAutomobile().getYear()));
								out.print(" ");
								out.print(cart.escapeHtml(cart.getAutomobile().getMake()));
								out.print(" ");
								out.print(cart.escapeHtml(cart.getAutomobile().getModel()));
						%>
					</td>
					<td>base price</td>
					<td>$<%
						out.print(cart.getAutomobile().getPrice());
					%>
					</td>
				</tr>
				<%
					int i, n;
						n = cart.getAutomobile().length();
						// option set
						for (i = 0; i < n; i++) {
				%>

				<tr>
					<td>
						<%
							out.print(cart.escapeHtml(cart.getAutomobile().getOptionSetName(i)));
						%>
					</td>
					<td>
						<%
							out.print(cart.escapeHtml(cart.getAutomobile().getOptionSetChoiceName(i)));
						%>
					</td>
					<td>
						<%
							out.print(cart.getAutomobile().getOptionSetChoicePrice(i));
						%>
					</td>
				</tr>
				<%
					}
				%>
				<tr class="total_row">
					<td>Total</td>
					<td></td>
					<td>$<%
						out.print(cart.getAutomobile().getChoiceTotalPrice());
					%>
					</td>
				</tr>
			</tbody>
		</table>
		<%
			}
		%>
	</div>


</body>
</html>