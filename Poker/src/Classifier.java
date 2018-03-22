import java.util.*;
import java.io.*;
import java.nio.*;

public class Classifier {

    public Map<String, Vector<Card>> classification = new HashMap<>();

    public Classifier(Map<Integer, Vector<Neuron.Perceptron>> smap, Vector<Vector<Card>> sdat) {
        //Want to work with the Decision Tree idea first
       // System.out.println(sdat.size() + " Hands being fed into the decision tree.");
        for (Vector<Card> ROUND : sdat) {
            new DecisionTree(ROUND);
        }
    }

    public static void main() {

    }

    public static class DecisionTree implements Runnable {

        public Vector<Card> STATE   = new Vector<>();
        public int          STATE_NUM;

        boolean             preflop = false;
        boolean             flop    = false;
        boolean             turn    = false;
        boolean             river   = false;
        
        public static Vector<Card> hand = new Vector<>();
        // ^ The Final Answer from the following options 
	    public Vector<Card> highest   = new Vector<>();
	    public Vector<Card> pair      = new Vector<>();
	    public Vector<Card> twopair   = new Vector<>();
	    public Vector<Card> threekind = new Vector<>();
	    public Vector<Card> flush     = new Vector<>();
	    public Vector<Card> straight  = new Vector<>();
	    public Vector<Card> fullhouse = new Vector<>(); 
	    public Vector<Card> sflush    = new Vector<>();
	    public Vector<Card> quads     = new Vector<>();
	    public Vector<Card> rylflush  = new Vector<>();
        

        public DecisionTree(Vector<Card> cards) {
            this.STATE = cards;
            this.STATE_NUM = cards.size();

            if (this.STATE_NUM == 2) {
                this.preflop = true;
            }
            if (this.STATE_NUM == 5) {
                this.flop = true;
            }
            if (this.STATE_NUM == 6) {
                this.turn = true;
            }
            if (this.STATE_NUM == 7) {
                this.river = true;
            }


            if (this.preflop || this.flop || this.turn || this.river) {
                run();
            } else {
                System.out.println("ERROR");
            }
        }

        public void run() {

            /** Two decision trees. One for ranks, and one for suits
             Pair,Two Pair, Trips,Straight,Full House,4 Kind: <RANK> related
             Flush, Straight Flush, Royal Flush: <SUIT> related
             Clearly the rank tree is far more complicated. 
             Start with the suit tree to see if it works. */

            //Decision tree for every step
            if (this.preflop) {
                preflop();
            }
            if (this.flop) {
                flop();
            }
            if (this.turn) {
                turn();
            }
            if (this.river) {
                river();
            }


        }

        /**<STATE0_Tree> */
        void preflop() {}

        /**<STATE1_Tree> */
        void flop() {/*Unneccessary bc I'm putting labels on final hands*/}

        /** <STATE2_Tree> */
        void turn() {/*Unneccessary bc I'm putting labels on final hands*/}

