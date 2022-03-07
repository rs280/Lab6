package client;

import java.io.BufferedReader;

public class SelectCarOption {
	private BufferedReader stdIn;
	private model.Automobile automobileObject;

	public SelectCarOption(BufferedReader stdIn_, model.Automobile automobileObject_) {
		stdIn = stdIn_;
		automobileObject = automobileObject_;
	}

	public void beginSelection() {
		automobileObject.print();
	}
}
