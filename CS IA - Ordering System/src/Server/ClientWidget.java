package Server;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* The ClientWidget panel is just a box that appears on the CentralServer
 * UI that indicates what type of client has connected. It is an icon that
 * delineates the current connections. Staff appear as cyan boxes beside the
 * "Staff" JLabel in the frame, while customer clients appear as yellow
 * boxes beside the "Customer" JLabel.
 */


public class ClientWidget extends JPanel{
	private static final long serialVersionUID = 1L;
	private String name;
	public ClientWidget(String name) {
		this.name = name;
		this.setLayout(new FlowLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.setBackground(Color.LIGHT_GRAY);
		JLabel clientName = new JLabel(name);
		this.setBackground(name.contains("staff") ? Color.CYAN : Color.YELLOW);
		
		this.add(clientName);
	}
	public String getName() {
		return name;
	}
}
