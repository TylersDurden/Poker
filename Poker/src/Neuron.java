/** NEURON.java */
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

/**<NEURON>
 * Classify and label the training set, by teaching program
 * to classify Poker Hands. 
 * @author ScottRobbins
 */
public class Neuron {
    
    public final int S0_size = 2;
    public final int S1_size = 3;
    public final int S2_size = 4;
    public final int S3_size = 5;
    
    public Perceptron selfconv;
    public static int DEPTH;
    
    public int SIZE;
    public Vector<Card> SELF = new Vector<>();
    public Map<Card,Vector<Card>> OUTS = new HashMap<>();
    public static Vector<Vector<Card>> STATES = new Vector<>();
    public boolean RESET = false;
    
    public Neuron(String cards){
        int length_classifier = cards.split(" ").length;
        this.DEPTH=0;
        switch(length_classifier){
            case(S0_size):
                prepSTATE0Neuron(cards.split(" "));
                this.SIZE = S0_size;
                break;
            case(S1_size):
                prepSTATE1Neuron(cards.split(" "));
                this.SIZE = S1_size;
                break;
            case(S2_size):
                prepSTATE2Neuron(cards.split(" "));
                this.SIZE = S2_size;
                break;
            case(S3_size):
                prepSTATE3Neuron(cards.split(" "));
                this.SIZE = S3_size;
                break;
            default:
                break;
        }
        /**Once all the Neurons are Prepped...<ACTIVATE>!*/ 
        
    }
    /** <Instantiate_PreFlop_Neurons> */
        void prepSTATE0Neuron(String [] cards){
            // What Matters for pre-flop conditions?
            createSelfInstance(cards);
            Perceptron p0 =  new Perceptron(this.SELF);
            this.selfconv = p0;
        }
        /** <Instantiate_Flop_Neurons> */
        void prepSTATE1Neuron(String [] cards){
            //Whats important about flop?
            createSelfInstance(cards);
            Perceptron p1 = new Perceptron(this.SELF);
            this.selfconv = p1;
        }
        /** <Instantiate_Turn_Neurons> */
        void prepSTATE2Neuron(String [] cards){
            //What happens at the turn?
            createSelfInstance(cards);
            Perceptron p2 = new Perceptron(this.SELF);
            this.selfconv = p2;
        }
        /** <Instantiate_Table_Neurons> */
        void prepSTATE3Neuron(String [] cards){
            //What are the results at STATE3?
            createSelfInstance(cards);
            Perceptron p3 = new Perceptron(this.SELF);
            this.selfconv = p3;
        }
        
        /** <Instantiate_the_SELF_Vector> */
        void createSelfInstance(String [] cards){
            for(String card : cards){
                int rank = (int)(extract(card));
                String suit = card.split(String.valueOf(rank))[1];
                this.SELF.add(new Card(rank,suit));
            }
        }
        
        /** <Extract> double from input String */
        double extract(String str) {
            Double dig = 0.0;
            Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(str);
            while (m.find()){return Double.parseDouble(m.group(1));}
            return dig;
        }
        
        static Map<Integer,Vector<Perceptron>> convolveBatch(Vector<Neuron>S0,
                           Vector<Neuron>S1,
                           Vector<Neuron>S2,
                           Vector<Neuron>S3){
            boolean dims = false;
            Map<Integer,Vector<Perceptron>> statemap = new HashMap<>();
            Vector<Vector<Card>> states = new Vector<>();
            
            if(S0.size()==S1.size() && (S3.size()==S2.size()) 
            && (S0.size()==S2.size())){dims=true;}
            if(dims){
                //Create new Perceptron for every sum of States
                int index = 0;
                for(Neuron n : S0){
                    Vector<Perceptron> stateconfig = new Vector<>();
                    Vector<Card> complete = new Vector<>();
                    stateconfig.add(n.selfconv);
                    stateconfig.add(S1.get(index).selfconv);
                    stateconfig.add(S2.get(index).selfconv);
                    stateconfig.add(S3.get(index).selfconv);
                    // Now add the cards themselves
                    Vector<Card> holding = n.SELF;
                    Vector<Card> visible = S3.get(index).SELF;
                    for(Card h : holding){complete.add(h);}
                    for(Card v : visible){complete.add(v);}
                    states.add(complete);
                    statemap.put(index,stateconfig);
                    index += 1;
                }
                
            }
            Neuron.STATES = states;
            return statemap;
        }

    

