import java.util.*;

/**<DECK>
 * A deck of Card objects. 
 * For more information: 
 * {@link https://github/com/TylersDurden/Poker}
 * @dependencies Card.java  
 * @author root
  */
public class Deck {
	
	public final static String [] suitOpts = {"S","H","C","D" };
	public static Vector<Card> self = new Vector<>();
	
	public Deck(){
		//create an initial deck (not shuffled) 
		initializeDeck();
		shuffle();
	}
	
	public static void initializeDeck(){
		
		for(int i=2;i<15;i++){
			for(String suit : suitOpts){
				Deck.self.add(new Card(i,suit));
			}
		}
		System.out.println("Created a deck of "+Deck.self.size()+" cards");
	}
	
	public static void shuffle(){Collections.shuffle(self);}
	
	public static void main(String[]args){ new Deck();}
		
}
