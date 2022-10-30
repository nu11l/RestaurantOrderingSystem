package CustomerClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import CustomerClient.MenuHandler.MenuItemType;
import DataStructures.ItemInfo;

public class MenuItem extends JPanel implements ActionListener{
	
	public String title;
	public String description = null;
	public int cost = -1;
	public String picPath = null;
	
	public int itemQuantity = 0;
	
	public boolean inCart = false;
	
	public MenuItemType ID = MenuItemType.DEFAULT;
	
	public static int REF_NUM = -1;
	public int instanceNum;
	public MenuItem(String title, String description, int cost, String picPath) throws IOException {
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		this.title = title;
		this.description = description;
		this.cost = cost;
		this.picPath = picPath;
		
		ID = MenuItemType.DETAILED;
		instanceNum = ++REF_NUM;
		
		init();
	}
	
	public MenuItem(String title, int cost, MenuItemType ID) throws IOException {
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		this.title = title;
		this.cost = cost;
		
		this.ID = ID;
		instanceNum = ++REF_NUM;
		init();
	}
	
	public MenuItem(String title, int cost, String desc, MenuItemType ID) throws IOException {
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		this.title = title;
		this.cost = cost;
		this.description = desc;
		this.ID = ID;
		instanceNum = ++REF_NUM;
		init();
	}
	
	private JTextArea quantity;
	
