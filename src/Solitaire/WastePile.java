package Solitaire;

public class WastePile extends GeneralPile implements Pile {

	public void deleteCardAtIndex(int index){
		pile.set(index, null);
	}

}
