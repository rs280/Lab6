/**
 * Assignment 5
 * 6/12/2018
 * Interface implemented by BuildAuto as defined in the directions. 
 * This interface requires server methods to aid Automobile creation
 */
package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import exception.AutoException;
import model.Automobile;

public interface AutoServer {
	public String buildAutomobileFromProperties(Properties automobileProperties) throws exception.AutoException;

	public Properties propertiesFromStream(InputStream socketStreamIn) throws exception.AutoException;

	public String automobileFromStream(InputStream socketStreamIn) throws exception.AutoException;

	public void automobileToStream(OutputStream socketStreamOut, String automobileKey) throws exception.AutoException;

	public void directoryToStream(OutputStream socketStreamOut) throws exception.AutoException;

	public String getAutomobileList();
	
	public model.AutomobileTable.Directory getAutomobileDirectoryMap();
	
	public Iterator<Map.Entry<String, Automobile>> getAutomobileIterator();
}
