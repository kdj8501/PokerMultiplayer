
public class Player {
	private Card hand1;
	private Card hand2;
	
	public Player(Card one, Card two)
	{
		hand1 = one;
		hand2 = two;
	}
	
	public Card getHand1() { return hand1; }
	public Card getHand2() { return hand2; }
	
	public String toString()
	{
		return "<" + hand1 + ", " + hand2 + ">";
	}
}
