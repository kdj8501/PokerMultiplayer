import java.util.ArrayList;

public class Game {
	private boolean started;
	private Deck deck;
	private ArrayList<Player> playerList;
	private Community community;
	
	public Game()
	{
		deck = new Deck();
		playerList = new ArrayList<Player>();
		community = new Community();
		started = false;
	}
	
	public boolean getStarted() { return started; }
	
	public void startGame(int n)
	{
		started = true;
		playerList.clear();
		for (int i = 0; i < n; i++)
			addPlayerList();
	}
	
	public void stopGame()
	{
		started = false;
		deck.resetDeck();
		community.reset();
	}
	
	public void draw()
	{
		if (community.getSize() < 5)
			community.addCard(deck.drawCard());
	}
	
	public int getCommunitySize() { return community.getSize(); }
	
	public void addPlayerList()
	{
		playerList.add(new Player(deck.drawCard(), deck.drawCard()));
	}
	
	public String getPlayerCardInfo(int i)
	{
		String str = "";
		int idx;
		idx = playerList.get(i).getHand1().getShape() * 13 + playerList.get(i).getHand1().getNumber() - 1;
		str = Integer.toString(idx);
		idx = playerList.get(i).getHand2().getShape() * 13 + playerList.get(i).getHand2().getNumber() - 1;
		str = str + "x" + Integer.toString(idx);
		return str;
	}
	
	public String getCommunityCardInfo()
	{
		String result;
		String cardInfo[] = new String[5];
		for (int i = 0; i < 5; i++)
			cardInfo[i] = Integer.toString(52);
		ArrayList<Card> tmp = community.getCards();
		for (int i = 0; i < tmp.size(); i++)
		{
			int idx = tmp.get(i).getShape() * 13 + tmp.get(i).getNumber() - 1;
			cardInfo[i] = Integer.toString(idx);
		}
		result = cardInfo[0] + "x" + cardInfo[1] + "x" + cardInfo[2] + "x" + cardInfo[3] + "x" + cardInfo[4];
		return result;
	}
	
	public Contested getWinnerContested()
	{
		return Contested.getContested(community, playerList.get(getWinnerIndex()));
	}
	
	public int getWinnerIndex()
	{
		int max = 0;
		for (int i = 0; i < playerList.size() - 1; i++)
		{
			for (int j = i + 1; j < playerList.size(); j++)
			{
				if (Contested.getContested(community, playerList.get(i)).compareTo(Contested.getContested(community, playerList.get(j))) == 1)
					max = i;
				else
					max = j;
			}
		}
		return max;
	}
	
	public ArrayList<Card> getWinnerValid()
	{
		return getWinnerContested().getValid();
	}
}
