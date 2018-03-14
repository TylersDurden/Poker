import java.util.*;

/**
 * AI - Brains for the game
 * 
 * @dependencies Deck.java, Card.java
 * @param Deck
 * @author ScottRobbins For more information:
 *         {@link https://github/com/TylersDurden/Poker}
 */
public class AI {

	public static Vector<Card> hand = new Vector<>();

	public static Vector<Card> highest   = new Vector<>();
	public static Vector<Card> pair      = new Vector<>();
	public static Vector<Card> twopair   = new Vector<>();
	public static Vector<Card> threekind = new Vector<>();
	public static Vector<Card> flush     = new Vector<>();
	public static Vector<Card> straight  = new Vector<>();
	public static Vector<Card> fullhouse = new Vector<>(); 
	public static Vector<Card> sflush    = new Vector<>();
	public static Vector<Card> quads     = new Vector<>();
	public static Vector<Card> rylflush  = new Vector<>();

	public String name;

	/**@param Player  */
	public AI(Player bot) {
		this.name = bot.player;
		/** Display the player's name and hand */
		System.out.println("- - - - - [AI] - - - - -");
		System.out.print(bot.player + " has:\n");
		//for (Card c : bot.hand) {c.showMe();}
		/** Initialize this bot's AI for the hand it holds */
		init(bot.hand);

	}

	/** <INITIALIZE> the AI with a hand of cards
	 * @param Vector  */
	void init(Vector<Card> h) {
		// Make sure the hand is correct size first
		if (h.size() != 2) {
			System.out.println("Hand is invalid");
		} else {
			AI.hand = h;
			if (h.get(0).rank == h.get(1).rank) {// pair!
				this.pair.add(h.get(0));
				this.pair.add(h.get(1));
			}
			if (h.get(0).rank > 10) {
				this.highest.add(h.get(0));
			}
			if (h.get(1).rank > 10) {
				this.highest.add(h.get(1));
			}
			if (h.get(0).suit.compareTo(h.get(1).suit) == 0) {
				this.flush.add(h.get(0));
				this.flush.add(h.get(1));
			}
		}

		// Debug printouts
		if (this.pair.size() > 0) {
			System.out.println("Pair of " + this.pair.get(0).rank + "s");
		}
		if (this.highest.size() > 0) {
			System.out.println("Has " + this.highest.size() + " Face cards.");
		}
		if (this.flush.size() > 0) {
			System.out.println("Has " + this.flush.size() + " " + this.flush.get(0).suit);
		}
	}
	void reviewTable(Vector<Card> dealt) {
		System.out.println("---[" + this.name + " reviews hand]---");

	}
	/**
	 * Here I want to use CardCounter to run experiments and figure out the
	 * best way to represent a hand.
	 */
	public static void main(String[] args) {
		if(args.length>0){
		    
		}
		else{new CardCounter(0);}
	}
 /**
  * CardCounter is how all the potentials outs for every given card in a 
  * deck is quickly generated. This way figuring out the probability of
  * hitting a given hand is as easy as looking up the number of outs 
  * for the given cards from the map corresponding the logic of THAT type
  * of Hand (i.e. a pair, flush, etc.) 
  * @author root
  *<-------------------------------------------------------------------->
  */
	public static class CardCounter implements Runnable {

		// private Deck simdeck = new Deck();
		private Map<Integer, Vector<String>> hashdeck = new HashMap<>();
		/** <DataStructures_for_OUTS> */
		private Map<Card, Vector<Card>> paired = new HashMap<>();
		private Map<Card, Vector<Card>> suited = new HashMap<>();
		private Map<Card, Vector<Card>> strait = new HashMap<>();
		private static HAND h;
		
		public int BATCH_SIZE;

