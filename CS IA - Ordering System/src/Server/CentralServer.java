package Server;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class CentralServer extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	public static Vector<ClientProtocol> clients = new Vector<ClientProtocol>();
	static int staffClientCounter = 0;
	static int customerClientCounter = 0;

	private static boolean SESSION_STARTED = false;

	public static CentralServer frame;

	public static void main(String[] args) throws IOException {
		try {
			frame = new CentralServer();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServerSocket ss = new ServerSocket(4444);

		while (true) {
			Socket s = ss.accept();
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			// if a new client attempts to connect once the session has already
			// started,
			// they will be notified accordingly
			if (SESSION_STARTED) {
				oos.writeObject(new String("$sessionstarted"));
				continue;
			}
			Object received = null;
			try {
				received = ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			String name = "";
			if (received instanceof String) {
				name = (String) received;
			}
			if (name.equals("staff")) {
				staffClientCounter++;
				name += staffClientCounter;
				clients.add(new StaffProtocol(name, s, ois, oos));
			} else {
				customerClientCounter++;
				name += customerClientCounter;
				clients.add(new CustomerProtocol(name, s, ois, oos));
			}
			System.out.println(name + " connected");
			addClientWidget(name);
			new Thread(clients.lastElement()).start();
		}
	}

	public static void addClientWidget(String name) {
		GridBagConstraints cwConstraints = new GridBagConstraints();
		cwConstraints.gridx = GridBagConstraints.RELATIVE;
		cwConstraints.gridy = (name.contains("staff") ? 1 : 2);
		cwConstraints.anchor = GridBagConstraints.WEST;
		cwConstraints.weightx = 1;
		cwConstraints.weighty = 1;
		contentPane.add(new ClientWidget(name), cwConstraints);
		frame.revalidate();

	}
	
	public static void removeClient(String name) {
		for(Component c : contentPane.getComponents()) {
			if(c instanceof ClientWidget) {
				if(((ClientWidget)c).getName().equals(name)) {
					contentPane.remove(c);
					break;
				}
			}
		}
		for(Iterator<ClientProtocol>iterator = clients.iterator(); iterator.hasNext();) {
			ClientProtocol cur = iterator.next();
			if(cur.name.equals(name)) {
				iterator.remove();
				break;
				//if(cur instanceof StaffProtocol) {
					//staffClientCounter--;
				//}
			}
		}
		frame.repaint();
	}
	
	public static JButton startSessionBtn;
	public static JLabel sessionStatus;

	public CentralServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridBagLayout());

		sessionStatus = new JLabel("Session Status: Inactive");
		GridBagConstraints gbc_sessionStatus = new GridBagConstraints();
		gbc_sessionStatus.insets = new Insets(0, 0, 5, 5);
		gbc_sessionStatus.gridx = 0;
		gbc_sessionStatus.gridy = 0;
		contentPane.add(sessionStatus, gbc_sessionStatus);
		JLabel staffLabel = new JLabel("Staff connected: ");
		GridBagConstraints connectedStatusLabels = new GridBagConstraints();
		connectedStatusLabels.anchor = GridBagConstraints.WEST;
		connectedStatusLabels.weighty = 1;
		connectedStatusLabels.gridx = 0;
		connectedStatusLabels.gridy = 1;
		contentPane.add(staffLabel, connectedStatusLabels);
		GridBagConstraints cstmrLblConstraints = new GridBagConstraints();
		JLabel customerLabel = new JLabel("Clients connected: ");
		cstmrLblConstraints.anchor = GridBagConstraints.WEST;
		cstmrLblConstraints.weighty = 1;
		cstmrLblConstraints.gridx = 0;
		cstmrLblConstraints.gridy = 2;
		contentPane.add(customerLabel, cstmrLblConstraints);

		startSessionBtn = new JButton("Start Session");
		startSessionBtn.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_startSessionBtn = new GridBagConstraints();
		gbc_startSessionBtn.weighty = 1.0;
		gbc_startSessionBtn.weightx = 1.0;
		gbc_startSessionBtn.anchor = GridBagConstraints.SOUTHEAST;
		gbc_startSessionBtn.gridx = 9;
		gbc_startSessionBtn.gridy = 8;
		contentPane.add(startSessionBtn, gbc_startSessionBtn);

		startSessionBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (!SESSION_STARTED) {
					startSessionBtn.setText("Stop Session");
					sessionStatus.setText("Session Status: Active");
					sessionStatus.setForeground(Color.GREEN);
					frame.revalidate();
					for (int i = 0; i < 2; i++) {
						for (ClientProtocol client : clients) {
							client.send(new String("$sessionstarting"));
						}
					}
					SESSION_STARTED = true;
				} else {
					for (Component c : contentPane.getComponents()) {
						if (c instanceof ClientWidget) {
							contentPane.remove((ClientWidget) c);
						}
					}
					for (ClientProtocol clp : clients) {
						clp.send(new String("$sessionend"));
						clp.closeConnection();
					}
					frame.repaint();
					sessionStatus.setText("Session Status: Ended. Close and relaunch to start a new session.");
					sessionStatus.setForeground(Color.RED);
				}

			}
		});

	}

}
