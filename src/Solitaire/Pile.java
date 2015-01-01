package Solitaire;

public interface Pile {
	public int size();
	public Card getCardAtIndex(int index);
	public Boolean isEmpty();
	public void appendCard(Card card);
	public Card removeCardAtIndex(int index);
	public void deleteCardAtIndex(int index);
}