        /** Construct CardCounter*/
		public CardCounter(int trainingDataBatchSize) {
		    if(trainingDataBatchSize<=0){this.BATCH_SIZE=1;}
		    else{this.BATCH_SIZE=trainingDataBatchSize;}
			Vector<Card> testdeck = new Vector<>();
			// Create the HashDeck
			for (int i = 2; i < 15; i++) {
				Vector<String> sopts = new Vector<>();
				for (String s : Deck.suitOpts) {
					sopts.add(s);
					testdeck.add(new Card(i, s));
				}
				hashdeck.put(i, sopts);
			}
			//System.out.println("CardCounter TestDeck initialized with " + testdeck.size() + " cards");

			Vector<Card> randeck = new Deck().self;
			for (Card h : testdeck) {
				Vector<Card> pairs = new Vector<>();
				Vector<Card> suits = new Vector<>();
				Vector<Card> strt  = new Vector<>();
				for (Card c : randeck) {
					if (h.rank == c.rank && (h.suit.compareTo(c.suit)!=0)) {pairs.add(h);}
					if (h.suit.compareTo(c.suit)==0 && 
					(h.rank != c.rank)) {suits.add(h);}
					if(Math.abs(h.rank - c.rank)<=4 && 
					(h.rank!=c.rank)){strt.add(h);}
				}
				this.suited.put(h, suits);
				this.paired.put(h, pairs);
				this.strait.put(h, strt);
			}

			run();
			//

		}

		public void run() {

			int i = 0;// Trial Number
			int badhand = 0;
			int badflop = 0;
			int badturn = 0;
			int badriv = 0;
			int badtab = 0;
			Deck simdeck = new Deck();
/////////////////////////////////////////////////////////////////////////////
			while (i < this.BATCH_SIZE) {
			    /** Deal a <HAND>*/
				Vector<Card> hand = simdeck.deal(2);
				if (hand.size() != 2) {badhand++;}
				else {
				CardCounter.h=new HAND(hand,
				                       this.paired, 
				                       this.suited,
				                       this.strait);
				}//AI.CardCounter is instantiated when dealt hand
				/** <FLOP> */
				Vector<Card> flop = simdeck.deal(3);
				if(flop.size() != 3){badflop++;}
				else {h.modifyTable(flop);}
					
				/** <TURN> */
				Vector<Card> turn = simdeck.deal(1);// add turn to table
				if (turn.size() != 1) {badturn++;}
				else {flop.add(turn.get(0));h.modifyTable(flop);}
				/** <RIVER> */
				Vector<Card> river = simdeck.deal(1);// add river
				if (river.size() != 1){badriv++;}
				Vector<Card> table = createTable(flop, river);
				if (table.size() != 5) {badtab++;
				} else {h.modifyTable(table);}
				
				i++;
				simdeck = new Deck();
			}

		}
//////////////////////////////////////////////////////////////////////	
        /** Update Table */
		Vector<Card> createTable(Vector<Card>flop, Vector<Card>more) {
			Vector<Card> table = new Vector<>();
			for (Card f : flop) {table.add(f);}
			for (Card t : more){table.add(t);}
			
			return table;
		}
	}

	/** Generic <HAND> class */
	public static class HAND {

		static Vector<Card> holding = new Vector<>();
		static Vector<Card> table = new Vector<>();

		public static boolean pocketpair = false;
		public static boolean pair = false;
		public static boolean twopair = false;
		public static boolean flush = false;
		public static boolean trips = false;

		static Vector<Card> pairouts = new Vector<>();
		static Vector<Card> suitouts = new Vector<>();
		static Vector<Card> flushout = new Vector<>();
		static Vector<Card> straightout = new Vector<>();

		static Map<Card, Vector<Card>> popts = new HashMap<>();
		static Map<Card, Vector<Card>> suits = new HashMap<>();
		static Map<Card, Vector<Card>> strtopts = new HashMap<>();
		//Store the accumulation of best possible hands as more info is added
		Map<String,Vector<Card>> besthand = new HashMap<>();
		
		
		

		public HAND(Vector<Card> cards, 
		            Map<Card, Vector<Card>> paired, 
		            Map<Card, Vector<Card>> suited,
		            Map<Card, Vector<Card>> straight){

			HAND.holding = cards;
			popts = paired;
			suits = suited;
			strtopts = straight;
			// Determine potential hands already, and outs to look for
			inthePocket();/** Initialze Cards in the Pocket */
			//for (Card cahd : HAND.holding) {cahd.showMe();}
		}

