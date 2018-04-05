import java.util.*;
import java.util.regex.*;

/** <:_[B]oosted[S]earch[T]ree_:>
 * @author ScottRobbins 
 * @date 3/23/18
 */
public class BST{
    
    public String [] possibleHands;
    Map<String,Vector<Card>> STATE0 = new HashMap<>();
    Map<String,Vector<Card>> STATE1 = new HashMap<>();
    Map<String,Vector<Card>> STATE2 = new HashMap<>();
    Map<String,Vector<Card>> STATE3 = new HashMap<>();
    
    /** <NETWORK_INPUTS>*/
    Vector<Vector<Card>> INPUT = new Vector<>();
    public Vector<Card> S0 = new Vector<>();
    public Vector<Card> S1 = new Vector<>();
    public Vector<Card> S2 = new Vector<>();
    public Vector<Card> S3 = new Vector<>();
    public Vector<Card> S4 = new Vector<>();
    public Vector<Card> S5 = new Vector<>();
    public Vector<Card> S6 = new Vector<>();
    
    /** <PREFLOP:NETWORK_WEIGHTS> */
   Map<String,Double> pocketpairweights = new HashMap<>();
   public final String [] pocketpairs = {"AA","KK","QQ","JJ","TT",
                                         "99","88","77","66","55",
                                         "44","33","22"};
    
    public BST(Map<String,Double>odds,Vector<String>data){
        this.possibleHands = Trainer.classes;
    }
    
