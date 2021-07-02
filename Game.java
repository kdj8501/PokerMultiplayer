import java.util.ArrayList;

public class Game {
	private boolean started;
	private Deck deck;
	private ArrayList<Player> playerList;
	private ArrayList<Info> infoList;
	private ArrayList<Boolean> dieChecker;
	private Community community;
	private int round;
	private int pot;
	private int turn;
	private int maxbet;
	private int betRound;
	private int cCount;
	private boolean isProgress;
	
	public Game()
	{
		deck = new Deck();
		playerList = new ArrayList<Player>();
		infoList = new ArrayList<Info>();
		community = new Community();
		dieChecker = new ArrayList<Boolean>();
		started = false;
		round = 0;
		pot = 0;
		turn = -1;
		maxbet = 0;
		betRound = 0;
		isProgress = false;
		cCount = 0;
	}
	
	public boolean getStarted() { return started; }
	
	public void setPot(int pot) { this.pot = pot; }
	public void addPot(int pot) { setPot(getPot() + pot); }
	public int getPot() { return pot; }
	
	public void setMaxBet(int maxbet) { this.maxbet = maxbet; }
	public int getMaxBet() { return maxbet; }
	
	public int getTurn() { return turn; }
	
	public void startGame(int n)
	{
		started = true;
		playerList.clear();
		infoList.clear();
		for (int i = 0; i < n; i++)
		{
			addPlayerList();
			infoList.add(new Info());
			dieChecker.add(false);
		}
		round = 1;
		turn = round % infoList.size();
		pot += infoList.get(getBigBlind()).doBet((int)Math.pow(2, round / 10 + 1));
		pot += infoList.get(getSmallBlind()).doBet((int)Math.pow(2, round / 10));
		maxbet = 2;
		betRound = 1;
		isProgress = false;
		cCount = 0;
	}
	
	public void stopGame()
	{
		started = false;
		deck.resetDeck();
		community.reset();
		round = 0;
		pot = 0;
		infoList.clear();
		playerList.clear();
		dieChecker.clear();
		maxbet = 0;
		betRound = 0;
		isProgress = false;
		cCount = 0;
		turn = -1;
	}
	
	public void resetGame()
	{
		if (started)
		{
			deck.resetDeck();
			community.reset();
			round++;
			betRound = 1;
			pot = 0;
			maxbet = 2;
			playerList.clear();
			for (int i = 0; i < infoList.size(); i++)
			{
				infoList.get(i).setDie(false);
				infoList.get(i).setBet(0);
				if (!dieChecker.get(i))
					addPlayerList();
				else
				{
					playerList.add(new Player());
					infoList.get(i).setDie(true);
				}
			}
			pot += infoList.get(getBigBlind()).doBet((int)Math.pow(2, round / 10 + 1));
			pot += infoList.get(getSmallBlind()).doBet((int)Math.pow(2, round / 10));
			turn = getFirstBetAtFirstRound();
		}
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
	
	public String getCommunityCardsString()
	{
		return community.toString();
	}
	
	public Contested getPlayerContested(int idx)
	{
		return Contested.getContested(community, playerList.get(idx));
	}
	
	public String getPlayerCardsString(int idx)
	{
		return playerList.get(idx).toString();
	}
	
	public Contested getWinnerContested()
	{
		int max = 0;
		for (int i = 0; i < infoList.size() - 1; i++)
		{
			for (int j = i + 1; j < infoList.size(); j++)
			{
				if (infoList.get(i).getDie() || infoList.get(j).getDie())
					continue;
				if (Contested.getContested(community, playerList.get(i)).compareTo(Contested.getContested(community, playerList.get(j))) == 1)
					max = i;
				else
					max = j;
			}
		}
		return getPlayerContested(max);
	}
	
	public ArrayList<Player> getWinners()
	{
		ArrayList<Player> result = new ArrayList<Player>();
		for (int i = 0; i < infoList.size(); i++)
			if (!infoList.get(i).getDie())
				if (getWinnerContested().compareTo(getPlayerContested(i)) == 0)
					result.add(playerList.get(i));
		return result;
	}
	
	public ArrayList<Card> getPlayerValid(int idx)
	{
		return getPlayerContested(idx).getValid();
	}
	
	public Info getInfo(int idx)
	{
		return infoList.get(idx);
	}
	
	public void turnProgress()
	{
		do
			turn = (turn + 1) % infoList.size();
		while (infoList.get(turn).getDie());
		if (betRound != 1 && turn == getFirstBet())
			cCount++;
		if (betRound == 1 && ((turn == getFirstBetAtFirstRound() % infoList.size() && maxbet == (int)Math.pow(2, round / 10 + 1) && infoList.get(turn).getBet() == (int)Math.pow(2, round / 10 + 1)) || (maxbet != 2 && infoList.get(turn).getBet() == maxbet))
				|| betRound != 1 && ((cCount == 1 && maxbet == 0) || (infoList.get(turn).getBet() == maxbet && maxbet != 0)))
		{
			for (Info x : infoList)
				x.setBet(0);
			maxbet = 0;
			betRound++;
			isProgress = true;
			cCount = 0;
			turn = getFirstBet();
		}
	}
	
	public int getPlayerIDByPlayerList(Player player)
	{
		return playerList.indexOf(player);
	}
	
	public int getBetRound() { return betRound; }
	public boolean getIsProgress() { return isProgress; }
	public void setIsProgress(boolean isProgress) { this.isProgress = isProgress; }
	public int getFirstBet()
	{
		int result = (round % infoList.size() - 2) > -1 ? round % infoList.size() - 2 : round % infoList.size() + infoList.size() - 2;
		
		while (infoList.get(result).getDie())
			result = (result + 1) % infoList.size();
		
		return result;
	}
	public int getAlive()
	{
		int result = 0;
		for (Info x : infoList)
			if (!x.getDie())
				result++;
		return result;
	}
	public int getLastStanding()
	{
		int result = 0;
		if (getAlive() != 1)
			result = -1;
		else
		{
			for (Info x : infoList)
				if (!x.getDie())
					result = infoList.indexOf(x);
		}
		return result;
	}
	public int getTotalAlive()
	{
		int result = 0;
		for (boolean x : dieChecker)
			if (!x)
				result++;
		return result;
	}
	public int getTotalLastStanding()
	{
		int result = 0;
		if (getTotalAlive() != 1)
			result = -1;
		else
		{
			int i = 0;
			for (boolean x : dieChecker)
			{
				if (!x)
					result = i;
				i++;
			}
		}
		return result;
	}
	public void DiePlayerChecker()
	{
		for (int i = 0; i < infoList.size(); i++)
			if (infoList.get(i).getMoney() <= 0)
				dieChecker.set(i, true);
	}
	public boolean getPlayerDie(int idx)
	{
		return dieChecker.get(idx);
	}
	public int getBigBlind()
	{
		int result = (getFirstBet() + 1) % infoList.size();
		while (infoList.get(result).getDie())
			result = (result + 1) % infoList.size();
		return result;
	}
	public int getSmallBlind()
	{
		int result = (getFirstBet()) % infoList.size();
		while (infoList.get(result).getDie())
			result = (result + 1) % infoList.size();
		return result;
	}
	public int getFirstBetAtFirstRound()
	{
		int result = (getBigBlind() + 1) % infoList.size();
		while (infoList.get(result).getDie())
			result = (result + 1) % infoList.size();
		return result;
	}
}
