import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.awt.Image;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Room extends JFrame implements ActionListener{
	
	private static final int WIDTH = 535;
	private static final int HEIGHT = 420;
	private Socket socket;
	private JTextArea userList;
	private JTextArea textArea;
	private JTextField textField;
	private JLabel[] cards;
	private JLabel myHand;
	private JLabel comHand;
	private ImageIcon[] icons;
	private JLabel[] cardText;
	public Room(Socket socket)
	{
		this.socket = socket;
		icons = new ImageIcon[53];
		for (int i = 0; i < 53; i++)
			icons[i] = new ImageIcon(new ImageIcon(this.getClass().getResource("/image/" + i + ".png")).getImage().getScaledInstance(55, 75, Image.SCALE_DEFAULT));
		
		new ChatClientReceiveThread(socket).start();
		setTitle("Poker");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		userList = new JTextArea();
		userList.setEditable(false);
		JScrollPane scrollPane1 = new JScrollPane(userList);
		userList.append("-------UserList-------\n");
		scrollPane1.setBounds(400, 10, 110, 190);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane2 = new JScrollPane(textArea);
		scrollPane2.setBounds(10, 240, 500, 110);
		
		textField = new JTextField("",30);
		textField.setBounds(10, 350, 500, 20);
		textField.addKeyListener(new KeyAdapter()
		{
            public void keyReleased(KeyEvent e)
            {
                char keyCode = e.getKeyChar();
                if (keyCode == KeyEvent.VK_ENTER && !textField.getText().isEmpty())
                    sendMessage();
            }
        });
		
		myHand = new JLabel("My Hands");
		myHand.setBounds(10, 5, 80, 20);
		
		comHand = new JLabel("Community Cards");
		comHand.setBounds(10, 120, 120, 20);
		
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
		
		add(myHand);
		add(comHand);
		add(scrollPane1);
		add(scrollPane2);
		add(textField);
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
		if (actionCmd.equals(""))
		{

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
            	textArea.append("error:올바르지 않은 명령어 입니다.");
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
	            	else
	            	{
		                textArea.append(msg);
		                textArea.append("\n");
		                textArea.setCaretPosition(textArea.getDocument().getLength());  // 맨아래로 스크롤한다.
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