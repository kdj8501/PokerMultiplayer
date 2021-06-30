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
            
            int i = 0;
            for (PrintWriter x : listWriters)
            {
            	if (x == printWriter)
            		break;
            	i++;
            }

            while(true)
            {
                String request = buffereedReader.readLine();

                if(request == null)
                {
                	doQuit(printWriter, userList.get(i));
                	break;
                }

                String[] tokens = request.split(":");
                if("join".equals(tokens[0]))
                    doJoin(tokens[1], printWriter);
                else if("message".equals(tokens[0]))
                    doMessage(tokens[1]);
                else if("quit".equals(tokens[0]))
                    doQuit(printWriter, userList.get(i));
                else if("command".equals(tokens[0]))
                	doCommand(tokens[1]);
                else if("isConnectable".equals(tokens[0]))
                {
                	boolean isExist = false;
                	for (String x : userList)
                		if (x.equals(tokens[1]))
                			isExist = true;
                	printWriter.println((isExist || game.getStarted()) ? "false" : "true");
                	printWriter.flush();
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
        String data = "server:" + this.nickname + "님이 퇴장했습니다.";
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
        String data = "server:" + nickname + "님이 입장하였습니다.";
        addWriter(writer, nickname);
        broadcast(data);
        printUserList();
    }

    private synchronized void addWriter(PrintWriter writer, String name)
    {
    	listWriters.add(writer);
        userList.add(name);
    }
    
    private void doCommand(String data)
    {
    	if (data.equals("start"))
    	{
    		if (!game.getStarted())
    		{
    			broadcast("server:게임을 시작합니다.");
    			game.startGame(userList.size());
    			for (int i = 0; i < userList.size(); i++)
    			{
    				listWriters.get(i).println("card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
    				listWriters.get(i).flush();
    			}
    		}
    	}
    	else if (data.equals("draw"))
    	{
    		if (game.getStarted())
    		{
    			if (game.getCommunitySize() < 5)
    			{
    				broadcast("server:카드를 한 장 뽑습니다.");
	    			game.draw();
	    			for (int i = 0; i < userList.size(); i++)
	    			{
	    				listWriters.get(i).println("card:" + game.getPlayerCardInfo(i) + "x" + game.getCommunityCardInfo());
	    				listWriters.get(i).flush();
	    			}
	    			if (game.getCommunitySize() == 5)
	    			{
	    				broadcast("server:" + userList.get(game.getWinnerIndex()) + "님이 승리하였습니다.");
	    				broadcast("server:" + game.getWinnerContested().getText());
	    				ArrayList<Card> tmp = game.getWinnerValid();
	    				for (Card x : tmp)
	    					broadcast("server:" + x.toString());
	    			}
    			}
    		}
    	}
    	else if (data.equals("stop"))
    	{
    		if (game.getStarted())
    		{
    			broadcast("server:게임을 종료합니다.");
    			game.stopGame();
    			for (int i = 0; i < userList.size(); i++)
    			{
    				game.addPlayerList();
    				listWriters.get(i).println("card:52x52x52x52x52x52x52");
    				listWriters.get(i).flush();
    			}
    		}
    	}
    }
    
    private void printUserList()
    {
    	int i = 0;
    	String str = "-------UserList-------|";
        for (String x : userList)
        {
        	if (i != 0)
        		str = str + x + "|";
        	else
        		str = str + x + "(Host)|";
        	i++;
        }
        broadcast("userList:" + str);
    }

    private synchronized void broadcast(String data)
    {
        for(PrintWriter writer : listWriters)
        {
            writer.println(data);
            writer.flush();
        }
    }
}