//package Poker.Neural;

import java.util.*;


public class MAIN {
    
    //Use this class to organize the flow of the network. 
    public static void main(String[]args){
        
        //Create a deck
        Deck theDeck = new Deck();
        theDeck.shuffleDeck();
        
        //Create some training input 
        DataTrain.CardDataGenerator(theDeck);
        
        
        
    }
    
}
