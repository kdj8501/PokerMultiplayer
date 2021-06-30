import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Server extends Thread{
	
	private int port;
    public Server(int port)
    {
    	this.port = port;
    }
    
    public void run()
	{
		ServerSocket serverSocket = null;
        ArrayList<PrintWriter> listWriters = new ArrayList<PrintWriter>();
        ArrayList<String> userList = new ArrayList<String>();
        Game game = new Game();
        try
        {
            serverSocket = new ServerSocket();
            serverSocket.bind( new InetSocketAddress("0.0.0.0", port) );

            while(true)
            {
                Socket socket = serverSocket.accept();
                new ServerThread(socket, listWriters, userList, game).start();
            }
        }
        catch (IOException e)
        {
        	JOptionPane.showMessageDialog(null, "이미 개방된 포트입니다. 심각한 에러이므로 열려있는 모든 앱을 종료해 주세요.", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        finally
        {
            try
            {
                if( serverSocket != null && !serverSocket.isClosed() )
                    serverSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
	}
}