    /** */
    public void createInputNodesFromTree(Vector<String>data){
        int faces = 0;
        int flopface = 0;
        int turnface = 0;
        int riverface = 0;
        for(String row : data){
            if(row.split(" ").length>6){
                String s0 = row.split(" ")[0];
                String s1 = row.split(" ")[1];
                String s2 = row.split(" ")[2];
                String s3 = row.split(" ")[3];
                String s4 = row.split(" ")[4];
                String s5 = row.split(" ")[5];
                String s6 = row.split(" ")[6];
                if(!s0.contains("J") && !s0.contains("Q") &&
                !s0.contains("K") && !s0.contains("A")){
                    int r = extract(s0);
                    String s = s0.split(String.valueOf(r))[1];
                    this.S0.add(new Card(r,s));
                }else{//S0 is a faceCard
                    if(s0.contains("J")){this.S0.add(new Card(11,s0.split("J")[1]));}
                    if(s0.contains("Q")){this.S0.add(new Card(12,s0.split("Q")[1]));}
                    if(s0.contains("K")){this.S0.add(new Card(13,s0.split("K")[1]));}
                    if(s0.contains("A")){this.S0.add(new Card(14,s0.split("A")[1]));}
                    faces ++;
                }
                if(!s1.contains("J") && !s1.contains("Q") && 
                !s1.contains("K") && !s1.contains("A")){
                    int rank = extract(s1);
                    String suit = s1.split(String.valueOf(rank))[1];
                    this.S1.add(new Card(rank,suit));
                    
                }else{//S1 is a FaceCard
                    if(s1.contains("J")){this.S1.add(new Card(11,s1.split("J")[1]));}
                    if(s1.contains("Q")){this.S1.add(new Card(12,s1.split("Q")[1]));}
                    if(s1.contains("K")){this.S1.add(new Card(13,s1.split("K")[1]));}
                    if(s1.contains("A")){this.S1.add(new Card(14,s1.split("A")[1]));}
                    faces++;
                }
                if(!s2.contains("J") && !s2.contains("Q") && 
                !s2.contains("K") && !s2.contains("A")){
                    int r2 = extract(s2);
                    String s = s2.split(String.valueOf(r2))[1];
                    this.S2.add(new Card(r2,s));  
                }else{//S2 is a FaceCard 
                    if(s2.contains("J")){this.S2.add(new Card(11,s2.split("J")[1]));}
                    if(s2.contains("Q")){this.S2.add(new Card(12,s2.split("Q")[1]));}
                    if(s2.contains("K")){this.S2.add(new Card(13,s2.split("K")[1]));}
                    if(s2.contains("A")){this.S2.add(new Card(14,s2.split("A")[1]));}
                    flopface++;
                }
                if(!s3.contains("J") && !s3.contains("Q") &&
                !s3.contains("K") && !s3.contains("A")){
                    int r3 = extract(s3);
                    this.S3.add(new Card(r3,s3.split(String.valueOf(r3))[1]));
                }else{//S3 is a Face Card 
                    if(s3.contains("J")){this.S3.add(new Card(11,s3.split("J")[1]));}
                    if(s3.contains("Q")){this.S3.add(new Card(12,s3.split("Q")[1]));}
                    if(s3.contains("K")){this.S3.add(new Card(13,s3.split("K")[1]));}
                    if(s3.contains("A")){this.S3.add(new Card(14,s3.split("A")[1]));}
                    flopface++;
                }
                if(!s4.contains("J") && !s4.contains("Q") && 
                !s4.contains("K") && !s4.contains("A")){
                    int r4 = extract(s4);
                    this.S4.add(new Card(r4,s4.split(String.valueOf(r4))[1]));
                }else{//S4 is a face card
                    if(s4.contains("J")){this.S4.add(new Card(11,s4.split("J")[1]));}
                    if(s4.contains("Q")){this.S4.add(new Card(12,s4.split("Q")[1]));}
                    if(s4.contains("K")){this.S4.add(new Card(13,s4.split("K")[1]));}
                    if(s4.contains("A")){this.S4.add(new Card(14,s4.split("A")[1]));}
                    flopface++;
                }
                if(!s5.contains("J") && !s5.contains("Q") && 
                !s5.contains("K") && !s5.contains("A")){
                    int r5 = extract(s5);
                    this.S5.add(new Card(r5,s5.split(String.valueOf(r5))[1]));
                }else{
                    if(s5.contains("J")){this.S5.add(new Card(11,s5.split("J")[1]));}
                    if(s5.contains("Q")){this.S5.add(new Card(12,s5.split("Q")[1]));}
                    if(s5.contains("K")){this.S5.add(new Card(13,s5.split("K")[1]));}
                    if(s5.contains("A")){this.S5.add(new Card(14,s5.split("A")[1]));}
                    turnface++;
                }
                if(!s6.contains("J") && !s6.contains("Q") && 
                !s6.contains("K") && !s6.contains("A")){
                    int r6 = extract(s6);
                    this.S6.add(new Card(r6,s6.split(String.valueOf(r6))[1]));
                }else{
                    if(s6.contains("J")){this.S6.add(new Card(11,s6.split("J")[1]));}
                    if(s6.contains("Q")){this.S6.add(new Card(12,s6.split("Q")[1]));}
                    if(s6.contains("K")){this.S6.add(new Card(13,s6.split("K")[1]));}
                    if(s6.contains("A")){this.S6.add(new Card(14,s6.split("A")[1]));}
                    riverface++;
                }
            }
        }
        
        double faceRatio = ((double)faces/70.0);//bc faces/1000 hands*100 = 1/70
        double faceFlop  = ((double)flopface/70.0);
        double faceTurn  = ((double)turnface/70.0);
        double rivTurn   = ((double)riverface/70.0);
        
        /** <:PrintOuts:>*/
        System.out.println("Odds of Face Cards in  pocket :  "+faceRatio+"%");       
        System.out.println("Odds of Face Cards in the Flop: "+faceFlop+" %");
        System.out.println("Odds of Face Card on the Turn : "+faceTurn+" %");
        System.out.println("Odds of Face Card on the River: "+rivTurn+"%");
        System.out.println(this.S0.size()+" STATE0 Neurons prepped.");
        System.out.println(this.S1.size()+" STATE1 Neurons prepped.");
        System.out.println(this.S2.size()+" STATE2 Neurons prepped.");
        System.out.println(this.S3.size()+" STATE3 Neurons prepped.");
        System.out.println(this.S4.size()+" STATE4 Neurons prepepd.");
        System.out.println(this.S5.size()+" STATE5 Neurons prepped.");
        System.out.println(this.S6.size()+" STATE6 Neurons prepped.");
    }
    