    public static void main(String []args){
        if(args.length<1){System.out.println("Incorrect Usage.");}
        else{
            TrainingDataPipeLine tdpl = new TrainingDataPipeLine(args[0]);
             if(tdpl.goodDims){System.out.println("Correct Dimensions");}
            // Create Neurons By STATE 
            Vector<Neuron> NEURONS0 = new Vector<>();
            Vector<Neuron> NEURONS1 = new Vector<>();
            Vector<Neuron> NEURONS2 = new Vector<>();
            Vector<Neuron> NEURONS3 = new Vector<>();
            for(String hand : tdpl.HANDS){NEURONS0.add(new Neuron(hand));}
            for(String flop : tdpl.FLOPS){NEURONS1.add(new Neuron(flop));}
            for(String turn : tdpl.TURNS){NEURONS2.add(new Neuron(turn));}
            for(String cards: tdpl.TABLES){NEURONS3.add(new Neuron(cards));}
            /** Debug PrintOut */
            System.out.println(NEURONS0.size()+" STATE0 Neurons Instantiated");
            System.out.println(NEURONS1.size()+" STATE1 Neurons Instantiated");
            System.out.println(NEURONS2.size()+" STATE2 Neurons Instantiated");
            System.out.println(NEURONS3.size()+" STATE3 Neurons Instantiated");
            //Take a peek at first set of states for debugging 
            //for(Card c : NEURONS3.get(0).SELF){c.showMe();}
            Map<Integer,Vector<Perceptron>> STATE_MAP = 
            Neuron.convolveBatch(NEURONS0,NEURONS1,NEURONS2,NEURONS3);
            //Activate with Neuron.STATES and the STATE_MAP to determine 
            //outocme label 
            Classifier labeler = new Classifier(STATE_MAP,Neuron.STATES);
            
           }
        }
    
    public static class TrainingDataPipeLine implements Runnable{
        // Raw text file data picked up by the FileReader
        public String fname;
        public Vector<String> DATAIN = new Vector<>();
        // Parse out the relevant content : 
        public Vector<String> HANDS  = new Vector<>();
        public Vector<String> FLOPS  = new Vector<>();
        public Vector<String> TURNS = new Vector<>();
        public Vector<String> TABLES = new Vector<>();
        //Checks for states of completion
        public boolean doneReading = false;
        public boolean goodDims = false;
        
        public TrainingDataPipeLine(String trainfile){
            this.fname  = trainfile;            
            this.DATAIN = fetchTrainingData();    
            //System.out.println(this.DATAIN.get(0));;}
            run();
            if(doneReading){//DEBUG - check variables
                if((this.HANDS.size()==this.FLOPS.size()) &&
            (this.TURNS.size()==this.TABLES.size())){this.goodDims=true;}
                        
                }
        }
        
        /** Read <training.txt> file */
        Vector<String> fetchTrainingData(){
            Vector<String> content = new Vector<>();
            BufferedReader br = null;
            try{
                File f = Paths.get(System.getProperty("user.dir"),this.fname).toFile();
                br = new BufferedReader(new FileReader(f));
                String ln = null; 
                while((ln = br.readLine()) != null){content.add(ln);}
            }catch(FileNotFoundException e){System.out.println("File Not Found!");}
            catch(IOException e){e.printStackTrace();}
            // Finally Return the content read 
            return content;
            
        }
        /** <RUN_IT> */
        public void run(){
            /** Every element is actually 1ROUND */ 
           for(String ROUND : this.DATAIN){
               this.HANDS.add(ROUND.split(",")[0]);
               this.FLOPS.add(ROUND.split(",")[1]);
               this.TURNS.add(ROUND.split(",")[2]);
               this.TABLES.add(ROUND.split(",")[3]);
           }
           this.doneReading = true; 
        }
        
        
  }/**<EndOf_TrainingDataPipeLine>*/
 
  /** <PERCEPTRON:> 
   * 
   */
  public static class Perceptron {
      
      //For each Rank, how many diff cards with this rank are present?
      public Map<Integer,Integer> rankedBinCounts = new HashMap<>(); 
      //For each Suit, how many diff cards with same suit are present?
      public Map<String, Integer> suitedBinCounts = new HashMap<>();
      //For each Rank, how many diff cards present build a straight? 
      public Map<Integer,Integer> straitBinCounts = new HashMap<>();
      
