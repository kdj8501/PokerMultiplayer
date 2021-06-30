import java.util.ArrayList;

public class Community {
	private ArrayList<Card> community;
	
	public Community() { community = new ArrayList<Card>(); }
	
	public void addCard(Card card) { community.add(card); }
	
	public ArrayList<Card> getCards() { return community; }
	
	public int getSize() { return community.size(); }
	
	public void reset() { community.clear(); }
	
	public String toString()
	{
		String result = "";
		String str = "";
		int i = 1;
		for (Card x : community)
		{
			str += x;
			if (i != community.size())
				str += ", ";
			i++;
		}
		
		result = "<" + str + ">";
		return result;
	}
}
