import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrameWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	/*
	 Author Sabina
	 */
	private static final long SerialVersionUid = 1L;
	private JFrame Frame;
	private JPanel panel;
	private JLabel label;
	private JButton SendButton;
	private JTextField TextField;
	private JTextArea texthistory, textarea;
	private JScrollPane ScrollPane;
	private int Port;
	private InetAddress Address;

	public static void main(String[] arg) {

	}

	// Here Constructor is passing socket through the driver, string IP & int Port;

	@SuppressWarnings("null")
	public ChatFrameWindow(Socket s, String IP, int Port) {
		// turning string IP into InetAddress data type;
		this.Port = Port;
		try {
			this.Address = InetAddress.getByName(IP);
		} catch (java.net.UnknownHostException l1) {
			// doing Auto generated catch block;
			
			Throwable l11 = null;
			l11.printStackTrace();
		}

		// GridbagConstrains()
		Frame = this;
		panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.GREEN);
		Frame.setTitle("Chat");
		Frame.getContentPane().add(panel, BorderLayout.SOUTH);
		Frame.setVisible(true);
		Frame.setSize(600, 600);
		Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Frame.setResizable(true);
		//Frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		Frame.setMinimumSize(new Dimension (100,100));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 6, 6, 6);

		// Here is Server address label; It will give Horizontal look;
		label = new JLabel("IP_Address");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);

		// This is Server address TextField; It will give Horizontal look.
		TextField = new JTextField(20);
		TextField.setText(IP);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		TextField.setEditable(false);
		panel.add(TextField, c);

		// Port#: label; It will appear Horizontally;
		label = new JLabel("Port:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		panel.add(label, c);

		// Port# TextField;It will appear Horizontally;
		TextField = new JTextField(6);
		TextField.setText(Integer.toString(Port));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		TextField.setEditable(false);
		panel.add(TextField, c);

		// Conversation: label; It will appear Horizontally;
		label = new JLabel("Communication:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		panel.add(label, c);

		// Here is the Conversation Window;
		texthistory = new JTextArea(11, 2);
		ScrollPane = new JScrollPane(texthistory);
		
		// Here the lines are wrapped if they are too long to fit with the width;
		texthistory.setLineWrap(true);
		
		/*Here lines are wrapped at word boundaries (whitespace), if they are too long to
		fit with the width; texthistory.setWrapStyleWord(true); textlog will be only viewable, no typing; no clicking; no editting*/
		
		texthistory.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		panel.add(ScrollPane, s);

		// This is Message: label;It will appear Horizontally;
		label = new JLabel("Message to Send:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		panel.add(label, c);

		// Message Window; It will appear Horizontally;
		textarea = new JTextArea(2, 2);
		ScrollPane = new JScrollPane(textarea);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		panel.add(ScrollPane, c);

		// Here is the Send button; It will appear Horizontally;
		SendButton = new JButton("Send Button");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 4;

		SendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*get text from textarea. That will append to textlog area or send message typed in
				textarea to the address/port of this window*/
				
				String message = textarea.getText();

				if (!message.isEmpty()) {
					texthistory.append("You: " + message + "\n");
					textarea.setText("");
					s.send(message, Address, Port);
				}

			}

		});
		panel.add(SendButton, c);

	}

	// Here window listener to receive window events from passed window;
	
	public void addCloseEventListener(WindowListener windowListener) {
		addWindowListener(windowListener);

	}

	// separating actionlistener unused;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// do Auto generated method stub;

	}

	// To get texthistory to call textarea from other classes;
	public JTextArea gettexthistory() {
		return this.texthistory;
	}

	// To do the address getter unused;
	public InetAddress getAddress() {
		return Address;
	}

	// Here Port getter unused;
	public int getPort() {
		return Port;
	}

	public static long getSerialversionuid() {
		return SerialVersionUid;
	}

}

