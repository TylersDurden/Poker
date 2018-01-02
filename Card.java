//package Poker.Neural;
import java.util.*;

/** <CARD>
@ScottRobbins  */
public class Card {
    
    int Rank;
    String type;                  /**Spades,Hearts,Diamonds,Clubs */
    public static final String [] suits = {"S","H","D","C"};
    
    
    public Card(int rank, String kind){
    
        if(isRank(rank) && isSuit(kind)){
           this.Rank = rank;
           this.type = kind;
        }else{
            System.out.println("Card Creation Error!");
            System.exit(0);
        }
    
        
    }
    
    boolean isRank(int rank){
        boolean valid = false;
        if(rank>0 && rank<=14){ valid=true; }
        return valid;
    }
    
    boolean isSuit(String kind){
        boolean valid = false;
        
        for(String suit: suits){
            if(suit.compareTo(kind)==0){valid = true;}
        }
        return valid;
    }
    
    public void showMe(){System.out.print(this.Rank+this.type);}
    
    Vector<String> compareCards(){
        Vector<String> results = new Vector<>();
        
        return results;
    }
 
    
}

