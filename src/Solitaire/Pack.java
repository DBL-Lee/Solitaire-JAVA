/** Just a method for shuffling a pack of cards
 */

package Solitaire;

import java.util.List;
import java.util.Random;

public class Pack {

    /**
    * Shuffle shuffles the pack.
    */
    public static void Shuffle( List<Card> cards) {
    	Shuffle( cards, cards.size(), new Random());
    }

	/** Shuffle the first n cards in list in-place, using the random
	 *  number generator rgen
	 */
    private static void Shuffle( List<Card> cards, int n, Random rgen) {
		if ( n<=1 ) return; // finished!
		
		// select a card number in the range [0..n), and swap that with position n-1
		int cn = (int) (n * rgen.nextFloat());
		assert cn>=0 && cn < n;
		Card cv = cards.get( cn), lv = cards.get( n-1);
		cards.set( cn, lv);  cards.set( n-1, cv);
		// recursively shuffle the rest 
		Shuffle( cards, n-1, rgen);
		return;
    }
}

