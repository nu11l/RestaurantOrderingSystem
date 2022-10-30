package StaffClient;

import DataStructures.CustomerOrder;

public class OrderHandler {
	private CustomerOrder co;
	public OrderHandler(CustomerOrder co) {
		this.co = co;
	}
	public OrderMenuItem generateOMI(boolean isFiller) {
		return new OrderMenuItem(co.clientNumber,
				co.itemsOrdered, co.cost, isFiller);
	}

}
