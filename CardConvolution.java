//package Poker.Neural;
import java.util.*;

public class CardConvolution {
    
    Vector<Card> highest = new Vector<>();
    Vector<Card> pair    = new Vector<>();//
    Vector<Card> twoPair = new Vector<>();//
    Vector<Card> tres    = new Vector<>();// Three of a Kind
    Vector<Card> flush   = new Vector<>();//
    Vector<Card> strt    = new Vector<>();// Straight
    Vector<Card> house   = new Vector<>();// Full House 
    Vector<Card>strFlush = new Vector<>();// Straight Flush 
    Vector<Card> quads   = new Vector<>();// Four of a Kind  
    Vector<Card> rylFlush = new Vector<>();// Royal Flush
    
    Map <String, Integer> flushCount = new HashMap<>();
    Map <String, Vector<Card>> convo = new HashMap<>();
    Vector<Card> implicitPlayerCards = new Vector<>();
    
    
    public CardConvolution(Vector<Player>players){
                               
        //Because a convolution may be called before all cards have been dealt
        //a 2card vector is extracted from input representing player hand 
        
        System.out.print("\n**LAYER 2: Starting Convolution:\n[PLAYER1]\n");
        System.out.println("PAIR: ");
        for(Card pc:players.get(0).pair){pc.showMe();}
        System.out.println("\n[PLAYER2]\nPAIR: ");
        for(Card pc2 : players.get(1).pair){pc2.showMe();}
    }
    
    /** <_CONVOLVE_>
    Scan through each possible hand vector of all players
    and accumulate possible outs to improve each card vector 
    */
    public void convolve(Player player){
        
        for(Card a : player.pair){
            for(Card b : player.pair){
                if(a.Rank == b.Rank &&(a.type == b.type)){
                    player.pair.remove(a);
                }
                if(a.Rank != b.Rank){player.twoPair.add(a);}
            }
        } 
        for(Card b : player.flush){
            
        }
        
    }
    
    
    //public void firstLayerPlayerConvolve(Player p){}
    
    
    public static void main(String[]args){}
}
