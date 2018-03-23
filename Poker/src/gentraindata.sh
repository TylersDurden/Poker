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
echo 'Generating training_data with hands.'
rm trainingdata.txt
rm training0.txt
rm training1.txt
touch trainingdata.txt
touch training0.txt
touch training1.txt
java AI 100 > trainingdata.txt
java AI 500 >> training0.txt 
java AI 500 >> training1.txt
echo 'Success. Now classifying/labeling all hands'
rm pokerdata.txt
touch pokerdata.txt 
# Put it all together into one file 
java Neuron trainingdata.txt > pokerdata.txt
java Neuron training0.txt >> pokerdata.txt
java Neuron training1.txt >> pokerdata.txt
# Now Get some stats about poker data, and create improved decision tree 
javac Trainer.java
java Trainer pokerdata.txt


# [3] Give the AI the 10 base logic data, then 10K sample hands for training 
