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
	public Vector<Card> self = new Vector<>();
	public Map<Card,Boolean> cardsInPlay = new HashMap<>();
	
	
	public Deck(){
		//create an initial deck (not shuffled) 
		initializeDeck();
		shuffle();
	}
	
	public void initializeDeck(){
		
		for(int i=2;i<15;i++){
			for(String suit : suitOpts){
				self.add(new Card(i,suit));
			}
		}
		//System.out.println("Created a deck of "+self.size()+" cards");
		for(Card c : this.self){cardsInPlay.put(c,false);}
	}
	
	public Vector<Card> deal(int nCards){
	    //shuffle();
	    Vector<Card> hand = new Vector<>();
	    int dealt = 0;
	    //If card isnt in play then deal it
	        for(Card i : this.self){
	           if(cardsInPlay.get(i)!=true && hand.size()<nCards){
	                hand.add(i);
	                cardsInPlay.put(i,true);
	                dealt ++;
	            }
	           /**if( (dealt<52 && hand.size()<nCards) && cardsInPlay.get(i)!=true ){
	               dealt++;
	               hand.add(i);
	           }
	            * 
	            */
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
	
	public void shuffle(){Collections.shuffle(self);}
	
	public static void main(String[]args){
	    Deck d = new Deck();
	    }
		
}
