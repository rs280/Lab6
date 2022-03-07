package client;

import exception.AutoException;

public interface SocketClientInterface {

	void openConnection() throws AutoException;

	void handleSession();

	void closeSession();
}
