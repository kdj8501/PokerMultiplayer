import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Room extends JFrame implements ActionListener{
	
	private static final int WIDTH = 535;
	private static final int HEIGHT = 440;
	private Socket socket;
	private JTextArea userList;
	private JTextArea textArea;
	private JTextField textField;
	private JLabel[] cards;
	private JLabel myHand;
	private JLabel comHand;
	private JLabel pot;
	private ImageIcon[] icons;
	private JLabel[] cardText;
	public Room(Socket socket, String nickname)
	{
		this.socket = socket;
		icons = new ImageIcon[53];
		for (int i = 0; i < 53; i++)
			icons[i] = new ImageIcon(new ImageIcon(this.getClass().getResource("/image/" + i + ".png")).getImage().getScaledInstance(55, 75, Image.SCALE_DEFAULT));
		
		new ChatClientReceiveThread(socket).start();
		setTitle("Poker (" + nickname + ")");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		userList = new JTextArea();
		userList.setEditable(false);
		JScrollPane scrollPane1 = new JScrollPane(userList);
		scrollPane1.setBounds(315, 10, 195, 210);
		
		JLabel cmdHelp = new JLabel("베팅은 채팅창에 /bet [금액]");
		cmdHelp.setBounds(10, 240, 250, 20);
		cmdHelp.setForeground(Color.blue);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane2 = new JScrollPane(textArea);
		scrollPane2.setBounds(10, 260, 500, 110);
		
		textField = new JTextField("",30);
		textField.setBounds(10, 370, 500, 20);
		textField.addKeyListener(new KeyAdapter()
		{
            public void keyReleased(KeyEvent e)
            {
                char keyCode = e.getKeyChar();
                if (keyCode == KeyEvent.VK_ENTER && !textField.getText().isEmpty())
                    sendMessage();
            }
        });
		
		myHand = new JLabel("My Hand");
		myHand.setBounds(10, 5, 80, 20);
		
		comHand = new JLabel("Community Cards");
		comHand.setBounds(10, 120, 120, 20);
		
		pot = new JLabel("");
		pot.setBounds(200, 110, 80, 20);
		
		cardText = new JLabel[7];
		cards = new JLabel[7];
		for (int i = 0; i < 7; i++)
		{
			cards[i] = new JLabel();
			cards[i].setIcon(icons[52]);
			cardText[i] = new JLabel("");
			if (i < 2)
			{
				cards[i].setBounds(10 + 60 * i, 25, 55, 75);
				cardText[i].setBounds(28 + 60 * i, 100, 40, 20);
			}
			else
			{
				cards[i].setBounds(10 + 60 * (i - 2), 140, 55, 75);
				cardText[i].setBounds(28 + 60 * (i - 2), 215, 40, 20);
			}
			add(cards[i]);
			add(cardText[i]);
		}
		
		JButton checkBtn = new JButton("체크");
		checkBtn.setBounds(130, 25, 55, 75);
		checkBtn.setActionCommand("Check");
		checkBtn.addActionListener(this);
		checkBtn.setFont(new Font("돋움", Font.PLAIN, 10));
		JButton callBtn = new JButton("콜");
		callBtn.setBounds(190, 25, 55, 75);
		callBtn.setActionCommand("Call");
		callBtn.addActionListener(this);
		callBtn.setFont(new Font("돋움", Font.PLAIN, 11));
		JButton foldBtn = new JButton("폴드");
		foldBtn.setBounds(250, 25, 55, 75);
		foldBtn.setActionCommand("Fold");
		foldBtn.addActionListener(this);
		foldBtn.setFont(new Font("돋움", Font.PLAIN, 10));
		
		add(myHand);
		add(comHand);
		add(pot);
		add(cmdHelp);
		add(scrollPane1);
		add(scrollPane2);
		add(textField);
		add(checkBtn);
		add(callBtn);
		add(foldBtn);
		setVisible(true);
		addWindowListener(new WindowAdapter()
		{
            public void windowClosing(WindowEvent e)
            {
                PrintWriter pw;
                try
                {
                    pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                    String request = "quit\r\n";
                    pw.println(request);
                    System.exit(0);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String actionCmd = e.getActionCommand();
		if (actionCmd.equals("Call"))
		{
			PrintWriter pw;
	        try
	        {
	            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
	            String request;
	            request = "command:call";
	            pw.println(request);
	        }
	        catch (IOException g)
	        {
	            g.printStackTrace();
	        }
		}
		else if (actionCmd.equals("Check"))
		{
			PrintWriter pw;
	        try
	        {
	            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
	            String request;
	            request = "command:check";
	            pw.println(request);
	        }
	        catch (IOException g)
	        {
	            g.printStackTrace();
	        }
		}
		else if (actionCmd.equals("Fold"))
		{
			PrintWriter pw;
	        try
	        {
	            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
	            String request;
	            request = "command:fold";
	            pw.println(request);
	        }
	        catch (IOException g)
	        {
	            g.printStackTrace();
	        }
		}
	}
	
	private void sendMessage()
	{
        PrintWriter pw;
        try
        {
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            String message = textField.getText();
            String request;
            if (message.equals("/"))
            {
            	textArea.append("server:올바르지 않은 명령어 입니다.");
                textArea.append("\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
            else
            {
            	if (message.charAt(0) == '/')	
            		request = "command:" + message.substring(1);
            	else
            		request = "message:" + message + "\r\n";
            	pw.println(request);
            }

            textField.setText("");
            textField.requestFocus();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	
	private class ChatClientReceiveThread extends Thread
	{
	    Socket socket = null;
	    ChatClientReceiveThread(Socket socket)
	    {
	    	this.socket = socket;
	    }
	    public void run()
	    {
	        try
	        {
	        	BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
	            while(true)
	            {
	            	String msg = br.readLine();
	            	if (msg.split(":")[0].equals("userList"))
	            	{
	            		String str = msg.split(":")[1].replace("|", "\n");
	            		userList.setText(str);
	            	}
	            	else if (msg.split(":")[0].equals("card"))
	            	{
	            		int cardInfo;
	            		String cardShape;
	            		String cardNumber;
	            		for (int i = 0; i < 7; i++)
	            		{
	            			cardInfo = Integer.parseInt(msg.split(":")[1].split("x")[i]);
	            			if (cardInfo / 13 == 0)
	            			{
	            				cardShape = "♠";
	            				cardText[i].setForeground(Color.black);
	            			}
	            			else if (cardInfo / 13 == 1)
	            			{
	            				cardShape = "◆";
	            				cardText[i].setForeground(Color.red);
	            			}
	            			else if (cardInfo / 13 == 2)
	            			{
	            				cardShape = "♥";
	            				cardText[i].setForeground(Color.red);
	            			}
	            			else
	            			{
	            				cardShape = "♣";
	            				cardText[i].setForeground(Color.black);
	            			}
	            			if (cardInfo % 13 + 1 == 1)
	            				cardNumber = "A";
	            			else if (cardInfo % 13 + 1 == 11)
	            				cardNumber = "J";
	            			else if (cardInfo % 13 + 1 == 12)
	            				cardNumber = "Q";
	            			else if (cardInfo % 13 + 1 == 13)
	            				cardNumber = "K";
	            			else
	            				cardNumber = Integer.toString(cardInfo % 13 + 1);
	            			cards[i].setIcon(icons[cardInfo]);
	            			if (cardInfo != 52)
	            				cardText[i].setText(cardShape + cardNumber);
	            			else
	            				cardText[i].setText("");
	            		}
	            	}
	            	else if (msg.split(":")[0].equals("pot"))
	            	{
	            		if (msg.split(":")[1].equals("-1"))
	            			pot.setText("");
	            		else
	            			pot.setText("pot: " + msg.split(":")[1]);
	            	}
	            	else
	            	{
		                textArea.append(msg);
		                textArea.append("\n");
		                textArea.setCaretPosition(textArea.getDocument().getLength());
	            	}
	            }
	        }
	        catch (IOException e)
	        {
	            JOptionPane.showMessageDialog(null, "방 정보를 찾을 수 없습니다.", "Error", JOptionPane.INFORMATION_MESSAGE);
	            dispose();
	        }
	    }
	}

}