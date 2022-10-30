package CustomerClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import DataStructures.ItemInfo;

public class ItemSelectScreen extends JPanel implements ActionListener{
	private static JTabbedPane menuTabs = new JTabbedPane();
	private static JButton showCartBtn = new JButton();
	public ItemSelectScreen() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints menuTabConstraints = new GridBagConstraints();
		menuTabConstraints.gridx = 0;
		menuTabConstraints.gridy = 0;
		menuTabConstraints.anchor = GridBagConstraints.NORTHWEST;
		menuTabConstraints.weightx = 1;
		menuTabConstraints.weighty = 1;
		menuTabConstraints.fill = GridBagConstraints.BOTH;
		this.add(menuTabs, menuTabConstraints);
		menuTabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				((ScrollComponent)menuTabs.getSelectedComponent()).contentScrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		
		JPanel bottomBar = new JPanel(new GridBagLayout());
		bottomBar.setBackground(Color.WHITE);
		BufferedImage cartIcon = null;
		try {
			cartIcon = ImageIO.read(getClass().getResource("/cart.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}catch(NullPointerException e) {
			System.out.println("cant find shit");
		}
		if(cartIcon != null) {
			showCartBtn = new JButton(new ImageIcon(cartIcon));
		}
		
		showCartBtn.setFocusable(false);
		showCartBtn.setFocusPainted(false);
		showCartBtn.setText("0");
		showCartBtn.setFont(new Font(showCartBtn.getFont().getName(), Font.BOLD, 16));
		showCartBtn.setHorizontalAlignment(JButton.RIGHT);
		showCartBtn.setBackground(Color.WHITE);
		showCartBtn.setVisible(false);
		showCartBtn.setName("CARTBUTTON");
		showCartBtn.addActionListener(this);
		

		GridBagConstraints bottomBarConstraints = new GridBagConstraints();
		bottomBarConstraints.gridx = 2;
		bottomBarConstraints.gridy = 0;
		bottomBarConstraints.anchor = GridBagConstraints.EAST;
		bottomBarConstraints.weightx = 1;
		bottomBarConstraints.weighty = 1;
		bottomBar.add(showCartBtn, bottomBarConstraints);

		GridBagConstraints cartConstraints = new GridBagConstraints();
		cartConstraints.gridx = 0;
		cartConstraints.gridy = 1;
		cartConstraints.anchor = GridBagConstraints.EAST;
		//cartConstraints.insets = new Insets(5, 0, 0, 0);
		cartConstraints.fill = GridBagConstraints.BOTH;
		this.add(bottomBar, cartConstraints);
	}
	public void populateSelectionScreen(String[] received) {
		MenuHandler.populateMenu((String[])received, menuTabs);
		showCartBtn.setVisible(true);
	}
	public void updateCartQuantityDisplay(int newQuantity) {
		showCartBtn.setText("" + newQuantity);
	}
	public void resetComponentsToDefault() {
		for(Component c1 : menuTabs.getComponents()) {
			if(c1 instanceof ScrollComponent) {
				((ScrollComponent)c1).resetQuantities();
				((ScrollComponent)c1).contentScrollPane.getVerticalScrollBar().setValue(0);
			}
		}
		menuTabs.getModel().setSelectedIndex(0);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			if(((JButton)e.getSource()).getName().equals("CARTBUTTON")) {
				CustomerClient.showPanel("cart");
				CustomerClient.populateCartDisplay();
			}
		}
		
	}
	public void synchItemQuantityFromCart(ItemInfo item) {
		for(Component c1 : menuTabs.getComponents()) {
			if(c1 instanceof ScrollComponent) {
				for(Component com : ((ScrollComponent)c1).contentPanel.getComponents()) {
					if(com instanceof MenuItem) {
						MenuItem mi = ((MenuItem)com);
						if(mi.getItem().equiv(item)) {
							mi.setQuantity(item.getQuantity());
						}
					}
				}
			}
		}
		
	}

}
