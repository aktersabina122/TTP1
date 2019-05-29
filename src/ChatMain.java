import java.rmi.UnknownHostException;

import javax.swing.JFrame;

public class ChatMain {
	private static final int PortNumber = 64000;
	/*
	 The driver include declaring a personal socket & start a ChatMain window;
	 passing the socket we just declared, also continue receiving new packet as
	 long as the ChatMain window is open and a packet is received //(checked
	 for how we handle received package in ChatMain receivePackets;
	 */
	public static void main(String[] args) throws UnknownHostException, java.net.UnknownHostException {

		Socket mySocket = new Socket(PortNumber, Socket.SocketType.NoBroadCast);

		@SuppressWarnings("unused")
		ChatClient Start_Chat = new ChatClient(mySocket);
            //Receiving packets:
		do {
			ChatClient.RecievePackets();
		} while (true);
	}

}