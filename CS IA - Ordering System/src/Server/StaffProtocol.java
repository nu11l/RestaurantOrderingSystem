package Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

public class StaffProtocol extends ClientProtocol{
	public Vector<Integer> omiRemoveRequests = new Vector<Integer>();
	
	public StaffProtocol(String name, Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
		super(name, s, ois, oos);
	}

}
