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
	public static final String [] types= {"S","H","C","D"};
	public static final String [] facecards = {"Ace","King","Queen","Jack"};
	
	public static Map<String,Integer> suitmap = new HashMap<>();
	public static Map<Integer, String > rankings = new HashMap<>();
	
	//int b/w 2-14 and suit type (string)
	public Card(int value,String type){
		//initialize the card logic 
		init();
		//assign the card fields
		if(value>14 || value<2){System.out.println("INVALID CARD RANK: "+value);}
		else{this.rank = value;}
		try{this.suit = types[suitmap.get(type)];}
		catch(NullPointerException e){System.out.println("BAD SUIT: "+type);}
		
	}
	
	void init(){
		String [] suits = {"S","H","C","D"};
		int i = 0;
		for(String s : suits){suitmap.put(s,i);i++;}
		i=14;
		for(String t : Card.facecards){rankings.put(i,t);i--;}
	}
	
	void showMe(){
	    if(this.rank<10){System.out.print(this.rank+" "+this.suit);}
	    else if(this.rank==11){System.out.print("J"+this.suit+" ");}
	    else if(this.rank==12){System.out.print("Q"+this.suit+" ");}
	    else if(this.rank==13){System.out.print("K"+this.suit+" ");}
	    else if(this.rank==14){System.out.print("A"+this.suit+" ");}
	    }
}