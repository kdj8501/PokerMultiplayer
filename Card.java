
public class Card implements Comparable<Card> {
	public static final int CARD_SHAPE_SPADE = 0;
	public static final int CARD_SHAPE_DIAMOND = 1;
	public static final int CARD_SHAPE_HEART = 2;
	public static final int CARD_SHAPE_CLOVER = 3;
	
	private int number;
	private int shape;
	
	public Card(int shape, int number)
	{
		setShape(shape);
		setNumber(number);
	}
	
	public void setShape(int shape) { this.shape = shape; }
	public int getShape() { return shape; }
	public void setNumber(int number) { this.number = number; }
	public int getNumber() { return number; }
	
	public String toString()
	{
		String shape = "";
		String number = "";
		switch(getShape())
		{
			case CARD_SHAPE_SPADE:
				shape = "¢¼";
				break;
			case CARD_SHAPE_DIAMOND:
				shape = "¡ß";
				break;
			case CARD_SHAPE_HEART:
				shape = "¢¾";
				break;
			case CARD_SHAPE_CLOVER:
				shape = "¢À";
				break;
		}
		switch(getNumber())
		{
			case 1:
				number = "A";
				break;
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				number = Integer.toString(getNumber());
				break;
			case 11:
				number = "J";
				break;
			case 12:
				number = "Q";
				break;
			case 13:
				number = "K";
				break;
		}
		return "<" + shape + ", " + number + ">";
	}
	
	public int compareTo(Card other)
	{
		return getNumber() - other.getNumber();
	}
	
	public boolean equals(Card other)
	{
		return (getShape() == other.getShape() && getNumber() == other.getNumber());
	}
}
