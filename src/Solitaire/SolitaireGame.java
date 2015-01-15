package Solitaire;

import java.util.ArrayList;
import java.util.List;

public class SolitaireGame {
	protected List<Card> talon = new ArrayList<Card>();
	protected Pile[] tableau = new TableauFoundationPile[7];
	protected Pile wastePile = new WastePile();
	//0spade 1heart 2club 3diamond
	
	protected Pile[] foundationPile = new TableauFoundationPile[4];
	protected List<Card> draggedCards = new ArrayList<Card>();
	protected int draggedRow;
	protected int draggedColumn;
	protected CardDraggedState draggedState = null;
	
	public SolitaireGame() {
		for ( Suit s: Suit.values() ) {
		    for ( int cn=0 ; cn<13 ; cn++ ) {
				talon.add( Card.getCard( s, cn+1));
		    }
		}
		
		Pack.Shuffle(talon);
		
		//initialize tableau and foundation
		for (int col = 0; col<7; col++){
			tableau[col]= new TableauFoundationPile();
		}
		for (int col = 0; col<4; col++){
			foundationPile[col]= new TableauFoundationPile();
		}
		
		for ( int row = 0 ; row < 7 ; row++){
	    	for (int col = row; col < 7 ; col++){
	    		tableau[col].appendCard(talon.remove(0));
	    		if (col!=row){
	    			tableau[col].getCardAtIndex(row).setVisible(false);
	    		}
	    	}
	    }

	}
	
	//General
	public Card getCardAtPosition(int x, int y){
		Card card = tableau[x].getCardAtIndex(y);
		if (draggedCards.contains(card)){
			return null;
		}else{
			return card;
		}
	}
	
	public Card getCardInFoundation(int col, int row){
	Card card = foundationPile[col].getCardAtIndex(row);
	if (draggedCards.contains(card)){
		return null;
	}else{
		return card;
	}
	}
	
	public Card getWastePileOfIndex(int index){
		Card card = wastePile.getCardAtIndex(index);
		if (draggedCards.contains(card)){
			return null;
		}else{
			return card;
		}
	}
	
	public void cardClickedAt(int x,int y){
		Card card = tableau[x].getCardAtIndex(y);
		if (!card.isVisible()){
			card.setVisible(true);
		}
	}

	public int numberOfCardsInColumn(int col){
		return tableau[col].size();
	}
	
	public int numberOfCardsInFoundation(int col){
		return foundationPile[col].size();
	}
	
	public Boolean gameWin(){
		int res = 0;
		for (int index=0; index<4;index++){
			res = res+foundationPile[index].size();
		}
		//all cards in foundation
		return res==52;
	}
	
	//talon&wastepile
	public Boolean talonEmpty(){
		return talon.isEmpty();
	}
	
	public void talonClicked(){
		if (talon.size()>=3){
			for (int i = 0; i < 3; i++) {
				wastePile.appendCard(talon.remove(0));
			}
		}else if (talon.size()>0){
			while (!talon.isEmpty()){
				wastePile.appendCard(talon.remove(0));
			}
		}else{
			talon.clear();
			for (int i = 0; i<wastePile.size();i++){
				Card card = wastePile.getCardAtIndex(i);
				if (card!=null){
					talon.add(card);
				}
			}
			wastePile = new WastePile();
		}
	}
	

	public int getWastePileSize(){
		return wastePile.size();
	}
	
	public int getIndexOfLastCardInWastePile(){
		for (int i = wastePile.size()-1; i >= 0; i--) {
			if (wastePile.getCardAtIndex(i)!=null){
				return i;
			}
		}
		return -1;
	}
	

	//Dragging cards
	public List<Card> getDraggedCards(){
		return draggedCards;
	}
	
	public void moveBackDragged(){
		draggedCards.clear();
	}
	
	public void addDraggedCardToFoundation(int col){
		draggedState.droppedCardToFoundation(this, col);
	}
	
	public void addDraggedCardToColumn(int col){
		draggedState.droppedCardToTableau(this, col);
	}
	
	public Boolean cardDraggedAtIndex(int col, int row){
		if (tableau[col].getCardAtIndex(row).isVisible()){
			int count = 0;
			count = numberOfCardsInColumn(col);
			for (int index=row; index<count; index++){
				draggedCards.add(tableau[col].getCardAtIndex(index));
				draggedRow = row;
			}
			draggedColumn=col;
			draggedState = TableauDraggedState.INSTANCE;
			return true;
		}else{
			return false;
		}
	}
	
	public void wastePileDraggedAtIndex(int index){
		draggedCards.add(wastePile.getCardAtIndex(index));
		draggedRow = index;
		draggedState = WastePileDraggedState.INSTANCE;
	}
	
	public void foundationDraggedAtColumn(int col){
		draggedColumn = col;
		int index = foundationPile[col].size();
		draggedCards.add(foundationPile[col].getCardAtIndex(index-1));
		draggedRow = index-1;
		draggedState = FoundationDraggedState.INSTANCE;
	}
	

	


}
