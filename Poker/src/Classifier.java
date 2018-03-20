import java.util.*;
import java.io.*;
import java.nio.*;

public class Classifier {

    public Map<String, Vector<Card>> classification = new HashMap<>();

    public Classifier(Map<Integer, Vector<Neuron.Perceptron>> smap, Vector<Vector<Card>> sdat) {
        //Want to work with the Decision Tree idea first
        System.out.println(sdat.size() + " Hands being fed into the decision tree.");
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
            boolean twopair = false;
            boolean threek = false;
            boolean flushed = false;
            boolean strayt = false;
            boolean full = false;
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
            for(Map.Entry<Integer,Vector<Card>>entry:pairs.entrySet()){
                if(entry.getValue().size()==2){this.pair = entry.getValue();pair=true;}
                if(entry.getValue().size()==2 && pair && entry.getKey()!=this.pair.get(0).rank){
                    this.twopair = entry.getValue();}
                if(entry.getValue().size()==3){this.threekind = entry.getValue();threek=true;}
                if(entry.getValue().size()==4){this.quads = entry.getValue();}
                 
            }
            
            /** <STRAIGHT_CHECKS>*/
            //Eliminate partial straights
            
            /** <PAIR> Looks God*/
            if(pair && !twopair){for(Card c : this.pair){c.showMe();}}
            /** <Two_PAIR> Looks Good*/
            if(pair && twopair){System.out.println("Two Pair");}
                
            /**<ThreeKind> Working */
            if(threek){System.out.println("Three of a Kind");}
            /** <Flush> Working */
            if(this.flush.size()==5){System.out.println("Flush");}
            /** <Straight> TODO: Unfinished */
            //if(strayt){System.out.println("Straight");}
            if(full){System.out.println("Full House");}
            
            System.out.println("\n--------------------------------");
        }

    }
}
