 #!/bin/bash
javac Card.java Game.java Player.java Table.java Deck.java
javac -Xlint:unchecked Classifier.java Neuron.java AI.java 
java Neuron training.txt
