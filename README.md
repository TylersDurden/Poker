# Poker
Command Line Poker game 

# Development Status
Just started this project, so it's currently under development. Initial gameplay is being worked on. 
Initially the game will run as a poker game between bots alone. Once this works, hopefully the bots
can be trained by running several games against each other before allowing the user to join. 

The maximum table size will be 4 players. This can be either a table of 4 bots, or a table with
one real user and 3 bots. 

## AI Strategy 
When a bot is dealt a hand, a map of all of the outs for any given
poker hand is contructed and the outs matching the cards the bot is holding 
are isolated. 

This way, when more cards are dealt, the program has relatively few cards it
knows will complete a given hand. 

This makes up the logical skeleton of the Poker Bot AI. 
