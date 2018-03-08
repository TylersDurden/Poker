import java.util.*;

/**<DECK>
 * A deck of Card objects. 
 * @dependencies Card.java  
 * @author root
 * For more information: 
 * {@link https://github/com/TylersDurden/Poker}
 */
public class Deck {
	
	public final static String [] suitOpts = {"S","H","C","D" };
	public static Vector<Card> self = new Vector<>();
	public static Map<Card,Boolean> cardsInPlay = new HashMap<>();
	
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
		for(Card c : Deck.self){cardsInPlay.put(c,false);}
	}
	
	public static Vector<Card> deal(int nCards){
	    Deck.shuffle();
	    Vector<Card> hand = new Vector<>();
	    int dealt = 0;
	    //If card isnt in play then deal it
	        for(Card i : Deck.self){
	           if(cardsInPlay.get(i)!=true && hand.size()<nCards){
	                hand.add(i);
	                cardsInPlay.put(i,true);
	                dealt ++;
	            }
	        }
	    
	     /** once cards are dealt make sure deck knows they're in play
	    for(Map.Entry<Card,Boolean>entry:cardsInPlay.entrySet()){
	        for(Card c : hand){
	            if(entry.getKey().suit.compareTo(c.suit)==0 && 
	               entry.getKey().rank==c.rank){
	                cardsInPlay.put(entry.getKey(),true); 
	            }
	        }
	    }*/
	    
	    
	   return hand;
	}
	
	
	public static void shuffle(){Collections.shuffle(self);}
	
	public static void main(String[]args){
	    Deck d = new Deck();
	    }
		
}
