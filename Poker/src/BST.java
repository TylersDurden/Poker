import java.util.*;

/** <BST>
 * @author ScottRobbins 
 * @date 3/23/18
 */
public class BST{
    
    public String [] possibleHands;
    Map<String,Vector<Card>> S0 = new HashMap<>();
    Map<String,Vector<Card>> S1 = new HashMap<>();
    Map<String,Vector<Card>> S2 = new HashMap<>();
    Map<String,Vector<Card>> S3 = new HashMap<>();
    
    public BST(Map<String,Double>odds,Vector<String>data){
        this.possibleHands = Trainer.classes;
    }
    
    /** Populate the <STATE_MAPS>
     *  [S0,S1,S2 and S3 respectively]
     */
    static void generateStateMap(Vector<String>data){
        //int count = 0;
        Map<String,String> cardOdds = new HashMap<>();
     

        
        
    }
    
    public static void main(String[]unused){}
    
}