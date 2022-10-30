package DataStructures;

import java.io.Serializable;
import java.util.Vector;

import CustomerClient.MenuHandler;

public class CustomerOrder implements Serializable{
	public Vector<Pair<Integer, String>> itemsOrdered = new Vector<Pair<Integer, String>>();
	public int clientNumber;
	public int cost;
	public CustomerOrder(Vector<Pair<Integer, String>> itemsOrdered, int clientNumber, int cost) {
		this.itemsOrdered = itemsOrdered;
		this.clientNumber = clientNumber;
		this.cost = cost;
	}
	public CustomerOrder() {	}
	
}