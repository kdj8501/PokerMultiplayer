import java.util.ArrayList;
import java.util.Collections;

public class Contested implements Comparable<Contested>{
	public static final int HIGH_CARD = 0;
	public static final int PAIR = 1;
	public static final int TWO_PAIR = 2;
	public static final int TRIPLE = 3;
	public static final int STRAIGHT = 4;
	public static final int FLUSH = 5;
	public static final int FULL_HOUSE = 6;
	public static final int FOUR_CARD = 7;
	public static final int STRAIGHT_FLUSH = 8;
	public static final int ROYAL_STRAIGHT_FLUSH = 9;
	
	private int rank;
	private int high1;
	private int high2;
	private ArrayList<Card> valid;
	private String text;
	public Contested(int rank, int high1, int high2, ArrayList<Card> valid, String text)
	{
		this.valid = valid;
		setRank(rank);
		setHigh1(high1);
		setHigh2(high2);
		setText(text);
	}
	
	public void setRank(int rank) { this.rank = rank; }
	public int getRank() { return rank; }
	public void setHigh1(int high1) { this.high1 = high1; }
	public int getHigh1() { return high1; }
	public void setHigh2(int high2) { this.high2 = high2; }
	public int getHigh2() { return high2; }
	public void setText(String text) { this.text = text; }
	public String getText() { return text; }
	
	public ArrayList<Card> getValid() { return valid; }
	
