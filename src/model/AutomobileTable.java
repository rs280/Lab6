package model;

import java.util.*;
import java.io.Serializable;

import exception.AutoException;

/** @class AutomobileTable
 *        Keeps a hash table of all automobile objects. \n
 *        This is essentially our database to manage our automobiles. **/
public class AutomobileTable implements Serializable {
	private static final long serialVersionUID = -6524925073605314987L;
	private Map<String, Automobile> automobileTable;

	/* constructors */
	public AutomobileTable() {
		automobileTable = new LinkedHashMap<String, Automobile>(64);
	}

	public AutomobileTable(int capacitySize) {
		automobileTable = new LinkedHashMap<String, Automobile>(capacitySize);
	}

	public Automobile getByKey(String automobileKey) {
		return automobileTable.get(automobileKey);
	}

	/** get the hash table key for an automobile object. \n
	 * key is a combination of Make, Model, and Year.
	 * @return the key string
	 * @throws AutoException **/
	public String getKey(Automobile automobileObject) throws AutoException {
		if (automobileObject == null)
			// Automobile could not be found in database
			throw new exception.AutoException(502);
		return automobileObject.getMake() + "-" + automobileObject.getModel() + "-" + automobileObject.getYear();
	}

	/** Gets the map iterator allowing one to traverse the map.
	 * @return The map iterator */
	public Iterator<Map.Entry<String, Automobile>> getIterator() {
		return automobileTable.entrySet().iterator();
	}

	public Directory toDirectory() {
		Directory automobileDirectory = new Directory();
		automobileDirectory.map = new LinkedHashMap<String, String>(automobileTable.size());
		Iterator<Map.Entry<String, Automobile>> mapIterator = automobileTable.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Map.Entry<String, Automobile> mapEntry = mapIterator.next();
			StringBuffer carNameBuffer = new StringBuffer();
			carNameBuffer.append(mapEntry.getValue().getYear());
			carNameBuffer.append(" ").append(mapEntry.getValue().getMake()).append(" ")
				.append(mapEntry.getValue().getModel());
			automobileDirectory.map.put(mapEntry.getKey(), carNameBuffer.toString());
		}
		return automobileDirectory;
	}

	/** Inserts an automobile into the hash table. Overwrites existing
	 * Automobiles
	 * with the same Make, Model, and Year.
	 * @return the key in the hash table **/
	public String insertOverwrite(Automobile automobileObject) throws AutoException {
		/* key = Make-Model-Year */
		String automobileKey = null;
		try {
			automobileKey = getKey(automobileObject);
			automobileTable.put(automobileKey, automobileObject);
		} catch (NullPointerException e) {
			// Automobile could not be added to database
			throw new exception.AutoException(501);
		} catch (Exception e) {
			// Automobile could not be added to database
			throw new exception.AutoException(501);
		}
		return automobileKey;
	}

	/** Inserts an automobile into the hash table. If an automobile with the
	 * same
	 * key exists then the exception fixString() method is used to correct it.
	 * @return the key in the hash table **/
	public String insertWrapper(Automobile automobileObject) throws AutoException {
		/* key = Make-Model-Year */
		int tryNumber = 1;
		String automobileKey = null;
		automobileKey = getKey(automobileObject);
		// self-healing
		while (tryNumber > 0) {
			try {
				insert(automobileKey, automobileObject);
			} catch (exception.AutoException e) {
				// just try once to fix
				if (tryNumber > 1) {
					tryNumber = -5;
				}
				e.setAutomobile(automobileObject);
				automobileKey = e.fixString(500);
				tryNumber += 1;
			}
		}
		return automobileKey;
	}

	public boolean insert(String automobileKey, Automobile automobileObject) throws AutoException {
		boolean returnValue = false;
		if (automobileTable.containsKey(automobileKey)) {
			throw new exception.AutoException(500);
		} else {
			automobileTable.put(automobileKey, automobileObject);
			returnValue = true;
		}
		return returnValue;
	}

	/* print() and toString() */
	public void print() {
		System.out.print(toString());
	}

	public String toString() {
		return "Automobile Table";
	}
	
	public class Directory implements Serializable {
		private static final long serialVersionUID = 8187458227654283135L;
		public Map<String, String> map;
	}
}
