#!/bin/bash
javac -Xlint:unchecked AI.java Game.java
javac Player.java Deck.java Card.java Table.java Player.java
# create .csv for training data
touch training.txt
# Now Generate Training Data
echo 'Simulating 1000 Poker Hands'
java AI 1000 >> training.txt
echo 'Finished Generating Training Data.'
echo 'Results dumped into training.txt'
exit 0