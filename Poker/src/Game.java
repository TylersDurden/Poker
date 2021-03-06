import java.util.*;

/**
 * <GAME>
 *  ____    ___   _   __  ____   ____
 * | n  |  | _ | | |/  / |  __| | n  |
 * | U _|  || || |    /  | |__  | U  /
 * |  |    ||_|| | |\ \  | ___| | |\ \
 * |__|    |___| |_| \_\ | |__  |_| \_\
 ***************************
 * @author ScottRobbins
 */
public class Game {
	public Deck house;
	public Table table;
	public Vector<Player> players = new Vector<>();
	public static Vector<AI> Bots = new Vector<>();
	public static int STATE = 0; // Keep track of moving blinds after each hand

	public Game(Deck d, Table t, Vector<Player> ps) {
		/** Set up the dealer [house], table and players */
		this.house = d;
		this.table = t;
		this.players = ps;
		System.out.println("- - - - - - - - - - - - - - ");
		System.out.println("The Game is Texas Hold 'Em.\n" +
		                   "Small Blinds start at 500\n" + 
		                   "Big Blinds start at 750 ");
		System.out.println("- - - - - - - - - - - - - - ");
		// Define the order of play, and whos big blind/small blind
		anteUp();

		/** Deal each player their intial hand */
		Game.Bots = dealHandsToPlayers();
		System.out.println("Each Player Dealt a hand.");

		// Let players decide on checking/betting
		/**
		 * TODO: Have to add the betting mechanism, but first start figuring out
		 * how the bots can figure out who won. Then start to add card
		 * counting/deciding how good a hand is during game. Finally, use this
		 * to influence bots betting behavior.
		 */

		// Deal Flop
		this.table.addCardsToTable(this.house.deal(3));
		System.out.println("- - - - FLOP - - - - ");
		for (Card c : this.table.tablecards) {
			c.showMe();
		}

		// Deal Turn
		this.table.addCardsToTable(this.house.deal(1));
		//System.out.println("- - - - Turn - - - - ");
		
		// mac.reviewTable(this.table.tablecards);
		// dennis.reviewTable(this.table.tablecards);
		// charlie.reviewTable(this.table.tablecards);

		// Deal River
		this.table.addCardsToTable(this.house.deal(1));
		//System.out.println("- - - - River - - - -  ");
		
		// mac.reviewTable(this.table.tablecards);
		// dennis.reviewTable(this.table.tablecards);
		// charlie.reviewTable(this.table.tablecards);

		Game.Bots.get(0).reviewTable(this.table.tablecards);
		Game.Bots.get(1).reviewTable(this.table.tablecards);
		Game.Bots.get(2).reviewTable(this.table.tablecards);

		// Evaluate best hand for players left
		System.out.println("Who wins?");
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		for(Card last : this.table.tablecards){last.showMe();}
	}

	void anteUp() {
		if (Game.STATE == 0) {
			System.out.println(this.players.get(0).player + " has big blind ");
			System.out.println(this.players.get(1).player + " has small blind");
			this.players.get(0).setBigBlind();
			this.players.get(1).setSmallBlind();

			Game.STATE += 1;
		} else { // Have to determine who gets big/small next

			int i = 0;
			for (Player p : this.players) {
				if (p.bigblind == true) {
					p.bigblind = false;
				}
				if (p.smallblind == true) {
					p.smallblind = false;
					p.setBigBlind();
				}
				if (i > 0 && this.players.get(i - 1).bigblind == true) {
					p.setSmallBlind();
				}
				if (i > 0 && this.players.get(i - 1).smallblind == true) {
					p.setBigBlind();
				}
				if (p.bigblind) {
					System.out.println(p.player + " has big blind");
				}
				if (p.smallblind) {
					System.out.println(p.player + " has small blind");
				}
				i++;
			}
			// Now Let players decide if they want to continue

		}

	}

	void betHandler(Vector<Card> cards) {
		for (AI bot : Game.Bots) {
			bot.reviewTable(cards);
		}
	}

	// Each player is initially dealt a 2 card hand
	Vector<AI> dealHandsToPlayers() {
		Vector<AI> bots = new Vector<>();
		for (Player p : this.players) {
			p.getHand(this.house.deal(2));
		}
		AI mac = new AI(this.players.get(0));
		AI dennis = new AI(this.players.get(1));
		AI charlie = new AI(this.players.get(2));
		bots.add(mac);
		bots.add(dennis);
		bots.add(charlie);
		return bots;
	}

	public static void main(String[] args) {
	    System.out.println("****************************************");
	    System.out.println("*  ____    ___   _   __  ____   ____   *");
	    System.out.println("* | n  |  | _ | | |/  / |  __| | n  |  *");
	    System.out.println("* | U _|  || || |    /  | |__  | U  /  *");
	    System.out.println("* |  |    ||_|| | |\\ \\  | ___| | |\\ \\  *");
	    System.out.println("* |__|    |___| |_| \\_\\ | |__  |_| \\_\\ *");
	    System.out.println("*  - - - - - - - - - - - - - - - - -   *");
	    System.out.println("****************************************");
	    
	    
		if (args.length < 1) { // Args should be a player name
			System.out.println("< RUNNING: [Simulation-Mode] >");

			/** Create the <Dealer> for the Game */
			Deck d = new Deck();
			/** Initiate group of <Player> */
			// Start each player with 5000
			Player p1 = new Player("Mac", 5000);
			Player p2 = new Player("Dennis", 5000);
			Player p3 = new Player("Charlie", 5000);

			Vector<Player> players = new Vector<>();
			players.add(p1);
			players.add(p2);
			players.add(p3);

			/**
			 * Create a
			 * <Table>
			 * for players
			 */
			Table tble = new Table(3, players);

			/** <:RUN_GAME:> */
			Game g = new Game(d, tble, players);

		} else { ////////////////////////////////////////////////////////
			// TODO: Handle live gameplay with user once the AI
			// mechanics are worked out.
		}
	}

}
