import java.util.*;

/**AI - Brains for the game 
 *@dependencies Deck.java, Card.java 
 *@param Deck 
 *@author ScottRobbins
 * For more information: 
 * {@link https://github/com/TylersDurden/Poker}
 */
public class AI {

    public static Vector<Card> hand = new Vector<>();
    
    public Vector<Card> highest = new Vector<>();
    public Vector<Card> pair    = new Vector<>();//
    public Vector<Card> twopair = new Vector<>();//
    public Vector<Card> tres    = new Vector<>();// Three of a Kind
    public Vector<Card> flush   = new Vector<>();//
    public Vector<Card> straight    = new Vector<>();// Straight
    public Vector<Card> fullhouse   = new Vector<>();// Full House 
    public Vector<Card>strflush = new Vector<>();// Straight Flush 
    public Vector<Card> quads   = new Vector<>();// Four of a Kind  
    public Vector<Card> rylflush = new Vector<>();// Royal Flush

    public String name;

    public AI(Player bot){
        this.name = bot.player;
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
            this.hand = h;
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
        if(this.flush.size()>0){System.out.println("Has "+this.flush.size()+" "+this.flush.get(0).suit);}
    }
    
     void reviewTable(Vector<Card>dealt){
       
      
        System.out.println("---["+this.name+" reviews hand]---");
        
    }
    
    public static void main(String[] args) {
        
        /**Here I want to use CardCounter to run experiments and
         figure out the best way to represent a hand. 
         */
        new CardCounter();
        
    }
    
    public static class CardCounter implements Runnable {
        
        //private Deck simdeck = new Deck();
        private Map<Integer,Vector<String>> hashdeck = new HashMap<>();
        /** <DataStructures_for_OUTS> */
        private Map<Card,Vector<Card>> paired = new HashMap<>();
        private Map<Card,Vector<Card>> suited = new HashMap<>();
        private Map<Card,Vector<Card>> strait = new HashMap<>();
        
        public CardCounter(){
            Vector<Card>testdeck = new Vector<>();
            //Create the HashDeck 
            int k = 0;
            for(int i=2;i<15;i++){
                Vector<String>sopts = new Vector<>();
                for(String s : Deck.suitOpts){sopts.add(s);testdeck.add(new Card(i,s));}
                hashdeck.put(i,sopts);
                
                
            }
            System.out.println("CardCounter TestDeck initialized with "+testdeck.size()+" cards");
            // Define outs for each card in constructor 
             /** <Algorithm?> It's kind of a classificaton problem...
           <[1]> Build the outs */
          Vector<Card>randeck = new Deck().self;
          for(Card h : testdeck){
              Vector<Card> pairs = new Vector<>(); 
              Vector<Card> suits = new Vector<>();
              for(Card c : randeck){
                 if(h.rank == c.rank && (h.suit!=c.suit)){pairs.add(c);}
                 if(h.suit == c.suit && (h.rank != c.rank)){suits.add(c);}
              }
              this.suited.put(h,suits);
              this.paired.put(h,pairs);
          }
          
            
            run();
            //
            
        }
        
        public void run(){
            
            int i = 0;// Trial Number 
            int N = 2;//N Trials 
            int badhand = 0;
            int badflop = 0;
            int badturn = 0;
            int badriv  = 0;
            int badtab  = 0;
             Deck simdeck = new Deck();
            
            while(i<N){
                //System.out.println("-------New Hand!--------");
                Vector<Card> hand = simdeck.deal(2);
                System.out.println("HAND:");
                for(Card hcs : hand){hcs.showMe();}
                //System.out.println(hand.get(0).rank+hand.get(0).suit);
                if(hand.size()!=2){badhand++;}
                /** <FLOP> */
                Vector<Card> flop = simdeck.deal(3);
                if(flop.size()!=3){badflop++;}
                /** <TURN> */
                Vector<Card> turn = simdeck.deal(1);//add turn to table
                if(turn.size()!=1){badturn++;}
                /** <RIVER> */
                Vector<Card> river = simdeck.deal(1);//add river 
                if(river.size()!=1){badriv++;}
                Vector<Card> table = createTable(flop,turn,river);
                if(table.size()!=5){badtab++;}
                
                System.out.println("SHOW TABLE:");
                for(Card tcs : table){tcs.showMe();}
                /** Generate Training data so return HAND objects */
                evaluateHand(hand,table); 
                i++;
                simdeck = new Deck();
            }
            /** Err Report printout
            //System.out.println("* * * {ERROR REPORT} * * *");
            //System.out.println(badhand+" malformed hands out of "+N);
            //System.out.println(badflop+" malformed flops out of "+N);
            //System.out.println(badturn+" malformed turns out of "+N);
            //System.out.println(badriv +" malformed rivers out of "+N);
            //System.out.println(badtab+ " malformed tables out of "+N);*/
        }
        
        Vector<Card> createTable(Vector<Card>flop,Vector<Card>turn,Vector<Card>river){
            Vector<Card>table = new Vector<>();
            for(Card f : flop){table.add(f);}
            for(Card t : turn){table.add(t);}
            for(Card r : river){table.add(r);}
            return table;
        }
    
        void evaluateHand(Vector<Card>hand,Vector<Card>table){
           Vector<Card> pairouts = new Vector<>();
           Vector<Card> flushout = new Vector<>();
           for(Map.Entry<Card,Vector<Card>>entry:this.paired.entrySet()){
            
            if(entry.getKey().rank==hand.get(0).rank &&
            (entry.getKey().suit.compareTo(hand.get(0).suit)==0)){
                for(Card c : entry.getValue()){pairouts.add(c);}    
             }
             if(entry.getKey().rank==hand.get(1).rank &&
            (entry.getKey().suit.compareTo(hand.get(1).suit)==0)){
                for(Card c : entry.getValue()){pairouts.add(c);} 
             }
         }
         for(Map.Entry<Card,Vector<Card>>entry:this.suited.entrySet()){
             if(entry.getKey().suit.compareTo(hand.get(0).suit)==0&& 
               (entry.getKey().rank!=hand.get(0).rank)){
                flushout.add(entry.getKey());              
             }
             if(entry.getKey().suit.compareTo(hand.get(1).suit)==0 &&
             (entry.getKey().rank!=hand.get(1).rank)){
                flushout.add(entry.getKey());
             }
         }
             
         Vector<Card>pairs = new Vector<>();
         Vector<Card>flushed = new Vector<>();
         
         //Now see if any of those outs were hit from cards on table
         for(Card card : table){
             for(Card pout : pairouts){
                 if(card.rank==pout.rank && card.suit.compareTo(pout.suit)==0){
                     pairs.add(card); pairs.add(pout);
                 }
             }
             for(Card fout : flushout){
                 if(card.rank==fout.rank && card.suit.compareTo(fout.suit)==0){
                     flushed.add(fout); flushed.add(card);
                 }
             }
             if(pairs.size()>0){
                 System.out.println("Pair of "+pairs.get(0).rank);
             }
             
         }
         // Also consider cars on table with themselves! 
         for(Card t : table){
             for(Card cs0:table){
                 if(t.rank==cs0.rank && t.suit.compareTo(cs0.suit)!=0){pairs.add(t); pairs.add(cs0);}
                 
             }
         }
            
        }   

    }
    
    /** Generic <HAND> class */
   public static class HAND {
       
       public HAND(Vector<Card>cards){
           
       }
   }

}/** <ENDof_:<<AI><><>*/