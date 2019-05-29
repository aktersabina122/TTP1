
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.net.DatagramPacket;
import java.rmi.UnknownHostException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements ActionListener {
	/**
	 * Author: Sabina Akter
	 */
	private static final long SerialVersionUid = 1L;
	private static Socket MySocket;
	private JFrame Frame;
	private JPanel panel;
	private JLabel IPlabel, portlabel;
	private JButton SendButton;
	private JTextField IPtxtField, PORTTextField;

	/* declaring HashMap */
	
	private static HashMap<String, ChatFrameWindow> Client_table = new HashMap<>();

	/*Constructor for ChatClient pass through the socket;
    Include IP/Port label/JTextField using a GridBagConstraints;
	GridBagConstrain has a square sized 5 units each face (line 46);
	decide where the component should be using gridx and gridy, (0,0) is the top left;
	and (5,5) is the bottom right*/
	
	public ChatClient(Socket s) {
		
		ChatClient.MySocket = s;

		Frame = this;
		panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.LIGHT_GRAY);
		Frame.setTitle("Sending a message: ");
		
		//Frame.setForeground(getForeground());
		
		Frame.getContentPane().add(panel, BorderLayout.NORTH);

		Frame.setSize(600, 464);
		//Frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  //maximizing windows;

		// Frame.setResizable(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 6, 6, 6);

		// IP address label;
		
		IPlabel = new JLabel("Host_Address: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(IPlabel, c);

		// IP address TextField;
		
		IPtxtField = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(IPtxtField, c);

		// About 'Port#:' label;
		
		portlabel = new JLabel("Port :");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		panel.add(portlabel, c);

		// About Port# TextField
		
		PORTTextField = new JTextField(6);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		panel.add(PORTTextField, c);

		// About 'Send' button
		
		SendButton = new JButton("Send");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 4;
		
		// sending button action listener;
		SendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ip = IPtxtField.getText();
				String portString = PORTTextField.getText();
				
				// key is HashMap key; It is calculated by ip:port number in string;
				String key = ip + ":" + portString;
				
				
				/* checking if ip/port which was filled in ChatClient text field is not empty &
	
				checking if key calculated from above ip/port is not already in the hashtable */
				
				if (!ip.isEmpty() && !portString.isEmpty() && !Client_table.containsKey(key)) {

					ChatFrameWindow chat = null;
					try {
						chat = new ChatFrameWindow(MySocket, ip, Integer.parseInt(portString));
					} catch (NumberFormatException e1) {
						
						// It will do Auto generated catch block;
						
						e1.printStackTrace();
					}
					chat.addCloseEventListener(new WindowListener() {

						/*
						 public void windowOpened(WindowEvent e) { // to do Auto-generated method stub;
						 public void windowClosing(WindowEvent e) { Client_table.remove(key); }
						 */
						@Override
						public void windowActivated(java.awt.event.WindowEvent e) {
							// do Auto generated method stub;

						}

						@Override
						public void windowClosed(java.awt.event.WindowEvent e) {
							/*doing Auto generated method stub;
							ExitAction.getInstance().actionPerformed(null);
							dispose();*/

						}

						@Override
						public void windowClosing(java.awt.event.WindowEvent e) {
							// doing Auto generated method stub;
							Client_table.remove(key);
						}

						@Override
						public void windowDeactivated(java.awt.event.WindowEvent e) {
							// doing Auto generated method stub;

						}

						@Override
						public void windowDeiconified(java.awt.event.WindowEvent e) {
							// doing Auto generated method stub;

						}

						@Override
						public void windowIconified(java.awt.event.WindowEvent e) {
							// doing Auto generated method stub;

						}

						@Override
						public void windowOpened(java.awt.event.WindowEvent e) {
							// doing Auto generated method stub;

						}

					});

					Client_table.put(key, chat);

					IPtxtField.setText("");
					PORTTextField.setText("");
				}

			}

		});
		panel.add(SendButton, c);

		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setVisible(true);
	}

	// separated actionlistener unused;
	
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		String btnClicked = e.getActionCommand();
		if (btnClicked.equalsIgnoreCase("Send")) {

		}

	}

	// geting the ip from IPtxtField as a String unused;
	public String getIPstring() {
		return IPtxtField.getText();
	}

	// getting the port number from PORTTextField as a String unused;
	public String getPORTstring() {
		return PORTTextField.getText();
	}
	// creating in package to handle receivedPackets;

	public static void recievePackets() throws UnknownHostException, java.net.UnknownHostException {
		DatagramPacket inPacket = null;

		do {
			try {
				inPacket = MySocket.receive();
				
				// if packet exist, get message, get ip, get port and finally calculate hash key;
				if (inPacket != null) {
					String message = new String(inPacket.getData());
					String ip = inPacket.getAddress().getHostAddress();
					int port = inPacket.getPort();
					String key = ip + ":" + port;

					ChatFrameWindow chat = null;
					
					/* if Hashtable Client_table contain the key then use that info to set visible the
					respective ChatFrameWindow and append a new message to the textlogarea*/
					
					if (Client_table.containsKey(key)) {
						chat = Client_table.get(key);
						chat.gettexthistory().append("Recipient: " + message + "\n");
						chat.setVisible(true);
						
						/* else make new ChatFrameWindow and pass in the hashtable new ChatFrameWindow and its
						calculated key*/
						
					} else {
						chat = new ChatFrameWindow(MySocket, ip, port);
						Client_table.put(key, chat);
						chat.gettexthistory().append("Recipient: " + message + "\n");
						chat.setVisible(true);
					}
				}
			} catch (NullPointerException npe) {
				// to do nothing
			}
		} while (true);
	}

	public static void RecievePackets() {
		// to do Auto-generated method stub
		
	}

}
