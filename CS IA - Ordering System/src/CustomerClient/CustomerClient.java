package CustomerClient;

import java.awt.CardLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DataStructures.CustomerOrder;
import DataStructures.ItemInfo;

public class CustomerClient extends JFrame{

	public static JPanel cardLayoutPane;

	private static CustomerClient frame;
	
	private static CartScreen cartScreen;
	private static ItemSelectScreen selectionScreen;
	private static CustomerNumberInput idInput;
	
	static ObjectOutputStream oos = null;
	static ObjectInputStream ois = null;
	
	public static int customerNumber = 0;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		try {
			frame = new CustomerClient();
			frame.setVisible(true);
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Host machine (that is running Server version) name is JeepClubCafe-PC, and the ServerSocket was
		//instantiated on port 4444
		Socket s = new Socket(InetAddress.getByName("localhost"), 4444);
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());
		//server version expecting to receive client type as first object
		oos.writeObject(new String("client"));
		
		Thread receiveMessage = new Thread(new Runnable() {

			@Override
			public void run() {
				Object received = null;
				while(true) {
					try {
						received = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(received instanceof String[]) {
						//any String[] received can only be the menu array, so it is passed to populateSelectionScreen,
						//which parses it and translated it to GUI, hence revalidating and repainting the frame
						selectionScreen.populateSelectionScreen((String[]) received);
						frame.revalidate();
						frame.repaint();
					}else if(received instanceof String) {
						//commands preceded by '$'
						if(((String) received).charAt(0) == '$') {
							String command = ((String)received).substring(1);
							if(command.equals("sessionend")) {
								JOptionPane.showMessageDialog(frame, "Session has been ended by the host!");
								break;
							}else if(command.equals("sessionstarting")) {
								try {
									oos.writeObject(new String("!clientconnected"));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			
		});
		receiveMessage.start();
		}
	
	public static void finalizeOrder(CustomerOrder toSend) {
		try {
			oos.writeObject((CustomerOrder)toSend);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//the resetComponentsToDefault function will trigger a series of functions that ultimately clear the cart
		//both programmatically and in the ui/display
		showPanel("first");
		selectionScreen.resetComponentsToDefault();
	}
	
	public static void updateCart(ItemInfo item) {
		cartScreen.updateCart(item);
		selectionScreen.updateCartQuantityDisplay(cartScreen.getQuantity());
	}
	public static void synchMainMenuWithCart(ItemInfo item) {
		selectionScreen.synchItemQuantityFromCart(item);
	}
	public CustomerClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		cardLayoutPane = new JPanel();
		cardLayoutPane.setLayout(new CardLayout());
		
		idInput = new CustomerNumberInput();
		cartScreen = new CartScreen();
		selectionScreen = new ItemSelectScreen();
		
		setContentPane(cardLayoutPane);		
		
		this.add(idInput, "first");
		this.add(selectionScreen, "main");
		this.add(cartScreen, "cart");
		
		
		CardLayout cl = (CardLayout)cardLayoutPane.getLayout();
		cl.show(cardLayoutPane, "first");
	}
	public static void populateCartDisplay() {
		cartScreen.populateCartDisplay();
	}
	public static void showPanel(String panelName) {
		CardLayout getLayout = (CardLayout)cardLayoutPane.getLayout();
		getLayout.show(cardLayoutPane, panelName);
		refreshContentPane();
	}
	public static void refreshContentPane() {
		cardLayoutPane.revalidate();
		cardLayoutPane.repaint();
	}

}
