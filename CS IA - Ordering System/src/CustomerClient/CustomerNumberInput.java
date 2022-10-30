package CustomerClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CustomerNumberInput extends ScrollComponent implements ActionListener {
	JTextArea numDisplay;
	private String defaultText = "Please Enter Your Order Number:\t\t";
	public CustomerNumberInput() {
		this.setLayout(new BorderLayout());
		numDisplay = new JTextArea(defaultText + "0");
		numDisplay.setEditable(false);
		numDisplay.setFocusable(false);
		numDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		numDisplay.setBackground(Color.WHITE);
		numDisplay.setFont(new Font(numDisplay.getFont().getName(), Font.BOLD, 22));
		numDisplay.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		numDisplay.setPreferredSize(new Dimension(200, 50));
		this.add(numDisplay, BorderLayout.PAGE_START);
		
		JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
		//programmatically generates buttons for order number input and assigns names for future reference
		for(int i = 0; i < 10; i++) {
			GridBagConstraints btn = new GridBagConstraints();
			btn.weighty = 1;
			btn.weightx = 1;
			btn.anchor = GridBagConstraints.CENTER;
			btn.gridy = 0;
			btn.gridx = i;
			btn.fill = GridBagConstraints.BOTH;
			btn.ipady = 30;
			JButton toAdd = new JButton("" + i);
			toAdd.addActionListener(this);
			toAdd.setName("NUM_" + i);
			toAdd.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			toAdd.setBackground(Color.LIGHT_GRAY);
			toAdd.setFont(new Font(toAdd.getFont().getName(), Font.BOLD, 24));
			toAdd.setFocusable(false);
			toAdd.setFocusPainted(false);
			buttonPanel.add(toAdd);
		}
		JButton delButton = new JButton("DEL");
		delButton.setBackground(Color.RED);
		delButton.setName("DELETE");
		delButton.addActionListener(this);
		delButton.setFocusable(false);
		delButton.setFocusPainted(false);
		buttonPanel.add(delButton);
		
		JButton contButton = new JButton("CONT");
		contButton.setBackground(Color.GREEN);
		contButton.setName("CONTINUE");
		contButton.addActionListener(this);
		buttonPanel.add(contButton);
		contButton.setFocusable(false);
		contButton.setFocusPainted(false);
		this.add(buttonPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton button = (JButton)e.getSource();
			if(button.getName().contains("NUM_")) {
				if(numDisplay.getText().equals(defaultText + "0")) {
					numDisplay.setText(defaultText + button.getText());
				}else {
					numDisplay.append(button.getText());
				}
			}else if(button.getName().equals("DELETE")) {
				if(numDisplay.getText().length() > (defaultText.length()) + 1) {
					numDisplay.setText(numDisplay.getText().substring(0, numDisplay.getText().length() - 1));
				}else if(numDisplay.getText().length() <= (defaultText.length() + 1)){
					numDisplay.setText(defaultText + "0");
				}
				
			}else if(button.getName().equals("CONTINUE")) {
				CustomerClient.customerNumber = Integer.parseInt(numDisplay.getText().substring(defaultText.length()));
				CustomerClient.showPanel("main");
				numDisplay.setText(defaultText + "0");
			}
			
		}
		
	}
}
