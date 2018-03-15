#!/bin/bash
javac -Xlint:unchecked AI.java Game.java
javac Player.java Deck.java Card.java Table.java Player.java
# create .csv for training data
touch training.txt
# Now Generate Training Data
for i in {1..100}
    do
    java AI >> training.txt
done
