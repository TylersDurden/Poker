#!/bin/bash
javac Deck.java Card.java Player.java 
javac Table.java Game.java AI.java
# create .csv for training data
#rm training.txt
#touch training.txt
# Now Generate Training Data
echo 'Simulating Poker Hands'
java AI 10 > training.txt
echo 'Finished Generating Training Data.'
echo 'Results dumped into training.txt'
javac -Xlint:unchecked Neuron.java Classifier.java
# Label the Training Data/Begin Learning of Hand Classification 
java Neuron training.txt
