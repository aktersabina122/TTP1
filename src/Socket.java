
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/*
 * This is our socket class. 
 * @author sameh
 */
public class Socket {
	
	public static enum SocketType {Broadcast, NoBroadCast};

	private int portNumber;
	private InetAddress address;
	private DatagramSocket MySocket;
	
	private ConcurrentLinkedQueue<DatagramPacket> messageQueue =
			new ConcurrentLinkedQueue<DatagramPacket>();
	
	private Thread ReceiveThread;
	private boolean ReceiveThreadRun = true;
	public int fill;
	public int gridx;
	public int gridy;
	public int gridwidth;
	
	/*
	 This is our constructor;
	 @param portNumber: Socket will use this port number.
	 */
	public Socket(int portNumber, SocketType socketType) {
		this.portNumber = portNumber;
		
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			System.exit(-1);
		}

		System.out.println("My IP_Address = " + address.getHostAddress());
	
		try {
			switch (socketType) {
			case Broadcast:
				MySocket = new DatagramSocket(portNumber);
				break;
			case NoBroadCast:
				MySocket = new DatagramSocket(portNumber, address);
				break;
			}
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(-1);
		}							
		
		//Thread will be receiving here;
		
		ReceiveThread = new Thread(
				new Runnable() {
					public void run() {
						receiveThreadMethod();
					}
				});
		ReceiveThread.setName("Receive Thread For The Port = " + this.portNumber);
		ReceiveThread.start();
	}
	
	public void receiveThreadMethod() {
		
		try {
			MySocket.setSoTimeout(40);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(-1);
		}
		
		do {
			DatagramPacket inPacket = null;
			
			byte[] inBuffer = new byte[1024];
			for ( int i = 0 ; i < inBuffer.length ; i++ ) {
				inBuffer[i] = ' ';
			}
			
			inPacket = new DatagramPacket(inBuffer, inBuffer.length);
			
			try {
				MySocket.receive(inPacket);
				messageQueue.add(inPacket);
			} catch (SocketTimeoutException ste) {
			   
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.exit(-1);
			}
			
		} while (ReceiveThreadRun);
		
		System.out.println(ReceiveThread.getName() + " is exiting!");
		
	}
	
	/**
	 * @param message
	 * @param destinationAddress
	 * @param destinationPort
	 */
	public void send(String message,
					 InetAddress destinationAddress,
					 int destinationPort) {

		byte[] outBuffer;
		outBuffer = message.getBytes();
		
		DatagramPacket outPacket = new DatagramPacket(outBuffer,
													  outBuffer.length,
													  destinationAddress,
													  destinationPort);
		
		try {
			MySocket.send(outPacket);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	/**
	 * Here, we are receiving method
	 * @return A DatagramPacket if available, otherwise do <code>null</code> 
	 */
	public DatagramPacket receive() {
		return messageQueue.poll();
	}

	public void close() {
		ReceiveThreadRun = false;
		try {
			TimeUnit.MILLISECONDS.sleep(90);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
			System.exit(-1);
		}		
		MySocket.close();
	}

	public int getPortNumber() {
		return portNumber;
	}


	public InetAddress getAddress() {
		return address;
	}
	
	
}
