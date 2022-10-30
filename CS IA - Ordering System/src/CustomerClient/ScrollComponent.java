package CustomerClient;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScrollComponent extends JComponent {
	public JPanel contentPanel;
	public JScrollPane contentScrollPane;
	public ScrollComponent() {
		this.setLayout(new GridBagLayout());
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		contentScrollPane = new JScrollPane(contentPanel);
		contentScrollPane.setAutoscrolls(false);
		contentScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(25, 0));
		GridBagConstraints contentConstraints = new GridBagConstraints();
		contentConstraints.gridx = 0;
		contentConstraints.gridy = 0;
		contentConstraints.anchor = GridBagConstraints.PAGE_START;
		contentConstraints.fill = GridBagConstraints.BOTH;
		contentConstraints.weighty = 1;
		contentConstraints.weightx = 1;
		this.add(contentScrollPane, contentConstraints);
	}

	public void addComponent(Component c, GridBagConstraints constraints) {
		contentPanel.add(c, constraints);
	}
	public void resetQuantities() {
		for(Component c : contentPanel.getComponents()) {
			if(c instanceof MenuItem) {
				if(((MenuItem)c).itemQuantity > 0) {
					((MenuItem)c).resetQuantity();
					
				}
			}
		}
	}
	public void removeComponent(Component c) {
		contentPanel.remove(c);
		this.revalidate();
		this.repaint();
	}
	public void clearComponents() {
		contentPanel.removeAll();
	}
}
