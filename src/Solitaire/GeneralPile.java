package Solitaire;

import java.util.ArrayList;
import java.util.List;

public abstract class GeneralPile implements Pile {


	protected List<Card> pile = new ArrayList<Card>();
	protected int size = 0;

	public int size() {
		return size;
	}

	public Card getCardAtIndex(int index) {
		assert(index<size);
		return pile.get(index);
	}

	public Boolean isEmpty() {
		return size==0;
	}

	public void appendCard(Card card) {
		pile.add(card);
		size++;
	}

	public Card removeCardAtIndex(int index) {
		assert(index<size);
		size--;
		return pile.remove(index);
	}
	
	abstract public void deleteCardAtIndex(int index);
}