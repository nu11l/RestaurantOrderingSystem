package Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CustomerProtocol extends ClientProtocol {
	public boolean receivedMenu = false; 
	public CustomerProtocol(String name, Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
		super(name, s, ois, oos);
	}

}
