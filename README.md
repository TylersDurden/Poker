# Poker
Command Line Texas Hold 'em  [Under Development]

## Game Play (*Quick Hold 'em Overview*)
User will play up to 3 bots at a time. Each player begins with the same 5000
chips. After every player chooses to buy in (and eventually if I get to it,
have big/small blinds) a round of betting, checking or folding occurs. 

Three cards, called the flop, are dealt to the table. These cards are common
to **all** players. After an additional round of player actions, a fourth 
card is dealt to the table. Again, each player at the table is given the choice
of checking, betting or folding. Finally, a fifth card called the River is 
dealt to the table. After the final round of bets the players show their cards
and the winner takes all. 

In the case of a tie the **pot** (sum of all bets made during round) is split
between players. 


# AI Development Strategy 
There are a finite number of cards, and a finite possible combination of cards
that make up all the valid states of every poker hand. This is great because
it means that the end goal of finding the best possible hand from a Bot's two
pocket cards, and the 5 table cards can be modeled as a series of Markov state
transitions [initial hands, the flop, turn and river].  

## Creating Training Data 
Using the AI class I generate large samples of training data, containing 4 states.
The first being the two cards dealt to a player, the second is the flop, the third 
the turn and the fourth the river and the whole table. The values are comma separated,
and saved into the training.txt file. 

## Pre-Processing the Data
First the training data is labeled with a classification class written in Java 
called Neuron. This classifier is unsupervised, and will likely become relevant
again downstream. For now it simply labels the training data. 

After passing through Neuron, the training data has been properly labeled and is
therefore ready a more complicated Neural Network structure that will be written 
in Python.

## Learning From Training Data
The goal is that by providing a network with a very large set of 4 consecutive 
sequences of Cards, with a final logical outcome label, the intent is to create
an ability to use Decision Trees and AutoEncoding to select favorable actions 
at any given state (based on the learned knowledge of which decisions are best). 

![Example Output](https://raw.githubusercontent.com/TylersDurden/Poker/master/ExOut.png)

