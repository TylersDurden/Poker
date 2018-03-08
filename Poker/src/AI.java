import java.util.*;

/**AI - Brains for the game 
 *@dependencies Deck.java, Card.java 
 *@param Deck 
 *@author ScottRobbins
 * For more information: 
 * {@link https://github/com/TylersDurden/Poker}
 */
public class AI {

    public Vector<Card> hand = new Vector<>();
    
    public Vector<Card> highest = new Vector<>();
    public Vector<Card> pair    = new Vector<>();//
    public Vector<Card> twoPair = new Vector<>();//
    public Vector<Card> tres    = new Vector<>();// Three of a Kind
    public Vector<Card> flush   = new Vector<>();//
    public Vector<Card> strt    = new Vector<>();// Straight
    public Vector<Card> house   = new Vector<>();// Full House 
    public Vector<Card>strFlush = new Vector<>();// Straight Flush 
    public Vector<Card> quads   = new Vector<>();// Four of a Kind  
    public Vector<Card> rylFlush = new Vector<>();// Royal Flush

    

    public AI(Player bot){
        
        /** Display the player's name and hand */
        System.out.println("- - - - - [AI] - - - - -");
        System.out.print(bot.player+" has:\n");
        for(Card c:bot.hand){c.showMe();}
        
        /** Initialize this bot's AI for the hand it holds */
        init(bot.hand);
        
    }
    
    void init(Vector<Card>h){
        //Make sure the hand is correct size first
        if(h.size()!=2){System.out.println("Hand is invalid");}
        else{
            if(h.get(0).rank==h.get(1).rank){//pair! 
                this.pair.add(h.get(0));
                this.pair.add(h.get(1));
                }
            if(h.get(0).rank>10){this.highest.add(h.get(0));}
            if(h.get(1).rank>10){this.highest.add(h.get(1));}
            if(h.get(0).suit.compareTo(h.get(1).suit)==0){
                this.flush.add(h.get(0));
                this.flush.add(h.get(1));
            }
        }
        
        //Debug printouts 
        if(this.pair.size()>0){System.out.println("Pair of "+this.pair.get(0).rank+"s");}
        if(this.highest.size()>0){System.out.println("Has "+this.highest.size()+" Face cards.");}
        if(this.flush.size()>0){System.out.println(this.flush.size()+" "+this.flush.get(0).suit);}
    }
    
    void reviewTable(Vector<Card>dealt){
        
    }
    
    void evaluate(){
        
    }
    
    

    

    public static void main(String[] args) {}

}
