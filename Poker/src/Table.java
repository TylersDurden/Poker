import java.util.*;

/** Table
 * Table object class 
 *@Dependencies Card.java, Deck.java, Player.java , Dealer.java
 *@param int nPlayers
 *@author ScottRobbins 
 * For more information: 
 * {@link https://github/com/TylersDurden/Poker}
 */
public class Table{
    
    public int smallblind = 500;
    public int bigblind = 750;
    public static int seats; 
    public Vector<Player> players = new Vector<>();
    
    public Table(int nseats,Vector<Player>ppl){
        Table.seats = nseats;
        this.players = ppl;
        System.out.println("Created poker table with "+nseats+" players");
        
        greetPlayers(ppl);
    }
    
    void greetPlayers(Vector<Player>ps){
        System.out.println("Welcome to the table: ");
        for(Player p : ps){System.out.print(p.player+" ");}
        System.out.println("");
    }
    
    public static void main(String[]args){}
    
}