package Solitaire;

public interface CardDraggedState {
	public void droppedCardToTableau(SolitaireGame game, int col);
	public void droppedCardToFoundation(SolitaireGame game, int col);
}