		void inthePocket() {
			// Clear old buffers
			pairouts.clear();
			suitouts.clear();
			flushout.clear();
			straightout.clear();
			//TODO: Double counting the strtopts for some reason!
            pocketpair = false;
            pair = false;
			// quick pocket pair check
			if (HAND.holding.get(0).rank == HAND.holding.get(1).rank) {
				pocketpair = true;
				Vector<Card> pp = new Vector<>();
				for(Card c : pp){pp.add(c);}
				this.besthand.put("pair",pp);
			}
			if (pocketpair) {pocketpair=true;} // only for debug
			else {
				for (Map.Entry<Card, Vector<Card>> entry : popts.entrySet()) {
					// pair outs for first card in hand
					if (entry.getKey().rank == HAND.holding.get(0).rank
				&& (entry.getKey().suit.compareTo(HAND.holding.get(0).suit) == 0)){
						for (Card c : entry.getValue()){pairouts.add(c);}
					} // pair outs for the second card in hand
					if (entry.getKey().rank == HAND.holding.get(1).rank &&
					(entry.getKey().suit.compareTo(HAND.holding.get(1).suit) == 0 
				                                         && pocketpair != true)) {
						for (Card d : entry.getValue()){pairouts.add(d);}
					}
				}
			} // Now look for potential flush outs
			for (Map.Entry<Card, Vector<Card>> entry : suits.entrySet()) {
				// get outs for suit match on first card in hand
				if (entry.getKey().rank == HAND.holding.get(0).rank
						&& (entry.getKey().suit.compareTo(HAND.holding.get(0).suit) == 0)) {
					for (Card f : entry.getValue()) {flushout.add(f);}
				} // check the second card in hand for suited outs
				if (entry.getKey().rank == HAND.holding.get(1).rank
						&& (entry.getKey().suit.compareTo(HAND.holding.get(1).suit) == 0)) {
					for (Card e : entry.getValue()) {flushout.add(e);}
				}
			}
			for(Map.Entry<Card,Vector<Card>>entry:strtopts.entrySet()){
			    //get the outs for a HIGHEST card in hand!!! Not Card? 
			    if(entry.getKey().rank==HAND.holding.get(0).rank && 
			    (entry.getKey().suit.compareTo(HAND.holding.get(0).suit)==0)){
			        for(Card f : entry.getValue()){straightout.add(f);}
			    }
			    if(entry.getKey().rank==HAND.holding.get(1).rank && 
			    (entry.getKey().suit.compareTo(HAND.holding.get(1).suit)==0)){
			        for(Card g : entry.getValue()){straightout.add(g);}
			    }
			}
		}

		/** Add new Cards to the Table */
		static void modifyTable(Vector<Card> cards) {
			HAND.table = cards;
		}