	public int compareTo(Contested other)
	{
		if (getRank() > other.getRank())
			return 1;
		else if (getRank() < other.getRank())
			return - 1;
		else
		{
			if (getHigh1() > other.getHigh1())
				return 1;
			else if (getHigh1() < other.getHigh1())
				return -1;
			else
			{
				if (getHigh2() > other.getHigh2())
					return 1;
				else if (getHigh2() < other.getHigh2())
					return -1;
				else
				{
					if (getRank() > 3) return 0;
					else
					{
						if (getRank() == 3)
						{
							int val1 = getValid().get(3).getNumber() == 1 ? 14 : getValid().get(3).getNumber(), val2 = other.getValid().get(3).getNumber() == 1 ? 14 : other.getValid().get(3).getNumber();
							if (val1 > val2)
								return 1;
							else if (val1 < val2)
								return -1;
							else
							{
								val1 = getValid().get(4).getNumber() == 1 ? 14 : getValid().get(4).getNumber(); val2 = other.getValid().get(4).getNumber() == 1 ? 14 : other.getValid().get(4).getNumber();
								if (val1 > val2)
									return 1;
								else if (val1 < val2)
									return -1;
								else
									return 0;
							}
						}
						else if (getRank() == 2)
						{
							int val1 = getValid().get(4).getNumber() == 1 ? 14 : getValid().get(4).getNumber(), val2 = other.getValid().get(4).getNumber() == 1 ? 14 : other.getValid().get(4).getNumber();
							if (val1 > val2)
								return 1;
							else if (val1 < val2)
								return -1;
							else
							{
								return 0;
							}
						}
						else if (getRank() == 1)
						{
							int val1 = getValid().get(2).getNumber() == 1 ? 14 : getValid().get(2).getNumber(), val2 = other.getValid().get(2).getNumber() == 1 ? 14 : other.getValid().get(2).getNumber();
							if (val1 > val2)
								return 1;
							else if (val1 < val2)
								return -1;
							else
							{
								val1 = getValid().get(3).getNumber() == 1 ? 14 : getValid().get(3).getNumber(); val2 = other.getValid().get(3).getNumber() == 1 ? 14 : other.getValid().get(3).getNumber();
								if (val1 > val2)
									return 1;
								else if (val1 < val2)
									return -1;
								else
								{
									val1 = getValid().get(4).getNumber() == 1 ? 14 : getValid().get(4).getNumber(); val2 = other.getValid().get(4).getNumber() == 1 ? 14 : other.getValid().get(4).getNumber();
									if (val1 > val2)
										return 1;
									else if (val1 < val2)
										return -1;
									else
										return 0;
								}
							}
						}
						else
						{
							int val1 = getValid().get(1).getNumber() == 1 ? 14 : getValid().get(1).getNumber(), val2 = other.getValid().get(1).getNumber() == 1 ? 14 : other.getValid().get(1).getNumber();
							if (val1 > val2)
								return 1;
							else if (val1 < val2)
								return -1;
							else
							{
								val1 = getValid().get(2).getNumber() == 1 ? 14 : getValid().get(2).getNumber(); val2 = other.getValid().get(2).getNumber() == 1 ? 14 : other.getValid().get(2).getNumber();
								if (val1 > val2)
									return 1;
								else if (val1 < val2)
									return -1;
								else
								{
									val1 = getValid().get(3).getNumber() == 1 ? 14 : getValid().get(3).getNumber(); val2 = other.getValid().get(3).getNumber() == 1 ? 14 : other.getValid().get(3).getNumber();
									if (val1 > val2)
										return 1;
									else if (val1 < val2)
										return -1;
									else
									{
										val1 = getValid().get(4).getNumber() == 1 ? 14 : getValid().get(4).getNumber(); val2 = other.getValid().get(4).getNumber() == 1 ? 14 : other.getValid().get(4).getNumber();
										if (val1 > val2)
											return 1;
										else if (val1 < val2)
											return -1;
										else
											return 0;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static Contested getContested(Community com, Player pl)
	{
		int high = 0;
		int tHigh = 0;
		int p1High = 0;
		int p2High = 0;
		boolean pair = false;
		boolean twoPair = false;
		boolean triple = false;
		boolean fullHouse = false;
		boolean fourCard = false;
		boolean flush = false;
		boolean straight = false;
		boolean straightFlush = false;
		boolean royalStraightFlush = false;
		
		ArrayList<Card> hands = new ArrayList<Card>();
		ArrayList<Card> straightValid = new ArrayList<Card>();
		ArrayList<Card> valid = new ArrayList<Card>();
		hands.add(pl.getHand1());
		hands.add(pl.getHand2());
		for (Card x : com.getCards())
			hands.add(x);
		
		Collections.sort(hands);
		
		// Check Four Card
		for (int i = 0; i < hands.size() - 3; i++)
		{
			if ((hands.get(i).getNumber() == hands.get(i + 1).getNumber()) && (hands.get(i).getNumber() == hands.get(i + 2).getNumber()) && (hands.get(i).getNumber() == hands.get(i + 3).getNumber()))
			{
				fourCard = true;
				high = hands.get(i).getNumber();
				if (high == 1) high = 14;
			}
		}
		// Check Flush
		int sCount = 0, dCount = 0, hCount = 0, cCount = 0;
		int sHigh = 0, dHigh = 0, hHigh = 0, cHigh = 0;
		int stHigh = 0;
		int fShape = -1;
		for (Card x : hands)
		{
			if (x.getShape() == Card.CARD_SHAPE_SPADE)
			{
				sCount++;
				int val = x.getNumber();
				if (val == 1) val = 14;
				sHigh = val > sHigh ? val : sHigh;
				if (sCount > 4)
				{
					flush = true;
					if (!fourCard)
						high = sHigh;
					fShape = Card.CARD_SHAPE_SPADE;
				}
			}
			else if (x.getShape() == Card.CARD_SHAPE_DIAMOND)
			{
				dCount++;
				int val = x.getNumber();
				if (val == 1) val = 14;
				dHigh = val > dHigh ? val : dHigh;
				if (dCount > 4)
				{
					flush = true;
					if (!fourCard)
						high = dHigh;
					fShape = Card.CARD_SHAPE_DIAMOND;
				}
			}
			else if (x.getShape() == Card.CARD_SHAPE_HEART)
			{
				hCount++;
				int val = x.getNumber();
				if (val == 1) val = 14;
				hHigh = val > hHigh ? val : hHigh;
				if (hCount > 4)
				{
					flush = true;
					if (!fourCard)
						high = hHigh;
					fShape = Card.CARD_SHAPE_HEART;
				}
			}
			else
			{
				cCount++;
				int val = x.getNumber();
				if (val == 1) val = 14;
				cHigh = val > cHigh ? val : cHigh;
				if (cCount > 4)
				{
					flush = true;
					if (!fourCard)
						high = cHigh;
					fShape = Card.CARD_SHAPE_CLOVER;
				}
			}
		}
		
		// Check Straight
		boolean thereAce = false;
		for (int i = 0; i < hands.size(); i++)
		{
			if (hands.get(i).getNumber() == 1) thereAce = true;
			if (straightValid.size() == 0)
				straightValid.add(hands.get(i));
			else if (hands.get(i).getNumber() - straightValid.get(straightValid.size() - 1).getNumber() < 2)
				straightValid.add(hands.get(i));
			else if (straightValid.get(straightValid.size() - 1).getNumber() - straightValid.get(0).getNumber() < 3)
				straightValid.clear();
		}

		if (straightValid.size() != 0 && straightValid.get(straightValid.size() - 1).getNumber() - straightValid.get(0).getNumber() >= 4)
		{
			straight = true;
			stHigh = straightValid.get(straightValid.size() - 1).getNumber();
		}
		else if (straightValid.size() != 0 && straightValid.get(straightValid.size() - 1).getNumber() - straightValid.get(0).getNumber() == 3 && straightValid.get(straightValid.size() - 1).getNumber() == 13 && thereAce)
		{
			straight = true;
			stHigh = 14;
			for (Card x : hands)
				if (x.getNumber() == 1)
					straightValid.add(x);
		}
		
		if (straight && stHigh == 13 && thereAce)
		{
			stHigh = 14;
			for (Card x : hands)
				if (x.getNumber() == 1)
					straightValid.add(x);
		}
		
		// Check Straight Flush & Royal Straight Flush
		if (straight && flush)
		{
			for (int i = 0; i < straightValid.size(); i++)
				if (straightValid.get(i).getShape() != fShape)
					straightValid.remove(i);
			while (straightValid.size() > 5)
				straightValid.remove(0);
			if (straightValid.size() == 5)
			{
				high = straightValid.get(straightValid.size() - 1).getNumber();
				if (high == 1) high = 14;
				straightFlush = true;
			}
			if (high == 14 && straightFlush)
				royalStraightFlush = true;
		}
		
		// Check Triple & Full House
		if (!fourCard)
		{
			for (int i = 0; i < hands.size() - 2; i++)
			{
				if ((hands.get(i).getNumber() == hands.get(i + 1).getNumber()) && (hands.get(i).getNumber() == hands.get(i + 2).getNumber()))
				{
					if (triple)
					{
						if (tHigh != 14)
						{
							p1High = tHigh;
							tHigh = hands.get(i).getNumber();
						}
						else
							p1High = hands.get(i).getNumber();
						if (tHigh == 1) tHigh = 14;
						fullHouse = true;
					}
					else
					{
						triple = true;
						tHigh = hands.get(i).getNumber();
						if (tHigh == 1) tHigh = 14;
					}
				}
			}
		}
		if (triple && !fullHouse)
			for (int i = 0; i < hands.size() - 1; i++)
				if (hands.get(i).getNumber() == hands.get(i + 1).getNumber())
					if (tHigh != hands.get(i).getNumber() && !(tHigh == 14 && hands.get(i).getNumber() == 1))
					{
						p1High = hands.get(i).getNumber();
						if (p1High == 1) p1High = 14;
						fullHouse = true;
					}
		
		// Check Pair & Two Pair
		if (!triple && !fourCard)
			for (int i = 0; i < hands.size() - 1; i++)
				if (hands.get(i).getNumber() == hands.get(i + 1).getNumber())
				{
					if (pair)
					{
						if (p1High == 14)
							p2High = hands.get(i).getNumber();
						else
						{
							p2High = p1High;
							p1High = hands.get(i).getNumber();
						}
						twoPair = true;
					}
					else
					{
						p1High = hands.get(i).getNumber();
						if (p1High == 1) p1High = 14;
						pair = true;
					}
				}
		
		if (!flush && !straight && !pair && !twoPair && !triple)
		{
			if (hands.get(0).getNumber() == 1)
				high = 14;
			else
				high = hands.get(hands.size() - 1).getNumber();
		}
		
		if (straightFlush)
		{
			for (Card x : straightValid)
				valid.add(x);
		}
		else if (fourCard)
		{
			for (Card x : hands)
				if (x.getNumber() == high || (high == 14 && x.getNumber() == 1))
					valid.add(x);
			if (high != 14)
				for (Card x : hands)
					if (x.getNumber() == 1)
					{
						valid.add(x);
						break;
					}
			int i = hands.size() - 1;
			while (valid.size() != 5)
			{
				Card x = hands.get(i--);
				if (!(x.getNumber() == high || (high == 14 && x.getNumber() == 1)))
					valid.add(x);
			}
		}
		else if (fullHouse)
		{
			for (Card x : hands)
				if (x.getNumber() == tHigh || x.getNumber() == p1High || (x.getNumber() == 1 && tHigh == 14) || (x.getNumber() == 1 && p1High == 14))
					valid.add(x);
			int i = 0;
			while (valid.size() != 5)
			{
				Card x = hands.get(i++);
				if (x.getNumber() == p1High)
					valid.remove(x);
			}
		}
		else if (flush)
		{
			for (Card x : hands)
				if (x.getShape() == fShape)
					valid.add(x);
			while (valid.size() != 5)
			{
				if (valid.get(0).getNumber() == 1)
					valid.remove(1);
				else
					valid.remove(0);
			}
		}
		else if (straight)
		{
			for (Card x : straightValid)
				valid.add(x);
			for (int i = 0; i < valid.size() - 1; i++)
				if (valid.get(i).getNumber() == valid.get(i + 1).getNumber())
					valid.remove(i + 1);
			while (valid.size() > 5)
					valid.remove(0);
		}
		else if (triple)
		{
			for (Card x : hands)
				if (x.getNumber() == tHigh || (tHigh == 14 && x.getNumber() == 1))
					valid.add(x);
			if (tHigh != 14)
				for (Card x : hands)
					if (x.getNumber() == 1)
						valid.add(x);
			int i = hands.size() - 1;
			while (valid.size() != 5)
			{
				Card x = hands.get(i--);
				if (!(x.getNumber() == tHigh || (tHigh == 14 && x.getNumber() == 1)))
					valid.add(x);
			}
		}
		else if (twoPair)
		{
			for (Card x : hands)
				if ((x.getNumber() == p1High || (p1High == 14 && x.getNumber() == 1)) || (x.getNumber() == p2High || (p2High == 14 && x.getNumber() == 1)))
					valid.add(x);
			if (p1High != 14 && p2High != 14)
				for (Card x : hands)
					if (x.getNumber() == 1)
					{
						valid.add(x);
						break;
					}
			int i = hands.size() - 1;
			while (valid.size() != 5)
			{
				Card x = hands.get(i--);
				if (!((x.getNumber() == p1High || (p1High == 14 && x.getNumber() == 1)) || (x.getNumber() == p2High || (p2High == 14 && x.getNumber() == 1))))
					valid.add(x);
			}
		}
		else if (pair)
		{
			for (Card x : hands)
				if (x.getNumber() == p1High || (p1High == 14 && x.getNumber() == 1))
					valid.add(x);
			if (p1High != 14)
				for (Card x : hands)
					if (x.getNumber() == 1)
					{
						valid.add(x);
						break;
					}
			int i = hands.size() - 1;
			while (valid.size() != 5)
			{
				Card x = hands.get(i--);
				if (!((x.getNumber() == p1High || (p1High == 14 && x.getNumber() == 1)) || (x.getNumber() == p2High || (p2High == 14 && x.getNumber() == 1))))
					valid.add(x);
			}
		}
		else
		{
			if (high == 14)
				for (Card x : hands)
					if (x.getNumber() == 1)
						valid.add(x);
			int i = hands.size() - 1;
			while (valid.size() != 5)
			{
				Card x = hands.get(i--);
				valid.add(x);
			}
		}

		int rank = Contested.HIGH_CARD;
		String text = "";
		if (royalStraightFlush)
		{
			text = "Royal Straight Flush!";
			p1High = high;
			p2High = 0;
			rank = Contested.ROYAL_STRAIGHT_FLUSH;
		}
		else if (straightFlush)
		{
			text = "Straight Flush! with " + Integer.toString(high);
			p1High = high;
			p2High = 0;
			rank = Contested.STRAIGHT_FLUSH;
		}
		else if (fourCard)
		{
			text = "Four Card! with " + Integer.toString(high);
			p1High = high;
			p2High = valid.get(valid.size() - 1).getNumber();
			rank = Contested.FOUR_CARD;
		}
		else if (fullHouse)
		{
			text = "Full House! with " + Integer.toString(tHigh) + " & " + Integer.toString(p1High);
			p1High = tHigh;
			p2High = p1High;
			rank = Contested.FULL_HOUSE;
		}
		else if (flush)
		{
			text = "Flush! with " + Integer.toString(high);
			p1High = high;
			p2High = 0;
			rank = Contested.FLUSH;
		}
		else if (straight)
		{
			text = "Straight! with " + Integer.toString(stHigh);
			p1High = stHigh;
			p2High = 0;
			rank = Contested.STRAIGHT;
		}
		else if (triple)
		{
			text = "Triple! with " + Integer.toString(tHigh);
			p1High = tHigh;
			p2High = 0;
			rank = Contested.TRIPLE;
		}
		else if (twoPair)
		{
			text = "Two Pair! with " + Integer.toString(p1High) + " & " + Integer.toString(p2High);
			rank = Contested.TWO_PAIR;
		}
		else if (pair)
		{
			text = "Pair! with " + Integer.toString(p1High);
			p2High = 0;
			rank = Contested.PAIR;
		}
		else
		{
			text = "High Card with " + Integer.toString(high);
			p1High = high;
			p2High = 0;
			rank = Contested.HIGH_CARD;
		}
		
		text = text.replace("11", "J");
		text = text.replace("12", "Q");
		text = text.replace("13", "K");
		text = text.replace("14", "A");
		
		return new Contested(rank, p1High, p2High, valid, text);
	}
}
