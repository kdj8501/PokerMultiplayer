import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
public class MainMenu extends JFrame implements ActionListener{
	
	private static final int WIDTH = 265;
	private static final int HEIGHT = 230;
	
	JTextField nickText;
	JTextField ipText;
	JTextField portText;
	public MainMenu()
	{
		setTitle("Poker");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		JLabel title = new JLabel("Poker V.1.5");
		title.setBounds(40, 20, 100, 20);
		
		JLabel nickLabel = new JLabel("Nickname:");
		nickLabel.setBounds(10, 60, 60, 20);
		nickText = new JTextField("Nickname", 50);
		nickText.setBounds(75, 60, 165, 20);
		
		JLabel ipLabel = new JLabel("IP:");
		ipLabel.setBounds(10, 90, 15, 20);
		ipText = new JTextField("", 50);
		ipText.setBounds(30, 90, 100, 20);
		
		JLabel portLabel = new JLabel("PORT:");
		portLabel.setBounds(10, 120, 40, 20);
		portText = new JTextField("5000", 20);
		portText.setBounds(50, 120, 40, 20);
		
		JButton cntBtn = new JButton("Connect");
		cntBtn.setBounds(140, 90, 100, 50);
		cntBtn.addActionListener(this);
		
		JButton rmBtn = new JButton("Create Room");
		rmBtn.setBounds(10, 150, 230, 30);
		rmBtn.addActionListener(this);
		
		add(title);
		add(nickLabel);
		add(nickText);
		add(ipLabel);
		add(ipText);
		add(portLabel);
		add(portText);
		add(cntBtn);
		add(rmBtn);
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		String actionCmd = e.getActionCommand();
		if (actionCmd.equals("Connect"))
		{
			if (nickText.getText().isEmpty() || nickText.getText().isBlank())
				JOptionPane.showMessageDialog(null, "닉네임을 입력해 주세요.", "Error", JOptionPane.INFORMATION_MESSAGE);
			else if (portText.getText().isEmpty() || portText.getText().isBlank())
				JOptionPane.showMessageDialog(null, "포트를 입력해 주세요.", "Error", JOptionPane.INFORMATION_MESSAGE);
			else
			{
		        Socket socket = new Socket();
		        try
		        {
					if (ipText.getText().isEmpty() || ipText.getText().isBlank())
						ipText.setText("127.0.0.1");
		            socket.connect(new InetSocketAddress(ipText.getText(), Integer.parseInt(portText.getText())));
		            new Check(socket, nickText.getText(), ipText.getText(), Integer.parseInt(portText.getText()));
		            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
		            String request = "isConnectable:" + nickText.getText() + "\r\n";
		            pw.println(request);
	        		dispose();
		        }
		        catch (IOException g)
		        {
		        	JOptionPane.showMessageDialog(null, "접속할 수 없습니다.", "Error", JOptionPane.INFORMATION_MESSAGE);
		        }
			}
		}
		else if (actionCmd.equals("Create Room"))
		{
			if (nickText.getText().isEmpty() || nickText.getText().isBlank())
			{
				JOptionPane.showMessageDialog(null, "닉네임을 입력해 주세요.", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
		        Socket socket = new Socket();
		        new Server(Integer.parseInt(portText.getText())).start();
		        Timer m_timer = new Timer();
		        TimerTask m_task = new TimerTask()
		        {
		        	public void run()
		        	{
		        		try
		    		    {
				        	socket.connect(new InetSocketAddress("127.0.0.1", Integer.parseInt(portText.getText())));
					        new Room(socket, nickText.getText());
					        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
			    	        String request = "join:" + nickText.getText() + "\r\n";
			    	        pw.println(request);
				        	dispose();
		    		    }
		        		catch (IOException g)
		        		{
		        			JOptionPane.showMessageDialog(null, "접속할 수 없습니다.", "Error", JOptionPane.INFORMATION_MESSAGE);
		        		}
		        	}
		        };
		        m_timer.schedule(m_task, 100);
		    }
		}
	}
	
	public static void main(String args[])
	{
		new MainMenu();
	}
}