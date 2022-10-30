package CustomerClient;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import DataStructures.ItemInfo;

public class MenuHandler {
	private static Vector<MenuItem> items = new Vector<MenuItem>();

	private static Vector<ScrollComponent> comps = new Vector<ScrollComponent>();

	public static enum MenuItemType {
		DETAILED, BASIC, HEADER, CART_ITEM, DEFAULT
	}

	public static void populateMenu(String[] source, JTabbedPane menuTab) {
		int compsCounter = 0;
		int counter = 0;
		for (String s : source) {
			if (s.charAt(0) != '+' && s.charAt(0) != '=' && s.charAt(0) != 'a') {
				System.out.println(s);
				String[] divideLine = s.split("=");
				String prefix = divideLine[0];
				String content = divideLine[1];
				if (prefix.equals("title")) {
					items.add(counter, new MenuItem());
					counter++;
					System.out.println(s.substring(1));
					items.elementAt(counter - 1).title = content;
				} else if (prefix.equals("desc")) {
					System.out.println(s.substring(1));
					items.elementAt(counter - 1).description = content;
				} else if (prefix.equals("cost")) {
					System.out.println(s.substring(1));
					items.elementAt(counter - 1).cost = (int) Double.parseDouble(content.replaceAll("EGP", "").replaceAll("EGP ", ""));
				} else if (prefix.equals("picpath")) {
					System.out.println(s.substring(1));
					items.elementAt(counter - 1).picPath = content;
				}else if(prefix.equals("subheader")) {
					GridBagConstraints MIConstraints = new GridBagConstraints();
					MIConstraints.gridx = 0;
					MIConstraints.gridy = GridBagConstraints.RELATIVE;
					MIConstraints.fill = GridBagConstraints.HORIZONTAL;
					MIConstraints.anchor = GridBagConstraints.PAGE_START;
					MIConstraints.weightx = 1;
					MIConstraints.weighty = 1;
					JLabel subheader = new JLabel(content);
					subheader.setFont(new Font(subheader.getFont().getName(), Font.BOLD, 18));
					comps.elementAt(compsCounter - 1).addComponent(subheader, MIConstraints);
				}else if(prefix.equals("mainheader")) {
					GridBagConstraints MIConstraints = new GridBagConstraints();
					MIConstraints.gridx = 0;
					MIConstraints.gridy = GridBagConstraints.RELATIVE;
					MIConstraints.fill = GridBagConstraints.HORIZONTAL;
					MIConstraints.anchor = GridBagConstraints.PAGE_START;
					MIConstraints.weightx = 1;
					MIConstraints.weighty = 1;
					JLabel subheader = new JLabel(content);
					subheader.setFont(new Font(subheader.getFont().getName(), Font.BOLD, 22));
					comps.elementAt(compsCounter - 1).addComponent(subheader, MIConstraints);
				}
			} else {
				if (s.equals("add")) {
					items.elementAt(counter - 1).init();
					GridBagConstraints MIConstraints = new GridBagConstraints();
					MIConstraints.gridx = 0;
					MIConstraints.gridy = GridBagConstraints.RELATIVE;
					MIConstraints.fill = GridBagConstraints.HORIZONTAL;
					MIConstraints.anchor = GridBagConstraints.PAGE_START;
					MIConstraints.weightx = 1;
					MIConstraints.weighty = 1;
					comps.elementAt(compsCounter - 1).addComponent(items.elementAt(counter - 1), MIConstraints);
				} else if (s.charAt(0) == '=') {
					comps.add(compsCounter, new ScrollComponent());
					menuTab.addTab(s.substring(1), comps.elementAt(compsCounter));
					JLabel lab = new JLabel(s.substring(1));
					lab.setPreferredSize(new Dimension(150, 30));
					menuTab.setTabComponentAt(menuTab.getTabCount() - 1, lab);
					compsCounter++;
				}
			}

		}

	}

	public static void populateCart(Vector<ItemInfo> itemVec, ScrollComponent cartPanel) {
		for (ItemInfo item : itemVec) {
			GridBagConstraints itemConstraint = new GridBagConstraints();
			itemConstraint.gridx = 0;
			itemConstraint.gridy = GridBagConstraints.RELATIVE;
			itemConstraint.fill = GridBagConstraints.BOTH;
			itemConstraint.weightx = 1;
			itemConstraint.weighty = 1;
			itemConstraint.anchor = GridBagConstraints.PAGE_START;
			itemConstraint.ipady = 30;
			
			for(MenuItem itemTemp : items) {
				if(itemTemp.getItem().equiv(item)) {
					MenuItem toAdd = new MenuItem(itemTemp);
					toAdd.inCart = true;
					
					cartPanel.addComponent(toAdd, itemConstraint);
					//toAdd.init();
				}
			}
			/*MenuItem toAdd = new MenuItem();
			toAdd.title = "x" + item.quantity + " " + item.name;
			toAdd.cost = item.cost;
			toAdd.description = null;
			toAdd.itemQuantity = item.quantity;
			toAdd.ID = MenuItemType.CART_ITEM;
			toAdd.inCart = true;
			toAdd.init();
			cartPanel.addComponent(toAdd, itemConstraint);*/
		}

	}

	public static int costOf(String itemName) {
		int toReturn = 0;
		for (int i = 0; i < items.size(); i++) {
			if (itemName.equals(items.elementAt(i).title)) {
				toReturn = items.elementAt(i).cost;
			}
		}
		return toReturn;
	}

	public MenuHandler() {

	}

}
