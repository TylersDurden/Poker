//package Poker.Neural;
import java.util.*;

public class DataTrain {

    public static Vector<Card> hand  = new Vector<>(); //deal a hand
    public static Vector<Card> table = new Vector<>();
    public static Vector<Player> bots = new Vector<>();
    public static void main(String[] args) {
    }

    public static Map<Integer, Vector<Card>> CardDataGenerator(Deck deck) {
        Map<Integer, Vector<Card>> cardhistory = new HashMap<>();

        int ntrials = 0;
        Player bot  = new Player("BOT" ,10000);
        Player bot2 = new Player("BOT2",10000);
        bots = new Vector<Player>();
        bots.add(bot);bots.add(bot2);
        deck.shuffleDeck();
        
        int nCards = (bots.size()*2 +5)*3;
        
        while (ntrials < 2) {
            System.out.println("\t\t\t## DEALING NEW CARDS ##");
            //Deal Player 1 their hand 
            DataTrain.hand = deck.dealCards(2);//deal a hand
            bot.getHand(DataTrain.hand);
            bot.setTable(DataTrain.table);
            //Now handle Player 2 
            bot2.getHand(deck.dealCards(2));
            bot2.setTable(DataTrain.table);
            
            //deal table cards (all at once for training)
            DataTrain.table = deck.dealCards(5);
            Vector<Card> trainingCards = new Vector<>();
            for (Card c : table) {
                trainingCards.add(c);
            }

            //Classify hands for all bots cards
            new CardClassification(DataTrain.table, bots);

            //Identify poker hands, potential/probable hands and the outs to make them
            CardConvolution CC = new CardConvolution(bots);
           
           
           //Reset for next hand 
            deck.shuffleDeck();
            bot.clearProbabilities();
            bot2.clearProbabilities();
            hand.clear();
            table.clear();

            //reset for multiple passes 
            if (deck.numCardsInPlay()==nCards) {
                deck.shuffleDeck();
                for (Map.Entry<Card,Boolean> entry : deck.cardsInPlay.entrySet()) {
                    deck.cardsInPlay.put(entry.getKey(),false);
                }

                ntrials++;
            }
        }


        return cardhistory;
    }


}
