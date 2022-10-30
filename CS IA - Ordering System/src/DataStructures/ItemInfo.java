package DataStructures;

public class ItemInfo {
	private String name;
	private String description;
	private int quantity;
	private int cost;
	private int instanceNum;
	public ItemInfo(String name, String description, int quantity, int cost, int instanceNum) {
		this.name = name;
		this.description = description;
		this.quantity = quantity;
		this.cost = cost;
		this.instanceNum = instanceNum;
	}
	public int calcCost() {
		return cost * quantity;
	}
	public String getDescription() {
		return description;
	}
	public int getCost() {
		return cost;
	}
	public int getID() {
		return instanceNum;
	}
	public int getQuantity() {
		return quantity;
	}
	public String getName() {
		return name;
	}
	public void setQuantity(int quant) {
		quantity = quant;
	}
	public boolean equiv(ItemInfo i) {
		return (instanceNum == i.getID());
	}

}