      // These are crude Memory Cells 
      public Vector<Card> pair    = new Vector<>();
      public Vector<Card> twopair = new Vector<>();
      public Vector<Card> threek  = new Vector<>();
      public Vector<Card> flush   = new Vector<>();
      public Vector<Card> strait  = new Vector<>();
      // The more rare arrangements 
      public Vector<Card> fullhouse = new Vector<>();
      public Vector<Card> strtflush = new Vector<>();
      public Vector<Card> fourkind  = new Vector<>();
      public Vector<Card> rylflush  = new Vector<>();
      
      
      //Contruct Perceptron
      public Perceptron(Vector <Card> cardsIn){
          /** Keep this as Generic as possible */
          //For following: If Cards aren't same record distinctions
          //Compare Ranks
          this.rankedBinCounts = rankCheck(cardsIn);
          //Compare Suits
          this.suitedBinCounts = suitCheck(cardsIn);
          //Proximity check
          this.straitBinCounts = nearCheck(cardsIn);
          ////////////////////////////////////////////////////
          //TODO: Now draw inferences from bin observations //
          enumerateFeaturesFromBinCounts();
      }
      
      /** <Compare_Input_Card_Ranks>*/
      Map<Integer,Integer> rankCheck(Vector<Card>cards){
          Map<Integer,Integer> rankoutfound = new HashMap<>();
          Vector<Integer>ranks = new Vector<>();
          Vector<String> suits = new Vector<>();
          int size = cards.size();
          for(Card c : cards){ranks.add(c.rank); suits.add(c.suit);}
          // Now check for matches, that aren't same card
          for(Card c : cards){
              int i = 0;
              int count = 0;
              for(Integer r : ranks){
                  if(c.rank==r && c.suit!=suits.get(i)){count+=1;}
                  i+=1;
              }
              rankoutfound.put(c.rank,count);   
          }
          //Verify this works
          return rankoutfound;
      }
      
      Map<String,Integer> suitCheck(Vector<Card>cards){
          Map<String,Integer> suitoutfound = new HashMap<>();
          Vector<Integer>ranks = new Vector<>();
          Vector<String>suits = new Vector<>();
          for(Card c : cards){ranks.add(c.rank); suits.add(c.suit);}
          //Now check for suit matches, that are diff cards
          Map<String,Integer> counter = new HashMap<>();
          for(String s : Card.types){counter.put(s,0);}
          for(Card d : cards){
              int i = 0;
              int count = 0;
              for(String suit : suits){
                  if(suit.compareTo(d.suit)==0 && (d.rank)!=ranks.get(i)){count+=1;}
                  i += 1;
              }
              int newCount = counter.get(d.suit)+count;
              suitoutfound.put(d.suit,newCount);
              //Not sure if this works? 
          }
          return suitoutfound;
      }
      
      Map<Integer,Integer> nearCheck(Vector<Card>cards){
          Map<Integer,Integer> strtoutfound = new HashMap<>();
          for(Card c : cards){
              int outs = 0;
              for(Card d : cards){
                  if(Math.abs(c.rank-d.rank)<=4 && (c.rank!=d.rank)){
                      outs++;
                  }
              }
              strtoutfound.put(c.rank,outs);
          }
          return strtoutfound;
      }
      
      /**TODO: Write this method */
      void enumerateFeaturesFromBinCounts(){
          //Start with ranked_Bin_Counts
          for(Map.Entry<Integer,Integer>entry:this.rankedBinCounts.entrySet()){
              if(entry.getValue()==2){/** <Pair?>*/}
              if(entry.getValue()==3){/** <Trip?>*/}
              if(entry.getValue()==4){/** <Quad?>*/}
              if(entry.getValue()>5){System.out.println("ERROR");}
          }
          //Check suited_Bin_Counts
          for(Map.Entry<String,Integer>entry:this.suitedBinCounts.entrySet()){
              //Do I care about near flushes at this stage? idts...
              if(entry.getValue()==5){/**Flush*/}
              if(entry.getValue()>5){System.out.println("ERROR");}
          }
          //check strait_Bin_Counts, also want highest ranks possible
          for(Map.Entry<Integer,Integer>entry:this.straitBinCounts.entrySet()){
              if(entry.getValue()==5){/** <Straight> */}
               
          }
          // What do these results mean for getting answer?
      }
      
      
  }/** <EndOf_PERCEPTRON>*/ 
 
  public static class Activation implements Runnable{
      
      public Activation(){
          
      }
      
      public void run(){}
  }
 
}/**<EndOf_NEURON>*/