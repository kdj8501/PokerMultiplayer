import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Check extends JFrame{
	
	private static final int WIDTH = 300;
	private static final int HEIGHT = 100;
	
	public Check(Socket socket, String name, String IP, int port)
	{
		new ChatClientReceiveThread(socket, name, IP, port).start();
		setTitle("Checking...");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
	}
	
	private class ChatClientReceiveThread extends Thread
	{
		Socket socket = null;
	    String name = "";
	    String IP = "";
	    int port = 0;
	    ChatClientReceiveThread(Socket socket, String name, String IP, int port)
	    {
	    	this.socket = socket;
	    	this.name = name;
	    	this.IP = IP;
	    	this.port = port;
	    }
	    public void run()
	    {
	        try
	        {
	        	Socket newSocket = new Socket();
	        	newSocket.connect(new InetSocketAddress(IP, port));
	        	BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
	            while(true)
	            {
	            	String msg = br.readLine();
	            	if (msg.equals("true"))
	            	{
	            		new Room(newSocket);
	            		PrintWriter pw = new PrintWriter(new OutputStreamWriter(newSocket.getOutputStream(), StandardCharsets.UTF_8), true);
	    	            String request = "join:" + name + "\r\n";
	    	            pw.println(request);
	    	            dispose();
	            	}
	            	else if(msg.equals("false"))
	            	{
	            		new MainMenu();
	            		JOptionPane.showMessageDialog(null, "접속할 수 없습니다. (닉네임 중복 or 이미 시작한 방)", "Error", JOptionPane.INFORMATION_MESSAGE);
	    	            dispose();
	            	}
	            }
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	            dispose();
	        }
	    }
	}

}