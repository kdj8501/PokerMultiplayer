import java.util.ArrayList;

public class Deck {
	private ArrayList<Card> deck;
	
	public Deck()
	{
		deck = new ArrayList<Card>();
		resetDeck();
	}
	
	public void resetDeck()
	{
		deck.clear();
		for (int i = 0; i < 4; i++)
			for (int j = 1; j <= 13; j++)
				deck.add(new Card(i, j));
		shuffleDeck();
	}
	
	public void shuffleDeck()
	{
		for (int i = 0; i < 100000; i++)
		{
			int rand = (int)(Math.random() * (deck.size()));
			Card tmp = deck.get(rand);
			deck.remove(rand);
			deck.add(tmp);
		}
	}
	
	public Card drawCard()
	{
		shuffleDeck();
		Card tmp = deck.get(deck.size() - 1);
		deck.remove(deck.size() - 1);
		return tmp;
	}
	
	public Card get(int idx)
	{
		return deck.get(idx);
	}
}
