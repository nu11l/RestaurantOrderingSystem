package StaffClient;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Scrollable;

import DataStructures.Pair;

public class OrderMenuItem extends JPanel implements Scrollable{
	
	public JButton resolveButton;
	
	//function that translates the vector of quantity-item pairs into a two-dimensional
	//array that can be passed as a parameter to JTable
	
	String[][] translateData(Vector<Pair<Integer, String>> dataVector) {
		String[][] translated = new String[dataVector.size()][2];
		for(int i = 0; i < translated.length; i++) {
			translated[i][0] = dataVector.get(i).getFirst().toString();
			translated[i][1] = dataVector.get(i).getSecond().toString();
		}
		return translated;
	}
	
	public void selfDestruct() {
		if(!toResolve) {
			StaffClient.removeOrderMenuItem(this);
			StaffClient.Refresh();
			toResolve = true;
		}
		
	}
	
	public boolean isFiller = false;
	public boolean toResolve = false;
	public static int CUR_ORDERNUM = 0;
	public int INSTANCE_ORDERNUM = 0;
	public OrderMenuItem(int orderNumber, Vector<Pair<Integer, String>> itemsOrdered, int cost, boolean isFiller) {
		INSTANCE_ORDERNUM = ++CUR_ORDERNUM;
		
		this.setLayout(new GridBagLayout());
		this.isFiller = isFiller;
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		JLabel orderNumberLabel = new JLabel("Customer Number: " + orderNumber);
		orderNumberLabel.setFont(new Font(orderNumberLabel.getFont().getName(), Font.BOLD, 16));
		//this.add(orderNumberLabel, c);
		
		
		JLabel costLabel = new JLabel("Total Cost: " + cost + " LE");
		costLabel.setFont(new Font(costLabel.getFont().getName(), Font.BOLD, 16));
		
		resolveButton = new JButton("X");
		resolveButton.setFont(new Font(resolveButton.getFont().getName(), Font.BOLD, 16));
		resolveButton.setForeground(Color.GREEN);
		resolveButton.setSize(20, 20);
		resolveButton.setFocusPainted(false);
		resolveButton.setFocusable(false);
		resolveButton.setBorderPainted(false);
		resolveButton.setBackground(Color.LIGHT_GRAY);
		resolveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				resolveButton.setText("...");
				resolveButton.setForeground(Color.YELLOW);
				selfDestruct();
			}
		});
		
		this.add(resolveButton);
		
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 3));
		topPanel.setBackground(Color.WHITE);
		this.add(topPanel);
		
		
		topPanel.add(costLabel);
		topPanel.add(orderNumberLabel);
		
		String[][] data = translateData(itemsOrdered);
		String[] column = {"Quantity", "Item"};
		JTable itemTable = new JTable(data, column);
		itemTable.setFont(new Font(itemTable.getFont().getName(), Font.PLAIN, 16));
		GridBagConstraints tableConstraint = new GridBagConstraints();
		tableConstraint.gridx = 0;
		tableConstraint.gridy = 1;
		tableConstraint.gridwidth = GridBagConstraints.REMAINDER;
		tableConstraint.gridheight = 1;
		tableConstraint.weighty = 10;
		tableConstraint.weightx = 1;
		tableConstraint.fill = GridBagConstraints.BOTH;
		tableConstraint.anchor = GridBagConstraints.NORTH;
		JScrollPane tableScrollPane = new JScrollPane(itemTable);
		tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
		//tableScrollPane.setPreferredSize(new Dimension(itemTable.getPreferredSize().width, itemTable.getPreferredSize().height + 20));
		this.add(tableScrollPane, tableConstraint);
		
		this.setPreferredSize(new Dimension(200, 140));
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}
}
