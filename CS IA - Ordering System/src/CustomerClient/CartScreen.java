package CustomerClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DataStructures.CustomerOrder;
import DataStructures.ItemInfo;
import DataStructures.Pair;

public class CartScreen extends JPanel implements ActionListener{
	private static Vector<ItemInfo> cart = new Vector<ItemInfo>();
	private static int numOfItems = 0;
	
	private JLabel totalCartCost = new JLabel();
	
	private ScrollComponent cartItemScrollDisplay = new ScrollComponent();;
	
	public CartScreen() {
		BorderLayout cartMenuLayout = new BorderLayout();
		cartMenuLayout.setHgap(5);
		cartMenuLayout.setVgap(20);
		this.setLayout(cartMenuLayout);
		totalCartCost.setFont(new Font(totalCartCost.getFont().getName(), Font.BOLD, 26));
		this.add(totalCartCost, BorderLayout.PAGE_START);
		
		this.add(cartItemScrollDisplay, BorderLayout.CENTER);
		GridLayout cartMenuLowerBar = new GridLayout(1, 2);
		JPanel cartButtonPanel = new JPanel(cartMenuLowerBar);
		
		cartMenuLowerBar.setHgap(5);
		JButton backToOrderMenu = new JButton("BACK");
		backToOrderMenu.setName("CART_BACK_BTN");
		backToOrderMenu.addActionListener(this);

		JButton finalizeOrder = new JButton("FINISH");
		finalizeOrder.setName("FINALIZE");
		finalizeOrder.addActionListener(this);
		cartButtonPanel.add(backToOrderMenu);
		cartButtonPanel.add(finalizeOrder);
		this.add(cartButtonPanel, BorderLayout.PAGE_END);
	}
	private double calculateCost() {
		double toReturn = 0;
		for(ItemInfo i : cart) {
			toReturn += i.calcCost();
		}
		return toReturn;
	}
	
	public int getQuantity() {
		return numOfItems;
	}
	
	public void populateCartDisplay() {
		MenuHandler.populateCart(cart, cartItemScrollDisplay);
		cartItemScrollDisplay.contentScrollPane.getVerticalScrollBar().setValue(0);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { 
			       cartItemScrollDisplay.contentScrollPane.getVerticalScrollBar().setValue(0);
			   }}
		);
		
	}
	public void updateCart(ItemInfo item) {
		int totalItems = 0;
		//iterator does not throw concurrentmodificationexception
		boolean alreadyInCart = false;
		for(Iterator<ItemInfo> iterator = cart.iterator(); iterator.hasNext();) {
			ItemInfo i = iterator.next();
			if(i.equiv(item)) {
				if(item.getQuantity() == 0) {
					iterator.remove();
					for(Component c : cartItemScrollDisplay.contentPanel.getComponents()) {
						if(c instanceof MenuItem) {
							if(((MenuItem)c).getItem().equiv(item)) {
								cartItemScrollDisplay.removeComponent(c);
							}
							
						}
					}
				}else {
					cart.elementAt(cart.indexOf(i)).setQuantity(item.getQuantity());
				}
				alreadyInCart = true;
				break;
			}
		}
		if(!alreadyInCart) {
			cart.add(item);
		}
		for(ItemInfo i : cart) {
			totalItems += i.getQuantity();
		}
		numOfItems = totalItems;
		totalCartCost.setText("Cost: " + calculateCost());
	}
	public void unpopulateComponents() {
		cartItemScrollDisplay.clearComponents();
	}
	private CustomerOrder generateCustomerOrder(Vector<ItemInfo> cart) {
		CustomerOrder toReturn = new CustomerOrder();
		for(ItemInfo item : cart) {
			toReturn.itemsOrdered.add(new Pair<Integer, String>(item.getQuantity(), item.getName()));
		}
		toReturn.cost = (int) calculateCost();
		toReturn.clientNumber = CustomerClient.customerNumber;
		return toReturn;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton pressed = (JButton)e.getSource();
			if(pressed.getName().equals("CART_BACK_BTN")) {
				unpopulateComponents();
				CustomerClient.showPanel("main");
			}else if(pressed.getName().equals("FINALIZE")) {
				unpopulateComponents();
				CustomerClient.finalizeOrder(generateCustomerOrder(cart));
			}
		}
		
	}
}
