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

	public Vector<Card> highest = new Vector<>();
	public Vector<Card> pair = new Vector<>();//
	public Vector<Card> twopair = new Vector<>();//
	public Vector<Card> tres = new Vector<>();// Three of a Kind
	public Vector<Card> flush = new Vector<>();//

	public String name;

	/**@param Player  */
	public AI(Player bot) {
		this.name = bot.player;
		/** Display the player's name and hand */
		System.out.println("- - - - - [AI] - - - - -");
		System.out.print(bot.player + " has:\n");
		for (Card c : bot.hand) {
			c.showMe();
		}
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
   /**@deprecated */
	void reviewTable(Vector<Card> dealt) {
		System.out.println("---[" + this.name + " reviews hand]---");

	}
	/**
	 * Here I want to use CardCounter to run experiments and figure out the
	 * best way to represent a hand.
	 */
	public static void main(String[] args) {
		new CardCounter();
	}
 /**
  * CardCounter is how all the potentials outs for every given card in a 
  * deck is quickly generated. This way figuring out the probability of
  * hitting a given hand is as easy as looking up the number of outs 
  * for the given cards from the map corresponding the logic of THAT type
  * of Hand (i.e. a pair, flush, etc.) 
  * @author root
  *
  */
	public static class CardCounter implements Runnable {

		// private Deck simdeck = new Deck();
		private Map<Integer, Vector<String>> hashdeck = new HashMap<>();
		/** <DataStructures_for_OUTS> */
		private Map<Card, Vector<Card>> paired = new HashMap<>();
		private Map<Card, Vector<Card>> suited = new HashMap<>();
		private Map<Card, Vector<Card>> strait = new HashMap<>();

		private static HAND h;

		public CardCounter() {
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
			System.out.println("CardCounter TestDeck initialized with " + testdeck.size() + " cards");

			/*
			 * <Algorithm?> It's kind of a classification problem... <[1]> -
			 * Build the outs
			 */
			Vector<Card> randeck = new Deck().self;
			for (Card h : testdeck) {
				Vector<Card> pairs = new Vector<>();
				Vector<Card> suits = new Vector<>();
				for (Card c : randeck) {
					if (h.rank == c.rank && (h.suit != c.suit)) {
						pairs.add(c);
					}
					if (h.suit == c.suit && (h.rank != c.rank)) {
						suits.add(c);
					}
				}
				this.suited.put(h, suits);
				this.paired.put(h, pairs);
			}

			run();
			//

		}

		public void run() {

			int i = 0;// Trial Number
			int N = 2;// N Trials
			int badhand = 0;
			int badflop = 0;
			int badturn = 0;
			int badriv = 0;
			int badtab = 0;
			Deck simdeck = new Deck();

			while (i < N) {
				Vector<Card> hand = simdeck.deal(2);
				// System.out.println("HAND:");
				// for(Card hcs : hand){hcs.showMe();}
				if (hand.size() != 2) {
					badhand++;
				} else {
					CardCounter.h = new HAND(hand, this.paired, this.suited);
				}
				// Sleep is to ensure printout happens
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				/** <FLOP> */
				Vector<Card> flop = simdeck.deal(3);
				if (flop.size() != 3) {
					badflop++;
				} else {
					HAND.modifyTable(flop);
					HAND.selfEval();
				}/** <TURN> */
				Vector<Card> turn = simdeck.deal(1);// add turn to table
				if (turn.size() != 1) {
					badturn++;
				} else {
					HAND.modifyTable(createTable(flop, turn, new Vector<Card>()));
					HAND.selfEval();
				}/** <RIVER> */
				Vector<Card> river = simdeck.deal(1);// add river
				if (river.size() != 1) {
					badriv++;
				}
				Vector<Card> table = createTable(flop, turn, river);
				if (table.size() != 5) {
					badtab++;
				} else {
					HAND.modifyTable(table);
					HAND.selfEval();
				}
				i++;
				simdeck = new Deck();
			}

		}
		/**
		 * Err Report printout System.out.println("* * * {ERROR REPORT} * * *");
		 * System.out.println(badhand+" malformed hands out of "+N);
		 * System.out.println(badflop+" malformed flops out of "+N);
		 * System.out.println(badturn+" malformed turns out of "+N);
		 * System.out.println(badriv +" malformed rivers out of "+N);
		 * System.out.println(badtab+ " malformed tables out of "+N);
		 */

		Vector<Card> createTable(Vector<Card> flop, Vector<Card> turn, Vector<Card> river) {
			Vector<Card> table = new Vector<>();
			for (Card f : flop) {
				table.add(f);
			}
			for (Card t : turn) {
				table.add(t);
			}
			for (Card r : river) {
				table.add(r);
			}
			return table;
		}
	}

	/** Generic <HAND> class */
	public static class HAND {

		static Vector<Card> holding = new Vector<>();
		static Vector<Card> table = new Vector<>();

		public boolean pocketpair = false;
		public boolean pair = false;
		public boolean twopair = false;
		public boolean flush = false;
		public boolean trips = false;

		static Vector<Card> pairouts = new Vector<>();
		static Vector<Card> suitouts = new Vector<>();
		static Vector<Card> flushout = new Vector<>();
		static Vector<Card> straightout = new Vector<>();

		Map<Card, Vector<Card>> popts = new HashMap<>();
		Map<Card, Vector<Card>> suits = new HashMap<>();

		public HAND(Vector<Card> cards, Map<Card, Vector<Card>> paired, Map<Card, Vector<Card>> suited) {

			HAND.holding = cards;
			popts = paired;
			suits = suited;
			// Determine potential hands already, and outs to look for
			inthePocket();
			System.out.print(HAND.flushout.size() + " outs for a flush and ");
			System.out.println(HAND.pairouts.size() + " pair outs possible for Hand: ");
			for (Card cahd : HAND.holding) {
				cahd.showMe();
			}
		}

		void inthePocket() {
			// Clear old buffers
			pairouts.clear();
			suitouts.clear();
			flushout.clear();

			// quick pocket pair check
			if (HAND.holding.get(0).rank == HAND.holding.get(1).rank) {
				pocketpair = true;
			}
			if (pocketpair) {
				System.out.println("Pocket Pair!");
			} // only for debug
			else {
				for (Map.Entry<Card, Vector<Card>> entry : popts.entrySet()) {
					// pair outs for first card in hand
					if (entry.getKey().rank == HAND.holding.get(0).rank
							&& (entry.getKey().suit.compareTo(HAND.holding.get(0).suit) == 0)) {
						for (Card c : entry.getValue()) {
							pairouts.add(c);
						}
					} // pair outs for the second card in hand
					if (entry.getKey().rank == HAND.holding.get(1).rank
							&& (entry.getKey().suit.compareTo(HAND.holding.get(1).suit) == 0 && pocketpair != true)) {
						for (Card d : entry.getValue()) {
							pairouts.add(d);
						}
					}
				}
			} // Now look for potential flush outs
			for (Map.Entry<Card, Vector<Card>> entry : suits.entrySet()) {
				// check for suited outs on first card in hand
				if (entry.getKey().rank == HAND.holding.get(0).rank
						&& (entry.getKey().suit.compareTo(HAND.holding.get(0).suit) == 0)) {
					for (Card f : entry.getValue()) {
						flushout.add(f);
					}
				} // check the second card in hand for suited outs
				if (entry.getKey().rank == HAND.holding.get(1).rank
						&& (entry.getKey().suit.compareTo(HAND.holding.get(0).suit) == 0)) {
					for (Card e : entry.getValue()) {
						flushout.add(e);
					}
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
		static void selfEval() {

		}
		/** */
		Vector<Card> bestSelf() {
			Vector<Card> besthand = new Vector<Card>();

			return besthand;
		}

	}/** EndOfHAND*/

}/** <ENDof_:<<AI><><> */