package StaffClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import DataStructures.CustomerOrder;
import DataStructures.Pair;

public class StaffClient extends JFrame {

	// misc. JFrame related components
	private static JPanel contentPane;
	private final JPanel panel = new JPanel();
	private static JPanel orderItemPanel = new JPanel();
	private static JScrollPane orderItemScrollpane = new JScrollPane();
	public static JLabel clientStatusLabel = new JLabel();

	// static instance of this class
	public static StaffClient frame;

	// boolean for indicating whether or not a customer machine is connected
	// to the server
	public static boolean clientConnected = false;

	public static ObjectOutputStream oos;
	public static ObjectInputStream ois;

	public static void main(String[] args) throws UnknownHostException, IOException {

		try {
			frame = new StaffClient();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Socket s = null;
		try {
			s = new Socket(InetAddress.getByName("localhost"), 4444);
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(frame, "No active session found!");
			System.exit(0);
		}
		clientStatusLabel.setText("Connected to host: Inactive session.");
		clientStatusLabel.setForeground(Color.CYAN);
		Refresh();

		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());
		oos.writeObject(new String("staff")); //notifies server of staff id (staff program connected)
		Thread receiveMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				Object received = null;
				String command = "";
				CustomerOrder order = null;
				while (true) {
					try {
						received = ois.readObject();
						if (received instanceof String) {
							command = (String) received;
							// any commands from client will always be preceded
							// by a '!' symbol
							if (command.charAt(0) == '!') {
								if (command.contains("clientconnected")) {
									clientStatusLabel.setText("Client Status: Connected");
									clientStatusLabel.setForeground(Color.GREEN);
									clientConnected = true;
									Refresh();
								}
							} else if (command.charAt(0) == '$') {
								if (command.contains("sessionend")) {
									JOptionPane.showMessageDialog(frame, "Session has been ended by the host!");
									System.exit(0);
								//server sends pending removal if client attempts to resolve an order that has
								//not already been resolved by the rest
								//splits from @ and sets color of corresponding orderMenuItem to yellow to indicate
								//pending removal
								}else if (command.contains("pendingremoval@")) {
									int toSetPending = Integer.parseInt("" + command.split("@")[1]);
									System.out.println(toSetPending);
									for(Component c : orderItemPanel.getComponents()) {
										if(c instanceof OrderMenuItem) {
											if(((OrderMenuItem) c).INSTANCE_ORDERNUM == toSetPending) {
												((OrderMenuItem)c).resolveButton.setForeground(Color.YELLOW);
											}
										}
									}
								//server sends staffclient sessionstarted if staffclient attempts to connect
								//when a session has already been started
								} else if (command.contains("sessionstarted")) {
									JOptionPane.showMessageDialog(frame,
											"Session already started! End the session from host to add additional clients.");
									System.exit(0);
								}
							}
						} else if (received instanceof CustomerOrder) {
							System.out.println("yo right here");
							order = (CustomerOrder) received;
							if (received != null) {
								//adds an ordermenuitem using OrderHandler class
								//generateOMI takes boolean argument that determines if ordermenuitem will be a
								//'filler' omi -- filler OMIs fill the screen at the beginning and are blank
								addOrderMenuItem(new OrderHandler(order).generateOMI(false));
								Refresh();
							}
						//any plain integer received from the server will represent the ordermenuitem number to be
						//removed if all staffclients have resolved the same order
						} else if (received instanceof Integer) {
							//iterates through components in panel containing ordermenuitems
							for(Component c : orderItemPanel.getComponents()) {
								//checks if component c is an instance of OrderMenuItem
								//(ordermenuitem inherits from JPanel, which is a child class of the base class Component)
								if(c instanceof OrderMenuItem) {
									//if current OMI id number corresponds to the one received from the server,
									//it will be removed from the orderitempanel
									if(((OrderMenuItem) c).INSTANCE_ORDERNUM == ((Integer)received)) {
										orderItemPanel.remove(c);
									}
								}
							}
							Refresh();
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		});
		receiveMessage.start();
	}

	static void addOrderMenuItem(OrderMenuItem omi) {
		// specifying constraints for OrderMenuItem to fit the GridBagLayout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		// code to replace any potential existing filler ordermenuitems with
		// real customer orders
		if (!clientConnected) {
			//if the client has not connected (staffclient program just launched) several filler OMIs are added
			//through this function in the main method
			orderItemPanel.add(omi, c);
		} else {
			//if the client is connected and this function is called, it will not be a filler order
			//below code iterates through components in panel containing filler OMIs and replaces any filler OMIs
			//with the current real OMI
			int componentCounter = 0;
			for (Component com : orderItemPanel.getComponents()) {
				if (((OrderMenuItem) com).isFiller) {
					orderItemPanel.remove(com);
					orderItemPanel.add(omi, c, componentCounter);
					return;
				}
				componentCounter++;
			}
			//previous for loop returns/terminates if a filler OMI is found and replaced
			//this code will only run if there is no more filler omi left, and a real OMI will just be added
			//to the existing list rather than replace any
			orderItemPanel.add(omi, c);
		}

	}

	public static void removeOrderMenuItem(OrderMenuItem toRemove) {
		for (Component c : orderItemPanel.getComponents()) {
			if ((OrderMenuItem) c == toRemove) {
				// will indicate to server that one instance of the StaffClient
				// has resolved a customer order
				// the server will receive the command and relay it to the rest
				// of the StaffClient instances to remove, all through the
				// ClientProtocol class
				try {
					oos.writeObject(new String("remove@" + ((OrderMenuItem)c).INSTANCE_ORDERNUM));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// function that will populate the orderItemPanel with filler OrderMenuItems
	// primarily for visual appeal
	//the filler OMIs are still real intances of the OrderMenuItem class, that take vectors and other data as constructor arguments
	//thus, 4 sample 'empty' vectors are generated below with a for loop and a filler OMI is added to the orderItemPanel
	//through the addOrderMenuItem function
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateDisplayArea() {
		Vector<Pair<Integer, String>> sampleVector = new Vector<Pair<Integer, String>>();
		for (int i = 0; i < 4; i++) {
			sampleVector.add(new Pair(i, "(Empty Item " + i + ")"));
		}
		for (int i = 0; i < 6; i++) {
			addOrderMenuItem(new OrderHandler(new CustomerOrder(sampleVector, 0, 0)).generateOMI(true));
		}
	}

	public static void Refresh() {
		try {
			frame.revalidate();
		} catch (NullPointerException e) {
			return;
		}

	}

	/**
	 * Create the frame.
	 */

	public StaffClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 353);
		// initializing content pane
		contentPane = new JPanel();
		contentPane.setBackground(Color.YELLOW);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridBagLayout());
		setContentPane(contentPane);

		// initializing orderItemPanel -- this is where the OrderMenuItems will
		// be
		// added
		orderItemPanel = new JPanel();
		orderItemPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		orderItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		orderItemPanel.setBounds(new Rectangle(0, 0, 500, 500));
		orderItemPanel.setBackground(Color.RED);
		orderItemPanel.setLayout(new GridBagLayout());
		populateDisplayArea();

		orderItemScrollpane = new JScrollPane(orderItemPanel);
		orderItemScrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		orderItemScrollpane.setAutoscrolls(true);
		orderItemScrollpane.getVerticalScrollBar().setPreferredSize(new Dimension(25, 0));
		GridBagConstraints scrollPanelConstraints = new GridBagConstraints();
		scrollPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		scrollPanelConstraints.fill = GridBagConstraints.BOTH;
		scrollPanelConstraints.gridx = 0;
		scrollPanelConstraints.gridy = 1;
		scrollPanelConstraints.weightx = 1;
		scrollPanelConstraints.weighty = 110;
		scrollPanelConstraints.gridheight = GridBagConstraints.REMAINDER;
		contentPane.add(orderItemScrollpane, scrollPanelConstraints);

		// panel with connection status
		panel.setBackground(Color.ORANGE);
		panel.setForeground(Color.ORANGE);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// gridbagconstraints for the panel containing connection status label
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.anchor = GridBagConstraints.NORTHWEST;
		gbcPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 0;
		gbcPanel.weighty = 5;
		gbcPanel.weightx = 1;
		contentPane.add(panel, gbcPanel);

		// label for connection status, updated through static access upon
		// socket acceptance
		clientStatusLabel = new JLabel("Client Status: Not Connected");
		clientStatusLabel.setForeground(Color.RED);
		panel.add(clientStatusLabel);
	}

}
