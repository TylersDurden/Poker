import java.util.*;
import java.io.*;
import java.nio.*;

public class Classifier {
    
    public Classifier(Map<Integer,Vector<Object>>smap,
                      Vector<Vector<Card>>sdat){
        
        
    }
    public static void main(){
        
    }
    
    public static class DecisionTree implements Runnable{
        
        public int STATE;
        
        public DecisionTree(Vector<Card>cards){
            this.STATE = cards.size();
            run();
        }
        
        public void run(){
            
        }
    }
}