		/**
		 * Figure out which possible hands are emerging, and which of those are
		 * the most likely, and the strongest as well
		 */
		static Map<String,Vector<Card>> selfEval(Vector<Card>holds) {
		    Map<String,Vector<Card>> hands = new HashMap<>();
		    //Specific outs for hand
		    Vector<Card>pair = new Vector<>(); 
		    Vector<Card>suite = new Vector<>();
		    Vector<Card>tres = new Vector<>();
		    Vector<Card>line = new Vector<>();
		    /**Essentially just looking through the pairouts, suitouts, etc.
		     * vectors and see if any of them are on table. If a card from
		     * outs is there, add it to the corresponding vec of the Map
		     * map corresponding to that hand. This way, every time 
		     * selfEval() is called when new cards are on table, new potential
		     * hands are saved, and by effect the likelihood of hitting hands
		     * becomes more clear.  
		     Start by checking <PAIR> outs 
		     Now check for <FLUSH> outs. Use same algo as above
		    /** And also check if any <STRAIGHT> outs appeared on table */
		    List<Card> optdeck = new ArrayList<Card>(popts.keySet());
		    Vector<Vector<Card>> outs = new Vector<Vector<Card>>();
		    for(Card cr : optdeck){
		        if(cr.rank==holds.get(0).rank && cr.suit.compareTo(holds.get(0).suit)==0){
		            //Get outs for card found to be in hand
		          for(Card out : popts.get(cr)){pair.add(out);} 
		          for(Card out : suits.get(cr)){suite.add(out);}
		          if(holds.get(0).rank>holds.get(1).rank){
		              for(Card out : strtopts.get(cr)){line.add(out);}
		          }
		        }
		        if(cr.rank==holds.get(1).rank && cr.suit.compareTo(holds.get(1).suit)==0){
		          if(pocketpair){popts.get(cr).remove(cr);tres = popts.get(cr);}
		          else{for(Card out : popts.get(cr)){pair.add(out);}}
		          for(Card out : suits.get(cr)){suite.add(out);}
                  if(holds.get(0).rank<holds.get(1).rank){
                      for(Card out : strtopts.get(cr)){line.add(out);}}
                  }		        
		        }
		        
		        
		        
		    int ncards = 50-table.size();
		    double pairodd = (double)pair.size()/ncards*100;
		    double suitodd = (double)suite.size()/ncards*100;
		    double strtodd = (double)line.size()/ncards*100;
		    //^ Working, but TODO: have to remove repeats before
		    //using outs to make probabilities!!!
		    //Wont matter when building a hand though.. will it?
		 
		    System.out.println("["+
		    pairodd+"% pair] ["+suitodd+"% flush] ["+strtodd+"% straight]");
            hands.put("pair",pair);
            hands.put("flush",suite);
            hands.put("straight",line);
            /** Check table to see if any outs are hit */
            if(table.size()>0){
                for(Card card : table){
                    for(Card out : pair){//check for a pair
                        if(pocketpair){//check three of a kind isntead
                            HAND.pair=true;
                            if(card.rank==holds.get(0).rank){trips = true;}
                        } 
                        if(pocketpair==false && card.rank==out.rank && 
                           card.suit.compareTo(out.suit)==0){
                               HAND.pair = true;}
                    }//
                    for(Card out : suite){
                        
                    }
                    
                }
            }
            
            return hands;   
	    }      
		
		
		/** */
		Vector<Card> bestSelf() {
			Vector<Card> besthand = new Vector<Card>();

			return besthand;
		}
		
		static Vector<Card> removeRepeats(Vector<Card>VEC){
		   Vector<Card>out = new Vector<Card>();
		   Map<Integer,Card>vec = new HashMap<>();
		   int i = 0;
		   for(Card c : VEC){
		       vec.put(i,c);
		       i++;
		   }
		   i=0;
		   List<Integer>ind = new ArrayList<Integer>(vec.keySet());
		   Vector<Integer> removals = new Vector<>();
		   Collections.shuffle(ind);
		   for(Card crd : VEC){
		       for(Integer index : ind){
		           if(vec.get(index).rank==crd.rank &&
		           vec.get(index).suit.compareTo(crd.suit)==0){
		               removals.add(i);
		           }
		       }
		       i++;
		   }
		   for(Integer indice : removals){VEC.remove(indice);}
		   
		   return VEC;
		}

	}/** EndOfHAND*/
	
	public static class Evaluation implements Runnable{
	    
	    /**___________________________________________
	     *|<HAND>|<OUTS>|<HIT>|<Y/N?>|<weighted_score>|
         *|------|------|-----|------|----------------|
	     *|<pair>|_<#>__|_<#>_|__<#>_|_____<#>________|
         *|______|______|_____|______|________________|
                    ^^^ <:MASTER_TABLE:> ^^^
	     *1st column in the type of hand. 
	     *2nd column in the number of outs left
         *3rd column is whether this is the "best" choice of table <DYNAMIC!>
         *4th column is score of how STRONG hand ranking is, not likelihood 
	     */
	    Map<String,Vector<Integer>> mastertable = new HashMap<>();
	    Map<Integer,String>  handref     = new HashMap<>();
	    Vector<Card> besthand = new Vector<>();
	    
	    public static final String [] HANDS = {"highest","pair","twopair",
	                                           "threekind","straight","flush",
	                                           "fullhouse","quads","straightflush",
	                                           "royalflush"};
	    
	    public Evaluation(Vector<Card>hand,Vector<Card>table){
	        // First set up the 1st column of MASTER_TABLE
	        int index = 0;
	        Vector<Integer>rankings = new Vector<Integer>();
	        for(String opt : HANDS){
	            rankings.add(index);
	            handref.put(index,opt);
	            index++;}
	        mastertable.put("HANDS",rankings);
	        // Now configure 2nd Column
	        
	    }
	    public void run(){
	        
	    }
	}

}/** <ENDof_:<<AI><><> */