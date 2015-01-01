package Solitaire;

public class FoundationDraggedState implements CardDraggedState{
	
	protected final static CardDraggedState INSTANCE = new FoundationDraggedState();

	public void droppedCardToTableau(SolitaireGame game, int col) {
		int currentLast = game.numberOfCardsInColumn(col);
		Card firstCard = game.draggedCards.get(0);
		if (game.tableau[col].isEmpty()){ //empty column
			if (firstCard.getCardNum()==13){
				game.tableau[col].appendCard(game.draggedCards.remove(0));
				game.foundationPile[game.draggedColumn].deleteCardAtIndex(game.draggedRow);
				game.draggedCards.clear();
			}else{
				game.moveBackDragged();
			}
		}else{
			Card lastCard = game.tableau[col].getCardAtIndex(currentLast-1);
			//different colour and number decrease by 1
			if ((firstCard.getCardNum()==lastCard.getCardNum()-1)&&(firstCard.isRed()!=lastCard.isRed())){
				game.tableau[col].appendCard(game.draggedCards.remove(0));
				game.foundationPile[game.draggedColumn].deleteCardAtIndex(game.draggedRow);
				game.draggedCards.clear();
			}else{
				game.moveBackDragged();
			}
		}
	}
	

	public void droppedCardToFoundation(SolitaireGame game, int col) {
		game.moveBackDragged();
	}

}