    /** Populate the <STATE_MAPS>
     *  [S0,S1,S2 and S3 respectively]
     */
    static void generateStateMap(Vector<String>data){
        //int count = 0;
        Map<String,String> cardOdds = new HashMap<>();
    }
    
    /** <Extract> double from input String */
    public int extract(String str) {
        int dig = 0;
        Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(str);
        while (m.find()){return Integer.parseInt(m.group(1));}
        return dig;
    }
    
    public static void main(String[]unused){}
    
    public static class CardShark implements Runnable{
        
        Map<String,Double> ODDS = new HashMap<>();
        Map<String,Integer> SCORES = new HashMap<>();
        Vector<Card> s0 = new Vector<>();
        Vector<Card> s1 = new Vector<>();
        Vector<Card> s2 = new Vector<>();
        Vector<Card> s3 = new Vector<>();
        Vector<Card> s4 = new Vector<>();
        Vector<Card> s5 = new Vector<>();
        Vector<Card> s6 = new Vector<>();
         //Simulate the 1000 hands like an actual game
         Map<Integer,Vector<Card>> hands_dealt = new HashMap<>();
         Map<Integer,Vector<Card>> flops_dealt = new HashMap<>();
         Map<Integer,Vector<Card>> turns_dealt = new HashMap<>();
         Map<Integer,Vector<Card>> rivrs_dealt = new HashMap<>();
        
        public CardShark(Vector<Card> S0,
                         Vector<Card> S1,
                         Vector<Card> S2,
                         Vector<Card> S3,
                         Vector<Card> S4, 
                         Vector<Card> S5,
                         Vector<Card> S6,
                         Map<String,Double> odds,
                         Map<String,Integer>scores){
            boolean handdims = false;
            boolean flopdims = false;
            boolean turndims = false;
            boolean rivrdims = false;
            if(S0.size()==S1.size()){handdims = true;}
            if(S2.size()==S3.size() && S3.size()==S4.size()){flopdims = true;}
            if(S4.size()==S0.size()&&S4.size()==S5.size()){turndims = true;}
            if(S6.size()==S1.size()){rivrdims = true;}
            if(handdims){System.out.println("STATE0 dims good");}
            if(flopdims){System.out.println("STATE1 dims good");}
            if(turndims){System.out.println("STATE2 dims good");}
            if(rivrdims){System.out.println("STATE3 dims good");}
            if(handdims && flopdims && turndims && rivrdims){
                this.ODDS = odds;
                this.SCORES = scores;
                this.s0 = S0;
                this.s1 = S1;
                this.s2 = S2;
                this.s3 = S3;
                this.s4 = S4;
                this.s5 = S5;
                this.s6 = S6;
                System.out.println("All dimensions aligned. Beginning Training.");
                run();
            }
        }
        
        public void run(){
      
         int index = 0;//All 7 SVectors have same indices! 
         for(Card c : this.s0){
             Vector<Card>hand = new Vector<>();
             Vector<Card>flop = new Vector<>();
             Vector<Card>turn = new Vector<>();
             Vector<Card>rivr = new Vector<>();
             //Now fill the vectors 
             hand.add(c);
             hand.add(this.s1.get(index));
             flop.add(this.s2.get(index));
             flop.add(this.s3.get(index));
             flop.add(this.s4.get(index));
             turn.add(this.s5.get(index));
             rivr.add(this.s6.get(index));
             this.hands_dealt.put(index,hand);
             this.flops_dealt.put(index,flop);
             this.turns_dealt.put(index,turn);
             this.rivrs_dealt.put(index,rivr);
             index++;
         }
            
        }
        
        /**
         * <PreFlopLearning>
         */
        public Map<String,Double> preFlopProbabilities(){
            Map<String,Double>odds = new HashMap<>();
            //Using only hands_dealt here and then a 
            //sample deck, and the initial Prob./Weights 
            //to make educated guesses abt PF.
            /** <Consider:_PFR_RFI_Strategies> */
        
            
           
          
            
            
            return odds;
        }
    
    }

    
}