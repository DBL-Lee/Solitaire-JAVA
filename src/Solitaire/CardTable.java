package Solitaire;

/** A subclass of JPanel that incorporates the visual idea of a card table, and a pack of cards upon it
 * 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
class CardTable extends JPanel {
    protected static final int Margin=40, XShift=80, YShift=160, CardShift=15;
    //0-6 for 7 columns; 7 for foundation; 8 for wastepile
	private SolitaireGame game;
    static int shiftX, shiftY, currentX, currentY;
    private Boolean cardDragged = false;
    
    private void printCard( Graphics g, Card card, int x, int y){
        g.drawImage( card.getImage(), x, y, this);
    }
    
	public void paintComponent(Graphics g) {
		super.paintComponent( g);
    	g.setColor( Color.green);
    	g.fillRect( 0, 0, getWidth(), getHeight());
    	Card back = Card.getCardBack();	    	
    	Card outline = Card.getCardOutline();
    	
    	for ( int coln=0 ; coln<7 ; coln++ ) {
    		if ( coln!=2 )  printCard( g, outline, Margin+coln*XShift, Margin);
    		printCard( g, outline, Margin+coln*XShift, Margin+YShift);
    	}
    	
    	// Print talon
    	if (game.talonEmpty()){
    		printCard(g, Card.getEmptyTalon(), Margin, Margin);
    	}else{
    		printCard(g, back, Margin, Margin);
    	}
    	
    	// Print foundation
    	for (int col=0; col<4;col++){
    		for (int row=0; row<game.numberOfCardsInFoundation(col);row++){
    			Card card = game.getCardInFoundation(col, row);
    			if (card!=null){
    				printCard(g, card, Margin+XShift*(col+3), Margin);
    			}
    		}
    	}
    	
    	//print wastePile
    	try {
    		int wastePileSize = game.getWastePileSize();
        	for (int index=0;index<wastePileSize;index++){
        		Card card = game.getWastePileOfIndex(index);
        		if (card!=null){
        			printCard(g,card,Margin+XShift+index%3*CardShift,Margin);
        		}
        	}
		} catch (Exception e) {
			System.out.println(e);
		}
    	
    	
    	//print tableau
	    for ( int col = 0 ; col < 7 ; col++){
	    	for (int row = 0; row < game.numberOfCardsInColumn(col) ; row++){
	    		Card card = game.getCardAtPosition(col, row);
	    		if (card==null) {
					break;
				}else{
					if (card.isVisible()) {
						//show front
						printCard(g, card, Margin+col*XShift, Margin+YShift+CardShift*row);
					}else {
						//show back
						printCard(g, back, Margin+col*XShift, Margin+YShift+CardShift*row);
						printCard(g, outline, Margin+col*XShift, Margin+YShift+CardShift*row);
					} 
				}
	    	}
	    }
	    
	    //print dragged cards
	    if(cardDragged){
	    	List<Card> draggedCards = game.getDraggedCards();
	    	for (int num=0; num<draggedCards.size();num++){
	    		printCard(g, draggedCards.get(num), currentX-shiftX, currentY-shiftY+CardShift*num);
	    	}
	    }

	}
	
	public void restart(){
		game = new SolitaireGame();
		repaint();
	}
	
	public CardTable() {
		game = new SolitaireGame();

		
		//click on Talon
		addMouseListener(new MouseAdapter() {
    	    public void mouseClicked(MouseEvent e) {
    	    	int x = e.getX(), y = e.getY();
    	    	//the talon area
    	    	if ((new Rectangle2D.Double((double)Margin,(double)Margin,Card.getWidth(),Card.getHeight()).contains(x,y))){
    	    		game.talonClicked();
    	    		repaint();
    	    	}else if ((x-Margin)%XShift<=Card.getWidth()){
    	    		int col=(x-Margin)/XShift;
    	    		int cardCount = game.numberOfCardsInColumn(col);
    	    		if (cardCount>0&&new Rectangle2D.Double(Margin+col*XShift, Margin+YShift+CardShift*(cardCount-1), Card.getWidth(), Card.getHeight()).contains(x, y)){
    	    			game.cardClickedAt(col,cardCount-1);
    	    			repaint();
    	    		}
    	    	}
    	    }
    	});
		
    	// Define, instantiate and register a MouseListener object
    	addMouseListener(new MouseAdapter() {
    	    public void mousePressed(MouseEvent e) {
    	    	int x=e.getX(), y=e.getY();
    	    	currentX = x; currentY=y;
    	    	//click in wastepile
    	    	if (new Rectangle2D.Double(Margin+XShift,Margin,2*CardShift+Card.getWidth(),Card.getHeight()).contains(x, y)){
    	    		int indexOfLastCard = game.getIndexOfLastCardInWastePile();
    	    		if (indexOfLastCard>=0){
    	    			if (new Rectangle2D.Double(Margin+XShift+CardShift*(indexOfLastCard%3),Margin,Card.getWidth(),Card.getHeight()).contains(x, y)){
    	    				cardDragged = true;
    	    			    game.wastePileDraggedAtIndex(indexOfLastCard);
    	    				shiftX = x-Margin-XShift-CardShift*(indexOfLastCard%3);
    	    				shiftY = y-Margin; 
    	    			}
    	    		}
    	    	}else if ((x-Margin)%XShift<=Card.getWidth()&&((x-Margin)/XShift<7)){
    	    		int col=(x-Margin)/XShift;
    	    		//clicked in foundation
    	    		if (new Rectangle2D.Double(Margin+3*XShift, Margin, 4*XShift, Card.getHeight()).contains(x, y)) {
						int cardCount = game.numberOfCardsInFoundation(col-3);
						if (cardCount>0){
							cardDragged = true;
							game.foundationDraggedAtColumn(col-3);
							shiftX = x-Margin-XShift*col;
    	    				shiftY = y-Margin; 
						}
					}else {
	    	    		int cardCount = game.numberOfCardsInColumn(col);
	    	    		//there is a card in the column and clicked above last card
	    	    		if (cardCount>0&&y<=(Margin+YShift+CardShift*(cardCount-1)+Card.getHeight())&&y>=(Margin+YShift)){
	    	    			//clicked on the last card
    	    				if (new Rectangle2D.Double(Margin+col*XShift, Margin+YShift+CardShift*(cardCount-1), Card.getWidth(), Card.getHeight()).contains(x, y)) {
    	    					cardDragged = game.cardDraggedAtIndex(col,cardCount-1);
    	    					if (cardDragged){
    	    						shiftX = (x-Margin)%XShift;
    	    						shiftY = y-Margin-YShift-CardShift*(cardCount-1);
    	    					}
    	    				}else{
    	    					cardDragged = game.cardDraggedAtIndex(col,(y-Margin-YShift)/CardShift);
    	    					if (cardDragged){
    	    						shiftX = (x-Margin)%XShift;
    	    						shiftY = (y-Margin-YShift)%CardShift;
    	    					}
    	    				}
    	    			}
					}
    	    	}
    	    }
    	});

    	// Define, instantiate and register a MouseListener object
    	addMouseListener(new MouseAdapter() {
    	    public void mouseReleased(MouseEvent e) {
    	    	if (cardDragged) {
    	    		// x,y is the centre of the card
    	    		int x = (int)Math.round(e.getX()-shiftX+Card.getWidth()/2), y= (int)Math.round(e.getY()-shiftY+Card.getHeight()/2);
    	    		//lies in a column
    	    		if ((x-Margin)%XShift<=Card.getWidth()&&((x-Margin)/XShift<7)){
    	    			int col=(x-Margin)/XShift;
    	    			//lies in foundation area
    	    			if (new Rectangle2D.Double(Margin+3*XShift, Margin, 4*XShift, Card.getHeight()).contains(x, y)){
        	    			game.addDraggedCardToFoundation(col-3);
        	    		}else{ 
        	    			int cardCount = game.numberOfCardsInColumn(col);
        	    			if (y>=Margin+YShift+CardShift*(cardCount-1)&&(y<=Margin+YShift+CardShift*(cardCount-1)+Card.getHeight())){
    	    					game.addDraggedCardToColumn(col);
    	    				}else{
    	    					game.moveBackDragged();
    	    				}
        	    		}
    	    		}else{
    	    			//move back dragged
    	    			game.moveBackDragged();
    	    		}
    	    		
    	    		cardDragged = false;
    	    		repaint();
    	    	}
    	    	if (game.gameWin()){
    	    		JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor((JPanel)e.getSource()), "You Win!");
    	    	}
    	    }
    	});

    	// Define, instantiate and register a MouseMotionListener object
    	addMouseMotionListener(new MouseMotionAdapter() {
    	    public void mouseDragged(MouseEvent e) {
    		// draw a rubber line (in black)
    		currentX = e.getX();  currentY = e.getY();
    		repaint();
    	    }
    	});
    	
    	//space for talon
    	this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(' '), "talon");
    	this.getActionMap().put("talon", new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				game.talonClicked();
				repaint();
			}
		});
    	
		repaint();
	}

    
    public Dimension getMinimumSize() {
    	return getPreferredSize();
    }

    public Dimension getMaximumSize() {
    	return getPreferredSize();
    }
    
}