//package Poker.Neural;

import java.util.*;

/**
 * 
 */
public class CardClassification {

    static Vector<Card>         cardsIn = new Vector<>();
    static Vector<Integer>      ranks   = new Vector<>();
    static Vector<String>       suits   = new Vector<>();
    public Vector<Player>       players = new Vector<>();
    static Map<String, Integer> flush   = new HashMap<>();
    public Card                 highestCard;
    public int                 numplayers;
    public Player               BOT; 
    public Player               BOT2;


    public CardClassification(Vector<Card> cards,Vector<Player> players) {
        numplayers = players.size(); 
        CardClassification.cardsIn = cards;
        BOT = DataTrain.bots.get(0);
        BOT2 = DataTrain.bots.get(1);
        processInputCards();
    }

    public static void main(String[] args) {}

    /** */
    void processInputCards() {

        int hearts = 0;
        int diamonds = 0;
        int clubs = 0;
        int spades = 0;
        int index = 0;
        int revdex = cardsIn.size();
        
        System.out.print("Player 1 has: ");
        for(Card p1:BOT.hand){p1.showMe();}
        System.out.print("\nPlayer 2 has : ");
        for(Card p2 : BOT2.hand){p2.showMe();}
        System.out.print("\n|TABLE CARDS : ");
        for (Card c : CardClassification.cardsIn) {
            ranks.add(c.Rank);
            suits.add(c.type);
            System.out.print("{" + c.Rank + c.type + "} ");
            //quickly check table cards against themselves
            for(Card cs : CardClassification.cardsIn){
                //pair chk
                if(cs.Rank == c.Rank &&(cs.type != c.type)){
                    BOT.pair.add(cs); BOT2.pair.add(cs);
                    BOT.pair.add(c);  BOT2.pair.add(c);
                }//flush chk
                if(cs.type == c.type &&(cs.Rank != c.Rank)){
                    BOT.flush.add(cs); BOT.flush.add(c);
                    BOT2.flush.add(c); BOT2.flush.add(cs);
                }//straight chk
                if(Math.abs(cs.Rank - c.Rank)<=4 && (cs!=c)){
                    BOT.strt.add(cs); BOT.strt.add(c);
                    BOT2.strt.add(c); BOT2.strt.add(cs);
                }
            }
            if (c.type.compareTo("H") == 0) {hearts += 1;}
            if (c.type.compareTo("S") == 0) {spades += 1;}
            if (c.type.compareTo("D") == 0) {diamonds += 1;}
            if (c.type.compareTo("C") == 0) {clubs += 1;}
            if(BOT.hand.get(0).Rank==c.Rank){
                BOT.pair.add(c);
                BOT.pair.add(BOT.hand.get(0));
                }
            if(BOT.hand.get(1).Rank==c.Rank){
                BOT.pair.add(c);
                BOT.pair.add(BOT.hand.get(1));
            }//Now do BOT2
            if(BOT2.hand.get(0).Rank==c.Rank){
                BOT2.pair.add(c);
                BOT2.pair.add(BOT2.hand.get(0));
                }
            if(BOT2.hand.get(1).Rank==c.Rank){
                BOT2.pair.add(c);
                BOT2.pair.add(BOT2.hand.get(1));
            }//Now check BOT for table flushes
            if(BOT.hand.get(0).type==c.type){
                BOT.flush.add(BOT.hand.get(0));
                BOT.flush.add(c);
            }
            if(BOT.hand.get(1).type == c.type){
                BOT.flush.add(BOT.hand.get(1));
                BOT.flush.add(c);
            }//now for BOT2 table flushes
            if(BOT2.hand.get(0).type == c.type){
                BOT2.flush.add(BOT2.hand.get(0));
                BOT2.flush.add(c);
            }
            if(BOT2.hand.get(1).type == c.type){
                BOT2.flush.add(BOT2.hand.get(1));
                BOT2.flush.add(c);
            }//check both bot hands for a straight 
            if(Math.abs(BOT.hand.get(0).Rank - c.Rank)<=4){
                BOT.strt.add(BOT.hand.get(0));
                BOT.strt.add(c);
            }
            if(Math.abs(BOT.hand.get(1).Rank - c.Rank)<=4){
                BOT.strt.add(BOT.hand.get(1));
                BOT.strt.add(c);
            }
            if(Math.abs(BOT2.hand.get(0).Rank - c.Rank)<=4){
                BOT2.strt.add(BOT2.hand.get(0));
                BOT2.strt.add(c);
            }
            if(Math.abs(BOT2.hand.get(1).Rank - c.Rank)<=4){
                BOT2.strt.add(BOT2.hand.get(1));
                BOT2.strt.add(c);
            }
            
            //chk player cards against themselves outside of loop    
            index++;
        }
        if(BOT.hand.get(0).Rank == BOT.hand.get(1).Rank){
            System.out.println("BOT 1 has a pocket pair of "+
            BOT.hand.get(0).Rank+"s");
            BOT.pair.add(BOT.hand.get(0));
            BOT.pair.add(BOT.hand.get(1));
        }
        if(BOT2.hand.get(0).Rank == BOT2.hand.get(1).Rank){
            System.out.println("BOT 2 has a pocket pair of "+
            BOT2.hand.get(0).Rank+"s");
            BOT2.pair.add(BOT2.hand.get(0));
            BOT2.pair.add(BOT2.hand.get(1));
        }
        System.out.print("|\n");
        int highestRank = Collections.max(ranks);
        System.out.println("\n***************************\n" + "Highest Rank is " + highestRank);
        System.out.println(hearts + " hearts on table");
        System.out.println(diamonds + " diamonds on table");
        System.out.println(clubs + " clubs on table");
        System.out.println(spades + " spades on table");
        System.out.print("\n***************************\n");
        /**Cards.suits = <Spades,Hearts,Diamonds,Clubs> */
        flush.put(Card.suits[0], spades);
        flush.put(Card.suits[1], hearts);
        flush.put(Card.suits[2], diamonds);
        flush.put(Card.suits[3], clubs);
        
       
    }



}
/** <EndOF_CardClassification_>*/
