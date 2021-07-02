
public class Info {
	private int money;
	private int bet;
	private boolean die;
	public Info()
	{
		money = 100;
		bet = 0;
		die = false;
	}
	
	public void setMoney(int money) { this.money = money; }
	public int getMoney() { return money; }
	public void setBet(int bet) { this.bet = bet; }
	public int getBet() { return bet; }
	public void setDie(boolean die) { this.die = die; }
	public boolean getDie() { return die; }
	
	public int doBet(int bet)
	{
		setBet(getBet() + bet);
		setMoney(getMoney() - bet);
		return bet;
	}
}
