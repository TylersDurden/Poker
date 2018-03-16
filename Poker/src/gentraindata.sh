#!/bin/bash
javac Deck.java Card.java Player.java 
javac Table.java Game.java AI.java
# create .csv for training data
touch training.txt
# Now Generate Training Data
echo 'Simulating 1000 Poker Hands'
#java AI 1000 >> training.txt
echo 'Finished Generating Training Data.'
echo 'Results dumped into training.txt'
javac Neuron.java 
# Label the Training Data/Begin Learning of Hand Classification 
java Neuron training.txt
