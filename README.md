This is here to keep me accountable and to not forget it later. 

The idea of this is to create a little GUI of bananagrams, maybe for a player to play, or maybe just to demonstrate the capabilites of a computer in this game. Using a life of words and creating a method for creating valid words (checking all the added words to see if valid etc etc.) we can try and solve this quickly as a computer. Since the solution space is incredibly large and there are many solutions, an A star search (maybe in Java or another language of that sort?) will be best with a heuristic function based on the number of letters left. This can always be an underestimate too because of dumping I think but I'll have to look into it more later.

Dependencies:
I haven't set up Maven or anything but unit is required for the test files. Otherwise nothing