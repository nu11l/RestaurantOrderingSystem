package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import DataStructures.CustomerOrder;

public class ClientProtocol implements Runnable {

	public static enum ClientID {
		CUSTOMER, STAFF
	}

	public ClientID getID() {
		return (name.contains("staff") ? ClientID.STAFF : ClientID.CUSTOMER);
	}

	String name;
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public void send(Object ob) {
		try {
			oos.writeObject(ob);
			//oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ClientProtocol(String name, Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
		this.name = name;
		this.s = s;
		this.ois = ois;
		this.oos = oos;
	}

	@Override
	public void run() {
		Object received;
		while (true) {
			System.out.println(name);
			try {
				received = ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				CentralServer.removeClient(name);
				return;
			}
			if (getID() == ClientID.CUSTOMER) {
		
				if(!((CustomerProtocol)this).receivedMenu) {
					Scanner sc = null;
					try {
						sc = new Scanner(new File("menu.txt"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List<String> lines = new ArrayList<String>();
					while (sc.hasNextLine()) {
						lines.add(sc.nextLine());
					}

					String[] menuArray = lines.toArray(new String[0]);
					for (String s : menuArray) {
						System.out.println(s);
					}
					sc.close();
					send(menuArray);
					((CustomerProtocol)this).receivedMenu = true;
				}
				for (ClientProtocol clp : CentralServer.clients) {
					if (clp.getID() == ClientID.STAFF) {
						// strings received from customerclient always
						// expected to be
						// commands for staffclient
						if (received instanceof String) {
							clp.send((String) received);
							// customerorders from customerclient will be
							// delivered to
							// staffclient to be displayed
						} else if (received instanceof CustomerOrder) {
							clp.send((CustomerOrder) received);
						}
					}

				}
			} else if (getID() == ClientID.STAFF) {
				if (received instanceof String) {
					String command = (String) received;
					if (command.contains("remove@")) {
						int removeIndex = Integer.parseInt(command.split("@")[1]);
						System.out.println(removeIndex);
						((StaffProtocol) this).omiRemoveRequests.add(removeIndex);
						boolean removalAgree = true;
						for (ClientProtocol staff : CentralServer.clients) {
							if (staff.getID() == ClientID.STAFF) {
								if (((StaffProtocol) staff).omiRemoveRequests.contains(removeIndex)) {
									continue;
								} else {
									removalAgree = false;
									break;
								}
							}
						}
						if (removalAgree) {
							for (ClientProtocol clp : CentralServer.clients) {
								if (clp.getID() == ClientID.STAFF) {
									// iterator does not throw
									// concurrentmodificaitonexception which may
									// happen when modifying vectors in
									// different threads
									for (Iterator<Integer> iterator = ((StaffProtocol) clp).omiRemoveRequests
											.iterator(); iterator.hasNext();) {
										Integer i = iterator.next();
										if (i == removeIndex) {
											iterator.remove();
										}
									}
									clp.send(new Integer(removeIndex));
								}

							}
						} else {
							for (ClientProtocol clp : CentralServer.clients) {
								if (clp.getID() == ClientID.STAFF) {
									clp.send(new String("$pendingremoval@" + removeIndex));
								}
							}
						}
					}
				}
			}
		}

	}
}
