#!/bin/bash
javac Deck.java Card.java Player.java 
javac Table.java Game.java AI.java
javac -Xlint:unchecked Neuron.java Classifier.java

# [1] Establish the 10 Essential Hand Classes 
# training.txt already has the 10 Basic Hand for Identification 
echo 'Generating Initial Logic Training Data.'
echo 'Labeling Raw data : training.txt'
# Label the Training Data/Begin Learning of Hand Classification
touch labels.txt 
java Neuron training.txt > labels.txt 
echo '10 Essential Poker hand Classes Identified :'
cat labels.txt

# [2] Now Generate/Label Training Data
echo 'Generating training_data with 10K hands.'
touch trainingdata.txt
java AI 10000 > trainingdata.txt 
echo 'Success. Now classifying/labeling all 10K hands'
# touch pokerdata.txt 
# java Neuron trainingdata.txt > pokerdata.txt

# [3] Give the AI the 10 base logic data, then 10K sample hands for training 
