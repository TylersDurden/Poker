import java.util.*;

/**GAME
 * @author ScottRobbins
 */
public class Game{
    
    public Dealer house;
    public Table table;
    public Vector<Player> players = new Vector<>();
    public static int STATE = 0; //Keep track of moving blinds after each hand
    
    public Game(Dealer deal, Table t, Vector<Player> ps){
        System.out.println("- - - - - - - - - - - - - - ");
        System.out.println("The Game is Texas Hold 'Em.\n"+
                           "Small Blinds start at 500\n" +
                           "Big Blinds start at 750 ");
        System.out.println("- - - - - - - - - - - - - - ");
        /** Set up the dealer [house], table and players */
        this.house = deal;
        this.table = t;
        this.players = ps;
        
        //Define the order of play, and whos big blind/small blind 
        anteUp();

        /** Deal each player their intial hand */       
       dealHandsToPlayers();
       System.out.println("Each Player Dealt a hand.");
       
       //Let players decide on checking/betting 
        
       
    }
    
    void anteUp(){
        if(Game.STATE==0){
            System.out.println(this.players.get(0).player+" has big blind ");
            System.out.println(this.players.get(1).player+" has small blind");
            for(Player p : this.players){
                if(p == this.players.get(0)){
                    this.table.setCurrentBet(this.table.smallblind);
                    //TODO: will p1 check/bet/fold the small blind?
                    }
                //If player 1 folds, small blind passed to p2
                if(p==this.players.get(1) && this.table.pot==0){
                    this.table.setCurrentBet(this.table.smallblind);
                    //TODO: will p2 check/bet/fold the small blind?
                    }
                if(p==this.players.get(1) && this.table.pot>0){
                    this.table.setCurrentBet(this.table.bigblind);
                    //TODO: will p2 check/bet/fold the big blind?
                }
                
                
            }
        }else{
            int big = (int)Game.STATE%1;
            int small = (int)Game.STATE%2;
            
        }
        
        
    }
    
    void betHandler(){
        
    }
    
    //Each player is initially dealt a 2 card hand  
    void dealHandsToPlayers(){
        for(Player p : this.players){p.getHand(this.house.cards.deal(2));}
        /**For Debug purposes, print player 1 hand to see.
        //this.players.get(0).showHand();
        //this.players.get(1).showHand();*/
       
    }
    
    
    
    
    public static void main(String[]args){
        
        
        /**Create the <Dealer> for the Game */
        Dealer d = new Dealer();
        /**Initiate group of <Player> */
       //Start each player with 5000
       Player p1 = new Player("Mac",5000);
       Player p2 = new Player("Dennis",5000);
       Player p3 = new Player("Charlie",5000);
       
       Vector<Player>players = new Vector<>();
       players.add(p1);
       players.add(p2);
       players.add(p3);
       
        /**Create a <Table> for players */
       Table tble = new Table(3,players);
       
        /** <:RUN_GAME:> */
        Game g = new Game(d,tble,players);
    }
    
}