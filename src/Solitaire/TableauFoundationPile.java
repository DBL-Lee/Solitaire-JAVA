package Solitaire;

public class TableauFoundationPile extends GeneralPile implements Pile {
	
	
	public void deleteCardAtIndex(int index){
		for (int i=size-1;i>=index;i--){
			pile.remove(index);
			size--;
		}
	}

}
