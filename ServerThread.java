import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerThread extends Thread{
    private String nickname = null;
    private Socket socket = null;
    ArrayList<PrintWriter> listWriters = null;
    ArrayList<String> userList = null;
    Game game = null;

    public ServerThread(Socket socket, ArrayList<PrintWriter> listWriters, ArrayList<String> userList, Game game)
    {
        this.socket = socket;
        this.listWriters = listWriters;
        this.userList = userList;
        this.game = game;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader buffereedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            while(true)
            {
                String request = buffereedReader.readLine();

                if(request == null)
                {
                	doQuit(printWriter, userList.get(listWriters.indexOf(printWriter)));
                	break;
                }

                String[] tokens = request.split(":");
                if("join".equals(tokens[0]))
                    doJoin(tokens[1], printWriter);
                else if("message".equals(tokens[0]))
                    doMessage(tokens[1]);
                else if("quit".equals(tokens[0]))
                    doQuit(printWriter, userList.get(listWriters.indexOf(printWriter)));
                else if("command".equals(tokens[0]))
                	doCommand(tokens[1], printWriter);
                else if("isConnectable".equals(tokens[0]))
                {
                	boolean isExist = false;
                	for (String x : userList)
                		if (x.equals(tokens[1]))
                			isExist = true;
                	message(printWriter, (isExist || game.getStarted()) ? "false" : "true");
                	break;
                }
            }
        }
        catch(IOException e)
        {
            
        }
    }

    private void doQuit(PrintWriter writer, String name)
    {
        removeWriter(writer, name);
        String data = "server:" + this.nickname + "���� �����߽��ϴ�.";
        broadcast(data);
        printUserList();
    }

    private synchronized void removeWriter(PrintWriter writer, String name)
    {
        listWriters.remove(writer);
        userList.remove(name);
    }

    private void doMessage(String data)
    {
        broadcast(this.nickname + ":" + data);
    }

    private void doJoin(String nickname, PrintWriter writer)
    {
        this.nickname = nickname;
        String data = "server:" + nickname + "���� �����Ͽ����ϴ�.";
        addWriter(writer, nickname);
        broadcast(data);
        printUserList();
    }

    private synchronized void addWriter(PrintWriter writer, String name)
    {
    	listWriters.add(writer);
        userList.add(name);
    }
    
    private void doCommand(String data, PrintWriter printWriter)
    {
    	int playerid = listWriters.indexOf(printWriter);
    	if (data.equals("start"))
    	{
    		if (playerid != 0)
    			message(printWriter, "server:������ �����ϴ�.");
    		else if (game.getStarted())
    			message(printWriter, "server:�̹� ������ �����߽��ϴ�.");
    		else if (userList.size() < 2)
    			message(printWriter, "server:2�� �̻���� ���ӽ����� �����մϴ�.");
    		else
    		{
    			broadcast("server:������ �����մϴ�.");
    			game.startGame(userList.size());
    			for (int i = 0; i < userList.size(); i++)
    				message(listWriters.get(i), "card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
    			printUserList();
    			broadcast("pot:" + game.getPot());
    		}
    	}
    	else if (data.equals("stop"))
    	{
    		if (playerid != 0)
    			message(printWriter, "server:������ �����ϴ�.");
    		else if (!game.getStarted())
    			message(printWriter, "server:������ ���������� �ʽ��ϴ�.");
    		else
    		{
    			broadcast("server:������ �����մϴ�.");
    			game.stopGame();
    			for (int i = 0; i < userList.size(); i++)
    				message(listWriters.get(i), "card:52x52x52x52x52x52x52");
    			printUserList();
    			broadcast("pot:-1");
    		}
    	}
    	else if (data.split(" ")[0].equals("bet"))
    	{
    		if (playerid != game.getTurn())
    			message(printWriter, "server:����� ���� �ƴմϴ�.");
    		else if (data.trim().equals("bet"))
    			message(printWriter, "server:�ùٸ� ���� �ݾ��� �Է����ּ���.");
    		else
    		{
    			int bet = Integer.parseInt(data.split("bet")[1].trim());
    			broadcast("server:" + userList.get(playerid) + "���� " + bet + "Ĩ�� �����մϴ�.");
    			game.getInfo(playerid).doBet(bet);
    			game.addPot(bet);
    			game.setMaxBet(game.getInfo(playerid).getBet());
    			if (game.getMaxBet() < game.getInfo(playerid).getBet())
    				game.setMaxBet(game.getInfo(playerid).getBet());
    			game.turnProgress();
    			printUserList();
    			broadcast("pot:" + game.getPot());
    		}
    	}
    	else if (data.equals("fold"))
    	{
    		if (playerid != game.getTurn())
    			message(printWriter, "server:����� ���� �ƴմϴ�.");
    		else
    		{
    			broadcast("server:" + userList.get(playerid) + "���� �����մϴ�.");
    			game.getInfo(playerid).setDie(true);
    			if (game.getAlive() == 1)
    			{
    				broadcast("server:��ΰ� ������ " + userList.get(game.getLastStanding()) + "���� �¸��߽��ϴ�.");
    				game.getInfo(game.getLastStanding()).setMoney(game.getInfo(game.getLastStanding()).getMoney() + game.getPot());
    				game.DiePlayerChecker();
    				game.resetGame();
        			for (int i = 0; i < userList.size(); i++)
        				message(listWriters.get(i), "card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
        			broadcast("pot:" + game.getPot());
        			checkLastManStanding();
    			}
    			else
    			{
    				game.turnProgress();
    				checkProgress();
    			}
    			printUserList();
    		}
    	}
    	else if (data.equals("check"))
    	{
    		if (playerid != game.getTurn())
    			message(printWriter, "server:����� ���� �ƴմϴ�.");
    		else
    		{
    			if (game.getInfo(playerid).getBet() == game.getMaxBet() || game.getInfo(playerid).getMoney() <= 0)
    			{
    				broadcast("server:" + userList.get(playerid) + "���� üũ�մϴ�.");
    				game.turnProgress();
    				checkProgress();
    				printUserList();
    				broadcast("pot:" + game.getPot());
    			}
    			else
    				message(printWriter, "server:üũ�� �� �����ϴ�.");
    		}
    	}
    	else if (data.equals("call"))
    	{
    		if (playerid != game.getTurn())
    			message(printWriter, "server:����� ���� �ƴմϴ�.");
    		else
    		{
    			if (game.getInfo(playerid).getBet() == game.getMaxBet())
					message(printWriter, "server:���� �� �� ���� �����Դϴ�.");
    			else
    			{
    				broadcast("server:" + userList.get(playerid) + "���� ���մϴ�.");
    				int bet = game.getMaxBet() - game.getInfo(playerid).getBet();
    				if (game.getInfo(playerid).getMoney() < bet) // ���̵� �� �߻� ���� ���� ����
    					bet = game.getInfo(playerid).getMoney();
	    			game.addPot(bet);
	    			game.getInfo(playerid).doBet(bet);
	    			game.turnProgress();
	    			checkProgress();
	    			broadcast("pot:" + game.getPot());
	    			printUserList();
    			}
    		}
    	}
    	else
    		message(printWriter, "server:�ùٸ��� ���� ��ɾ� �Դϴ�.");
    }
    
    private void checkLastManStanding()
    {
    	if (game.getTotalLastStanding() != -1)
    	{
    		broadcast("server:" + userList.get(game.getTotalLastStanding()) + "���� ���� �¸��߽��ϴ�.");
    		game.stopGame();
			for (int i = 0; i < userList.size(); i++)
				message(listWriters.get(i), "card:52x52x52x52x52x52x52");
			printUserList();
			broadcast("pot:-1");
    	}
    }
    
    private void printUserList()
    {
    	int i = 0;
    	String str = "------------------UserList------------------|";
        for (String x : userList)
        {
        	String info = "";
        	if (game.getStarted())
        	{
        		info = info + game.getInfo(i).getMoney();
        		if (game.getInfo(i).getBet() > 0)
        		{
        			if (game.getInfo(i).getMoney() > 0)
        				info = info + " bet= " + game.getInfo(i).getBet();
        			else
        				info = info + " All In";
        		}
        		else if (game.getInfo(i).getMoney() <= 0 && !game.getPlayerDie(i))
        			info = info + " All In";
        		if (game.getInfo(i).getDie())
            		info = info + " X";
        		if (game.getTurn() == i)
        			info = info + " ��";
        	}
        	if (i != 0)
        		str = str + x + " " + info + "|";
        	else
        		str = str + x + "(Host)" + " " + info + "|";
        	i++;
        }
        broadcast("userList:" + str);
    }

    private synchronized void broadcast(String data)
    {
        for(PrintWriter writer : listWriters)
            message(writer, data);
    }
    
    private synchronized void message(PrintWriter writer, String data)
    {
    	writer.println(data);
    	writer.flush();
    }
    
    private synchronized void checkProgress()
    {
    	if (game.getIsProgress())
		{
			if (game.getBetRound() == 2)
			{
				for (int i = 0; i < 3; i++)
					game.draw();
				for (int i = 0; i < userList.size(); i++)
				{
					if (game.getPlayerDie(i))
    					message(listWriters.get(i), "card:52x52x" + game.getCommunityCardInfo());
    				else
    					message(listWriters.get(i), "card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
				}
				broadcast("server:ī�带 �̽��ϴ�.");
			}
			else if (game.getBetRound() == 3)
			{
				game.draw();
				for (int i = 0; i < userList.size(); i++)
				{
					if (game.getPlayerDie(i))
    					message(listWriters.get(i), "card:52x52x" + game.getCommunityCardInfo());
    				else
    					message(listWriters.get(i), "card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
				}
				broadcast("server:ī�带 �̽��ϴ�.");
			}
			else if (game.getBetRound() == 4)
			{
				game.draw();
				for (int i = 0; i < userList.size(); i++)
				{
					if (game.getPlayerDie(i))
    					message(listWriters.get(i), "card:52x52x" + game.getCommunityCardInfo());
    				else
    					message(listWriters.get(i), "card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
				}
				broadcast("server:ī�带 �̽��ϴ�.");
			}
			else if (game.getBetRound() == 5)
			{
				int splitPot = game.getPot() / game.getWinners().size();
				int j = 0;
				for (Player x : game.getWinners())
				{
					broadcast("server:" + userList.get(game.getPlayerIDByPlayerList(x)) + "���� �¸��߽��ϴ�.");
					broadcast("server:" + game.getPlayerCardsString(game.getPlayerIDByPlayerList(x)) + " with " + game.getCommunityCardsString());
					game.getInfo(game.getPlayerIDByPlayerList(x)).setMoney(game.getInfo(game.getPlayerIDByPlayerList(x)).getMoney() + splitPot);
					if (j == 0)
						game.getInfo(game.getPlayerIDByPlayerList(x)).setMoney(game.getInfo(game.getPlayerIDByPlayerList(x)).getMoney() + game.getPot() % game.getWinners().size());
					broadcast("server:" + game.getPlayerContested(game.getPlayerIDByPlayerList(x)).getText());
					ArrayList<Card> tmp = game.getPlayerValid(game.getPlayerIDByPlayerList(x));
					String str = "";
					for (Card c : tmp)
						str = str + c.toString();
					broadcast(str);
					j++;
				}
				game.DiePlayerChecker();
				checkLastManStanding();
				game.resetGame();
				if (game.getStarted())
				{
	    			for (int i = 0; i < userList.size(); i++)
	    			{
	    				if (game.getPlayerDie(i))
	    					message(listWriters.get(i), "card:52x52x" + game.getCommunityCardInfo());
	    				else
	    					message(listWriters.get(i), "card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
	    			}
	    			broadcast("pot:" + game.getPot());
				}
			}
			game.setIsProgress(false);
		}
    }
}