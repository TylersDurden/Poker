import java.util.*;

/** CARD object
 * @param integer Rank,  String Suit 
 * @author root 
 * For more information: 
 * {@link https://github/com/TylersDurden/Poker}
 */
public class Card{
	
	public String suit;
	public int rank;
	public static final String [] types= {"Spades","hearts","Clubs","Diamonds"};
	public static final String [] facecards = {"Ace","King","Queen","Jack"};
	
	public static Map<String,Integer> suitmap = new HashMap<>();
	public static Map<Integer, String > rankings = new HashMap<>();
	
	//int b/w 2-14 and suit type (string)
	public Card(int value,String type){
		//initialize the card logic 
		init();
		//assign the card fields
		if(value>14 || value<2){System.out.println("INVALID CARD RANK");}
		else{this.rank = value;}
		this.suit = types[suitmap.get(type)];
		
	}
	
	void init(){
		String [] suits = {"S","H","C","D"};
		int i = 0;
		for(String s : suits){suitmap.put(s,i);i++;}
		i=14;
		for(String t : Card.facecards){rankings.put(i,t);i--;}
	}
	
	void showMe(){
	    if(this.rank<10){System.out.println(this.rank+" "+this.suit);}
	    else if(this.rank==11){System.out.println("J "+this.suit);}
	    else if(this.rank==12){System.out.println("Q "+this.suit);}
	    else if(this.rank==13){System.out.println("K "+this.suit);}
	    else if(this.rank==14){System.out.println("A "+this.suit);}
	    }
}