        /** <STATE3_Tree>
        As the final addition to the table, the river decision tree
        will ultimately decide the final hand made with cards available*/
        void river(){
            
            /** Alt: <ForLoopConvolution?>*/
            //Pair mappings - rank to the cards making that pair
            Map<Integer,Vector<Card>> pairs = new HashMap<>();
            Map<String, Vector<Card>> suits = new HashMap<>();
            Map<Integer,Vector<Card>> stray = new HashMap<>();
            
            boolean hip = false;//hit pair 
            boolean pair = false;
            boolean toopair = false;
            boolean threek = false;
            boolean flushed = false;
            boolean strayt = false;
            boolean full = false;
            boolean fourk = false;
            boolean strflush = false;
            boolean rylflush = false;
            for(Card c : this.STATE){
                Vector<Card>paired = new Vector<>();
                Vector<Card>suited = new Vector<>();
                Vector<Card>strait = new Vector<>();
                boolean hitp = false;
                boolean hits = false;
                boolean near = false;
                for(Card d : this.STATE){
                    if(c.sameRank(d)){//check ranks
                        paired.add(d);
                        hitp = true;
                    }//Now Check suits
                    if(c.sameSuit(d)){
                        suited.add(d);
                        hits = true;
                    }//Check if straight possible
                    if(c.proximity(d)<=4){
                        strait.add(d);
                        near = true;
                    }
                }
                if(hitp){paired.add(c);}
                if(hits){suited.add(c);}
                if(near){strait.add(c);}
                pairs.put(c.rank,paired);
                suits.put(c.suit,suited);
                stray.put(c.rank,strait);
                hip = hitp;
            }
          
            // Based on Map configs, most hands can be classified already
            //TODO: Write this identification process! 
            /** Steps:  
             * <[1]ID:Pair,TwoPair,3Kind,FullHouse,4Kind>
             * <[2]ID:Flush,StraightFlush,RoyalFlush>---\  These two should 
             * <[3]ID:Straight,StraightFlush>-----------/  work together
             *<PAIR_CHECKS>*/
            Vector<Card>too = new Vector<>();
            for(Map.Entry<Integer,Vector<Card>>entry:pairs.entrySet()){
                if(entry.getValue().size()==2 && !pair && ! toopair){this.pair = entry.getValue();pair=true;}
                if(entry.getValue().size()==2 && pair){toopair=true;too = entry.getValue();}
                if(entry.getValue().size()==3){this.threekind = entry.getValue();threek=true;}
                if(entry.getValue().size()==4){this.quads = entry.getValue();fourk=true;}
                if(threek && pair){
                	for(Card c:this.threekind){this.fullhouse.add(c);}
                	for(Card d:this.pair){this.fullhouse.add(d);}
                	full=true;
                }
                if(toopair){for(Card d : too){this.twopair.add(d);}}
            }
            /**<TwoPairCleanUp>*/
          
            
            /** <SUIT_CHECKS> **/
           for(Map.Entry<String,Vector<Card>>entry:suits.entrySet()){
               if(entry.getValue().size()>4){flushed=true;flush = entry.getValue();}
           }
            
            
            /** <STRAIGHT_CHECKS>*/
            int max_hits = 0;
            Map<Integer,String> CARDS = new HashMap<>();
            for(Map.Entry<Integer,Vector<Card>>entry:stray.entrySet()){
                if(entry.getValue().size()>4){
                    for(Card c : entry.getValue()){CARDS.put(c.rank,c.suit);}
                    }
                }
            //Make a List of unique ranks (no pairs)
            List<Integer>RANKS = new ArrayList<Integer>(CARDS.keySet());
            Collections.sort(RANKS);
            boolean str = false;
            int index = 0;
            for(Integer r : RANKS){
                if(index>0){
                    if(Math.abs(r-RANKS.get(index-1))==1){this.straight.add(new Card(r,CARDS.get(r)));}
//                    else{new Card(r,CARDS.get(r)).showMe();}
                }
                index += 1;
            }
            if(this.straight.size()>4){strayt=true;}else{strayt=false;}
            if(strayt && flushed){strflush=true;}
            if(strflush|| flushed){ /** <ROYALFLUSHCHECK> */
            	boolean ace = false;
            	boolean king = false;
            	boolean queen = false;
            	boolean jack = false;
            	boolean ten = false;
               for(Card c : this.STATE){
            	   if(c.rank==14){ace=true;}
            	   if(c.rank==13){king=true;}
            	   if(c.rank==12){queen=true;}
            	   if(c.rank==11){jack=true;}
            	   if(c.rank==10){ten=true;}
               }
               if(ace && king && queen && jack && ten){rylflush=true;} 
            }

                


            /** <PairNotWorking>*/
            if(pair && !toopair && !threek && !flushed && !strayt){System.out.print("Pair");}
            /** <TwoPairWorking>*/
            if(toopair && !threek){System.out.println("Two Pair");
            for(Card c: this.pair){c.showMe();}for(Card d:this.twopair){d.showMe();}
            	System.out.println("");
            }          
            
            /**<ThreeKindWorking> */
            if(threek && !pair){System.out.println("Three of a Kind");for(Card c: this.pair){c.showMe();}}
            /** <FlushWorking> */
            if(flushed && !rylflush && !strflush){System.out.println("Flush");} 
            else{flushed=false;}
            /** <StraightWorking>*/
            if(strayt && !rylflush && !flushed && !strflush){System.out.println("Straight");}
            if(full){System.out.println("Full House");}
            if(fourk){System.out.println("Four of a Kind");}
            if(strflush){System.out.println("Straight Flush");}
            if(rylflush){System.out.println("Royal Flush");}
            if(!pair && !toopair && !threek && !flushed && !full &&!fourk  && !strayt && !rylflush){
            	System.out.println("High Card?");}
           //System.out.println("\n");
           // System.out.println("\n");
        }

    }
}
 