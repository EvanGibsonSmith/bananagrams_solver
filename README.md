
This project has the purpose of creating a bananagrams solver using A*. This is still a work in progress at the moment.
Currently, running src/main/game/run/runCheatPlayer allows you to add the tiles from the game you would be playing in real life and 
solve it for a solution. There are still some shortcomings in it's capabilites, which could likely be improved with optimizations and better
heuristics. To quantify this performance better, benchmark tests could be created. While much of the game is implemented, the "game" object that would handle a game between players on the computer is not completed. Because of this, at the moment there is no GUI (except for the outputs to show you what to play within the terminal) or playable game within the code. Of course, all of the critical pieces are there, because they are needed to create a solver.

Dependencies:
    junit5

Since this project only uses standard java libraries, aside from junit5, there is no dependency manager used.