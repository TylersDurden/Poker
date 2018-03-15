import numpy as np 
import matplotlib.pyplot as plt
import pandas as pd
# Training Data will be saved in the same path as training.txt

def loadTrainingData():
    trainfile = open('training.txt','r')
    raw_train = []
    for line in trainfile:
        raw_train.append(line)
    print("Poker Hands for training data "+str(len(raw_train)))
    return raw_train


def parseColumns(raw):
    clean = {}
    n = 0
    for line in raw:
       ROUND = []
       for state in line.split(','):
           ROUND.append(state.strip())
       clean[n] = ROUND
       n+=1
    return clean

def main():
    # Load TrainingData from training.txt
    raw_training = loadTrainingData()
    # Parse out STATE0-STATE3
    training_data = pd.DataFrame(parseColumns(raw_training))
    
    
if __name__ == "__main__":
    main()