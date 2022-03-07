package exception;

import java.util.Date;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Log {
	PrintWriter writer;

	public Log() {
		// nothing
	}

	public void error(String str) {
		try {
			DateFormat d1 = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Date date = new Date();
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			writer.println("[" + d1.format(date) + "] ERROR " + str);
			writer.close();
		} catch (FileNotFoundException e) {
			// nothing
		} catch (SecurityException e) {
			// nothing
		} catch (IOException e) {
			// nothing
		}
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (SecurityException e) {
			// nothing
		}
	}

	public void warning(String str) {
		try {
			DateFormat d1 = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Date date = new Date();
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			writer.println("[" + d1.format(date) + "] WARNING " + str);
			writer.close();
		} catch (FileNotFoundException e) {
			// nothing
		} catch (SecurityException e) {
			// nothing
		} catch (IOException e) {
			// nothing
		}
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (SecurityException e) {
			// nothing
		}
	}
}