	public void init() {
		if(ID == MenuItemType.DEFAULT) {
			ID = (description == null && picPath == null ? MenuItemType.BASIC : MenuItemType.DETAILED);
		}
		JLabel titleLabel = new JLabel(title);
		
		JTextArea desc = new JTextArea(description);
		desc.setLineWrap(true);
		desc.setText(description);
		desc.setEditable(false);
		desc.setFocusable(false);
		desc.setFont(new Font(desc.getFont().getName(), Font.BOLD, 12));
		desc.setPreferredSize(new Dimension(140, 140));
		
		JPanel quantityPanel = new JPanel(new GridBagLayout());
		quantityPanel.setBackground(Color.WHITE);
		JButton quantitySub = new JButton("-");
		quantitySub.setBackground(Color.WHITE);
		quantitySub.setForeground(Color.RED);
		quantitySub.setFont(new Font(quantitySub.getFont().getName(), Font.BOLD, 22));
		
		JButton quantityAdd = new JButton("+");
		quantityAdd.setBackground(Color.WHITE);
		quantityAdd.setForeground(Color.GREEN);
		quantityAdd.setFont(new Font(quantityAdd.getFont().getName(), Font.BOLD, 22));
		
		quantity = new JTextArea("" + itemQuantity);
		quantity.setEditable(false);
		quantity.setFocusable(false);
		quantity.setFont(new Font(quantity.getFont().getName(), Font.BOLD, 16));
		
		JLabel costLabel = new JLabel(cost + "LE");
		
		if(ID == MenuItemType.DETAILED) {
			GridBagConstraints titleConstraints = new GridBagConstraints();
			titleConstraints.gridx = 0;
			titleConstraints.gridy = 0;
			titleConstraints.weightx = 1;
			titleConstraints.weighty = 1;
			titleConstraints.gridwidth = GridBagConstraints.REMAINDER;
			titleConstraints.anchor = GridBagConstraints.NORTHWEST;
			this.add(titleLabel, titleConstraints);
			//picPath is set to an arbitrary value such that the placeholder image is displayed
			//the client has not provided me with the images yet so that this placeholder portion
			//may be removed
			//once he provides me with the images, i will alter the code and provide him with the updated solution
			picPath = "o";
			if(picPath != null) {
				BufferedImage myPicture = null;
				try {
					myPicture = ImageIO.read(getClass().getResource("/download.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				
				}catch(NullPointerException e) {
					System.out.println("cant find shit");
				}
				if(myPicture != null) {
					JLabel picLabel = new JLabel(new ImageIcon(myPicture));
					picLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
					picLabel.setPreferredSize(new Dimension(150, 150));
					GridBagConstraints picConstraints = new GridBagConstraints();
					picConstraints.gridx = 0;
					picConstraints.gridy = 1;
					picConstraints.weightx = 1;
					picConstraints.weighty = 1;
					picConstraints.anchor = GridBagConstraints.WEST;
					this.add(picLabel, picConstraints);
				}
			}
			
			if(description != null) {
				GridBagConstraints descConstraints = new GridBagConstraints();
				descConstraints.gridx = GridBagConstraints.RELATIVE;
				descConstraints.gridy = 1;
				descConstraints.weighty = 1;
				descConstraints.weightx = 1;
				descConstraints.gridwidth = GridBagConstraints.REMAINDER;
				descConstraints.anchor = GridBagConstraints.NORTH;
				descConstraints.fill = GridBagConstraints.HORIZONTAL;
				this.add(desc, descConstraints);
			}
			
			GridBagConstraints quanPnlConstraints = new GridBagConstraints();
			quanPnlConstraints.gridx = 1;
			quanPnlConstraints.gridy = GridBagConstraints.RELATIVE;
			quanPnlConstraints.weightx = 1;
			quanPnlConstraints.weighty = 1;
			//quanPnlConstraints.gridwidth = 2;
			quanPnlConstraints.anchor = GridBagConstraints.SOUTHEAST;
			this.add(quantityPanel, quanPnlConstraints);
			
			
			GridBagConstraints quantityConstraints = new GridBagConstraints();
			quantityConstraints.insets = new Insets(0, 0, 0, 20);
			quantityConstraints.gridx = 0;
			quantityConstraints.gridy = 0;
			quantityConstraints.anchor = GridBagConstraints.WEST;
			quantityConstraints.fill = GridBagConstraints.HORIZONTAL;
			quantityConstraints.weightx = 0.5;
			quantityPanel.add(quantitySub, quantityConstraints);
			

			quantityConstraints.gridx = 1;
			
			quantityPanel.add(quantity, quantityConstraints);
			
			
			
			quantityConstraints.gridx = 2;
			quantityPanel.add(quantityAdd, quantityConstraints);
			

			GridBagConstraints costConstraints = new GridBagConstraints();
			costConstraints.gridx = 0;
			costConstraints.gridy = 2;
			costConstraints.anchor = GridBagConstraints.SOUTHWEST;
			this.add(costLabel, costConstraints);
		}else if(ID == MenuItemType.BASIC || ID == MenuItemType.CART_ITEM){
			GridBagConstraints titleConstraints = new GridBagConstraints();
			titleConstraints.gridx = 0;
			titleConstraints.gridy = 0;
			titleConstraints.weightx = 1;
			titleConstraints.weighty = 1;
			titleConstraints.gridwidth = GridBagConstraints.REMAINDER;
			titleConstraints.anchor = GridBagConstraints.NORTHWEST;
			this.add(titleLabel, titleConstraints);
			if(description != null) {
				GridBagConstraints descConstraints = new GridBagConstraints();
				descConstraints.gridx = 1;
				descConstraints.gridy = 1;
				descConstraints.weighty = 1;
				descConstraints.weightx = 1;
				descConstraints.gridwidth = GridBagConstraints.REMAINDER;
				descConstraints.anchor = GridBagConstraints.NORTH;
				descConstraints.fill = GridBagConstraints.HORIZONTAL;
				this.add(desc, descConstraints);
			}
			if(ID != MenuItemType.CART_ITEM) {
				
				GridBagConstraints quanPnlConstraints = new GridBagConstraints();
				quanPnlConstraints.gridx = 1;
				quanPnlConstraints.gridy = 2;
				quanPnlConstraints.weightx = 1;
				quanPnlConstraints.weighty = 1;
				//quanPnlConstraints.gridwidth = 2;
				quanPnlConstraints.anchor = GridBagConstraints.EAST;
				this.add(quantityPanel, quanPnlConstraints);
				
				GridBagConstraints quantityConstraints = new GridBagConstraints();
				quantityConstraints.insets = new Insets(0, 0, 0, 20);
				quantityConstraints.gridx = 0;
				quantityConstraints.gridy = 0;
				quantityConstraints.anchor = GridBagConstraints.WEST;
				quantityConstraints.fill = GridBagConstraints.HORIZONTAL;
				quantityConstraints.weightx = 0.5;
				quantityPanel.add(quantitySub, quantityConstraints);
				
				
				
				quantityConstraints.gridx = 1;
				quantityPanel.add(quantity, quantityConstraints);
				
				quantityConstraints.gridx = 2;
				quantityPanel.add(quantityAdd, quantityConstraints);
				
				
			}else if(ID == MenuItemType.CART_ITEM) {
				GridBagConstraints quanPnlConstraints = new GridBagConstraints();
				quanPnlConstraints.gridx = 1;
				quanPnlConstraints.gridy = 2;
				quanPnlConstraints.weightx = 1;
				quanPnlConstraints.weighty = 1;
				//quanPnlConstraints.gridwidth = 2;
				quanPnlConstraints.anchor = GridBagConstraints.EAST;
				this.add(quantityPanel, quanPnlConstraints);
				
				GridBagConstraints quantityConstraints = new GridBagConstraints();
				quantityConstraints.insets = new Insets(0, 0, 0, 20);
				quantityConstraints.gridx = 0;
				quantityConstraints.gridy = 0;
				quantityConstraints.anchor = GridBagConstraints.WEST;
				quantityConstraints.fill = GridBagConstraints.HORIZONTAL;
				quantityConstraints.weightx = 0.5;
				quantityConstraints.weighty = 1;
				quantityPanel.add(quantitySub, quantityConstraints);
				
				System.out.println(title);
				//quantity.setText("" + itemQuantity);
				quantity.setCaretPosition(0);
				System.out.println(itemQuantity);
				quantityConstraints.gridx = 1;
				quantityPanel.add(quantity, quantityConstraints);
				
				quantityConstraints.gridx = 2;
				quantityPanel.add(quantityAdd, quantityConstraints);
			}
			

			GridBagConstraints costConstraints = new GridBagConstraints();
			costConstraints.gridx = 0;
			costConstraints.gridy = 2;
			costConstraints.anchor = GridBagConstraints.SOUTHWEST;
			this.add(costLabel, costConstraints);
			
			if(ID == MenuItemType.CART_ITEM) {
				titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 22));
				desc.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 22));
				costLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 22));
			}
			
		}
		quantityAdd.addActionListener(this);
		quantitySub.addActionListener(this);
		
		quantityAdd.setFocusPainted(false);
		quantityAdd.setFocusable(false);

		quantitySub.setFocusPainted(false);
		quantitySub.setFocusable(false);
		quantityAdd.setPreferredSize(new Dimension(50, 35));
		quantitySub.setPreferredSize(new Dimension(50, 35));
		
	}
	
	public MenuItem() {
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		ID = MenuItemType.DEFAULT;
		instanceNum = ++REF_NUM;
	}

	public MenuItem(MenuItem mi) {
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		description = mi.description;
		cost = mi.cost;
		title = mi.title;
		itemQuantity = mi.itemQuantity;
		ID = mi.ID;
		instanceNum = mi.instanceNum;
		init();
	}
	
	public ItemInfo getItem() {
		return new ItemInfo(title, description, itemQuantity, cost, instanceNum);
	}
	
	public void resetQuantity() {
		itemQuantity = 0;
		quantity.setText("" + itemQuantity);
		CustomerClient.updateCart(this.getItem());
		//this.revalidate();
		//this.repaint();
	}
	public void setQuantity(int quan) {
		itemQuantity = quan;
		quantity.setText("" + itemQuantity);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton source = ((JButton)e.getSource());
			if(source.getText().equals("-")) {
				itemQuantity -= (itemQuantity > 0 ? 1 : 0);
			}else if(source.getText().equals("+")) {
				itemQuantity += 1;
			}
			quantity.setText("" + itemQuantity);
			CustomerClient.updateCart(this.getItem());
			if(inCart) {
				CustomerClient.synchMainMenuWithCart(this.getItem());
				//CustomerClient.populateCartDisplay();
				//CustomerClient.refreshContentPane();
			}
			
		}
		
		
	}

}
