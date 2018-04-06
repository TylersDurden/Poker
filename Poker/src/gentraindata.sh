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
rm pokerdata.txt
touch pokerdata.txt
for i in {1..10}
    do
    touch training"$i".txt
    java AI 100 > training"$i".txt
    # Combine all into one file 
    java Neuron training"$i".txt >> pokerdata.txt
    rm training"$i".txt
done
echo 'Success. Now classifying/labeling all hands'
# Now Get some stats about poker data, and BOOST decision tree 
javac Trainer.java BST.java 
java Trainer pokerdata.txt


# [3] Give the AI the 10 base logic data, 
# then sample hands for training. See how it does at 
# Predicting the river. 

echo 'Generating unfinished, unlabeled hands for evaluation'
touch unsupervised.txt
java Trainer unsupervised > unsupervised.txt
echo '1000 hands dumped into: /../Poker/src./unsupervised.txt'