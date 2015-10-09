Hunt the Wumpus
CS664
Team Members: Jan Horjus


 1.) How to build and test

 2.) Description of the Algorithm used to solve
     A.) Keep track of the properties of previously visited squares.
     B.) Have a function which can navigate a safe path (action sequence) to an unvisited square which is adjacent 
         to a visited square (edge square) without going through any other unvisited squares.
     C.) Have a function which determines which edge square to explore next. 
         i.) It should prefer the closest available edge square which can be proven safe. 
         ii.)How do you know which squares are safe?
             a.) If any edge square is adjacent to at least one non-breezy and at least one non-smelly square it is 
                 safe.
             b.) If more than one stench square is visited, any square adjacent to at least one non-breezy square 
                 and less than all known stench squares is safe.
             c.) It seems very hard to know much more than this, but you mostly don't need to. 
     D.) If no safe edge squares remain and the gold is still not found, we can try to kill the wumpus to proceed.  
         i.) If more than one stench square is visited, stand in one of them and fire the arrow into the un-visited 
             square adjacent to the others. Then proceed to explore the dead-wumpus square and keep looking for 
             the gold as before.
         ii.) If only one stench square is visited and it has multiple unvisited squares adjacent to it, pick one at
              random and fire the arrow into it. Then proceed to explore the square you fired the arrow into, and 
              proceed as before. 
         iii.) If the wumpus is dead, the "safe square" calculations become a bit simpler.  
     E.) If, after using the arrow, you again encounter a situation in which no safe edge squares remain and you 
         have not yet found the gold, throw an exception! The game is always supposed to be solvable, so either your 
         map is bad or your solving algorithm is.
     F.) Obviously, once you find the gold pick it up. Then stop exploring and back-track to the exit.
     
